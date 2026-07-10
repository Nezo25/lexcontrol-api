package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.Fatura;

import tfs.lexcontrol_api.enums.StatusFatura;
import tfs.lexcontrol_api.repositories.FaturaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Slf4j
public class AsaasIntegrationService {

    private final RestClient restClient;
    private final FaturaRepository faturaRepository;
    private final ObjectMapper objectMapper;

    public AsaasIntegrationService(
            @Value("${asaas.api.url}") String apiUrl,
            @Value("${asaas.api.key}") String apiKey,
            FaturaRepository faturaRepository,
            ObjectMapper objectMapper) {
        
        this.faturaRepository = faturaRepository;
        this.objectMapper = objectMapper;
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(apiUrl)
                .defaultHeader("access_token", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String criarClienteNoAsaas(Cliente cliente) {
        log.info("Sincronizando cliente {} com Asaas...", cliente.getNomeCliente());

        Map<String, String> payload = Map.of(
                "name", cliente.getNomeCliente(),
                "cpfCnpj", cliente.getCpf(),
                "email", "cliente" + cliente.getId() + "@lexcontrol.com.br", // Mock email for now
                "phone", cliente.getTelefone() != null ? cliente.getTelefone() : ""
        );

        Map<String, Object> response = restClient.post()
                .uri("/customers")
                .body(payload)
                .retrieve()
                .body(Map.class);

        if (response != null && response.containsKey("id")) {
            String customerId = (String) response.get("id");
            log.info("Cliente sincronizado no Asaas com sucesso. ID: {}", customerId);
            return customerId;
        }
        
        throw new tfs.lexcontrol_api.infra.exceptions.RegraNegocioException("Falha ao criar cliente no Asaas");
    }

    @Async
    public void emitirCobranca(Fatura fatura, Cliente cliente) {
        log.info("[THREAD-SECUNDÁRIA] Emitindo cobrança da Fatura ID: {} no Asaas...", fatura.getId());

        String billingType = switch (fatura.getMeioPagamento()) {
            case PIX -> "PIX";
            case CARTAO_CREDITO -> "CREDIT_CARD";
            case BOLETO -> "BOLETO";
            default -> "UNDEFINED";
        };

        Map<String, Object> payload = Map.of(
                "customer", cliente.getAsaasCustomerId(),
                "billingType", billingType,
                "value", fatura.getValor(),
                "dueDate", fatura.getDataVencimento().format(DateTimeFormatter.ISO_LOCAL_DATE),
                "description", fatura.getContrato().getDescricaoCobranca() + " (Parcela " + fatura.getNumeroParcela() + ")",
                "externalReference", String.valueOf(fatura.getId())
        );

        try {
            Map<String, Object> response = restClient.post()
                    .uri("/payments")
                    .body(payload)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("invoiceUrl")) {
                String invoiceUrl = (String) response.get("invoiceUrl");
                log.info("Cobrança gerada com sucesso no Asaas! Link: {}", invoiceUrl);
                
                faturaRepository.findById(fatura.getId()).ifPresent(f -> {
                    f.setLinkPagamentoUrl(invoiceUrl);
                    faturaRepository.save(f);
                });
            }
        } catch (Exception e) {
            log.error("Falha ao emitir cobrança no Asaas para Fatura ID {}: {}", fatura.getId(), e.getMessage());
            faturaRepository.findById(fatura.getId()).ifPresent(f -> {
                f.setStatus(StatusFatura.ERRO_EMISSAO);
                faturaRepository.save(f);
            });
        }
    }

    @Transactional
    public void processarWebhook(String jsonRecebido) {
        log.info("WEBHOOK RECEBIDO DO ASAAS: {}", jsonRecebido);
        try {
            JsonNode rootNode = objectMapper.readTree(jsonRecebido);
            if (!rootNode.has("event") || !rootNode.has("payment")) {
                return;
            }

            String event = rootNode.get("event").asText();
            JsonNode paymentNode = rootNode.get("payment");

            if (paymentNode.has("externalReference") && !paymentNode.get("externalReference").isNull()) {
                String externalReference = paymentNode.get("externalReference").asText();
                try {
                    Long faturaId = Long.parseLong(externalReference);
                    
                    faturaRepository.findById(faturaId).ifPresent(fatura -> {
                        boolean changed = false;
                        if ("PAYMENT_RECEIVED".equals(event) || "PAYMENT_CONFIRMED".equals(event)) {
                            fatura.setStatus(StatusFatura.PAGA);
                            changed = true;
                            log.info("Fatura ID {} atualizada para PAGA pelo Webhook.", faturaId);
                        } else if ("PAYMENT_OVERDUE".equals(event)) {
                            fatura.setStatus(StatusFatura.ATRASADA);
                            changed = true;
                            log.info("Fatura ID {} atualizada para ATRASADA pelo Webhook.", faturaId);
                        } else if ("PAYMENT_DELETED".equals(event)) {
                            fatura.setStatus(StatusFatura.CANCELADA);
                            changed = true;
                            log.info("Fatura ID {} atualizada para CANCELADA pelo Webhook.", faturaId);
                        }

                        if (changed) {
                            faturaRepository.save(fatura);
                        }
                    });
                } catch (NumberFormatException e) {
                    log.error("externalReference inválido no Webhook: {}", externalReference);
                }
            }
        } catch (Exception e) {
            log.error("Falha ao processar Webhook do Asaas: {}", e.getMessage());
        }
    }
}
