package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.ContratoHonorario;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.enums.StatusFatura;
import tfs.lexcontrol_api.enums.TipoHonorario;
import tfs.lexcontrol_api.repositories.ClienteRepository;
import tfs.lexcontrol_api.repositories.ContratoHonorarioRepository;
import tfs.lexcontrol_api.repositories.FaturaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final ContratoHonorarioRepository contratoRepository;
    private final FaturaRepository faturaRepository;
    private final NotificacaoCobrancaService notificacaoService;
    private final AsaasIntegrationService asaasIntegrationService;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ContratoHonorario emitirContratoComCobrancas(ContratoHonorario contrato) {
        
        // Sincronização Lazy (Opção B): Cadastra no Asaas só na hora de cobrar
        Cliente cliente = contrato.getCliente();
        if (cliente.getAsaasCustomerId() == null) {
            String asaasId = asaasIntegrationService.criarClienteNoAsaas(cliente);
            cliente.setAsaasCustomerId(asaasId);
            clienteRepository.save(cliente);
        }

        // 1. Salva o contrato mestre para gerar ID
        ContratoHonorario contratoSalvo = contratoRepository.save(contrato);
        int totalParcelas = (contratoSalvo.getTotalParcelas() != null) ? contratoSalvo.getTotalParcelas().getQuantidade() : 1;

        List<Fatura> faturasGeradas = new ArrayList<>();
        BigDecimal valorParcela = contratoSalvo.getValorTotal().divide(BigDecimal.valueOf(totalParcelas), 2, java.math.RoundingMode.HALF_UP);
        
        LocalDate vencimentoParcela = contratoSalvo.getDataPrimeiroVencimento();
        LocalDate dataTermino = contratoSalvo.getDataTerminoCobranca();
        int contador = 1;

        // Loop de provisionamento inteligente
        while (contador <= totalParcelas && (dataTermino == null || !vencimentoParcela.isAfter(dataTermino))) {
            Fatura fatura = new Fatura();
            fatura.setContrato(contratoSalvo);
            fatura.setNumeroParcela(contador + "/" + totalParcelas);
            fatura.setValor(valorParcela);
            fatura.setDataVencimento(vencimentoParcela);
            fatura.setStatus(StatusFatura.PENDENTE);
            fatura.setMeioPagamento(contratoSalvo.getMeioPagamentoPadrao());
            
            // Simulação de Link temporário (será atualizado pelo Asaas logo abaixo de forma assíncrona)
            fatura.setLinkPagamentoUrl("Aguardando geração do link...");
            
            faturasGeradas.add(fatura);

            // Incrementa o vencimento iterativamente com base no Enum de Frequência
            if (contratoSalvo.getFrequencia() == null) {
                vencimentoParcela = vencimentoParcela.plusMonths(1);
            } else {
                vencimentoParcela = switch (contratoSalvo.getFrequencia()) {
                    case BIMESTRAL -> vencimentoParcela.plusMonths(2);
                    case TRIMESTRAL -> vencimentoParcela.plusMonths(3);
                    case SEMESTRAL -> vencimentoParcela.plusMonths(6);
                    case ANUAL -> vencimentoParcela.plusYears(1);
                    case MENSAL -> vencimentoParcela.plusMonths(1);
                };
            }
            contador++;
        }

        List<Fatura> savedFaturas = faturaRepository.saveAll(faturasGeradas);
        
        // Define a Data de Término automaticamente
        if (!savedFaturas.isEmpty()) {
            contratoSalvo.setDataTerminoCobranca(savedFaturas.get(savedFaturas.size() - 1).getDataVencimento());
            contratoSalvo = contratoRepository.save(contratoSalvo);
        }
        
        contratoSalvo.setFaturas(savedFaturas);

        // Dispara orquestração de notificações assíncronas
        notificacaoService.dispararCargaInicial(contratoSalvo, savedFaturas);

        // INTEGRAÇÃO ASAAS: Dispara geração assíncrona de boletos
        for (Fatura faturaGerada : savedFaturas) {
            asaasIntegrationService.emitirCobranca(faturaGerada, cliente);
        }

        return contratoSalvo;
    }
}
