package tfs.lexcontrol_api.controller.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import tfs.lexcontrol_api.services.AsaasIntegrationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/webhooks/asaas")
public class AsaasWebhookController {

    @Value("${asaas.webhook.token}")
    private String tokenAutorizado;

    private final AsaasIntegrationService asaasIntegrationService;

    public AsaasWebhookController(AsaasIntegrationService asaasIntegrationService) {
        this.asaasIntegrationService = asaasIntegrationService;
    }

    @PostMapping
    public ResponseEntity<StandardResponseDTO<Void>> receberNotificacao(
            @RequestHeader(value = "asaas-access-token", required = false) String headerToken,
            @RequestBody String jsonDoAsaas) {

        // 1. Shield de Segurança: Se o token não bater, barra na hora
        if (headerToken == null || !tokenAutorizado.equals(headerToken)) {
            String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String logFalha = String.format("LOG_TRACE [%s]: FALHA - Tentativa de acesso não autorizada ao Webhook do Asaas.", dataFormatada);
            
            StandardResponseDTO<Void> responseErro = new StandardResponseDTO<>(
                    false,
                    "Acesso negado. Token de Webhook inválido.",
                    logFalha,
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseErro);
        }

        // 2. Encaminhar para o Service processar o JSON (ler se foi pago, vencido, etc.)
        asaasIntegrationService.processarWebhook(jsonDoAsaas);

        String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String logNarrativo = String.format("LOG_TRACE [%s]: Webhook do Asaas processado com sucesso. Evento de conciliação financeira registrado.", dataFormatada);
        
        StandardResponseDTO<Void> response = new StandardResponseDTO<>(
                true,
                "Evento processado", 
                logNarrativo, 
                null
        );
        
        return ResponseEntity.ok(response);
    }
}
