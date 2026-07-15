package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tfs.lexcontrol_api.dtos.VencerCausaDTO;
import tfs.lexcontrol_api.enums.StatusContrato;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.ContratoHonorario;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.enums.StatusFatura;
import tfs.lexcontrol_api.enums.TipoHonorario;
import tfs.lexcontrol_api.repositories.ClienteRepository;
import tfs.lexcontrol_api.repositories.ContratoHonorarioRepository;
import tfs.lexcontrol_api.repositories.FaturaRepository;
import tfs.lexcontrol_api.infra.exceptions.RegraNegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        
        Cliente cliente = contrato.getCliente();
        if (cliente.getAsaasCustomerId() == null) {
            String asaasId = asaasIntegrationService.criarClienteNoAsaas(cliente);
            cliente.setAsaasCustomerId(asaasId);
            clienteRepository.save(cliente);
        }

        if (contrato.getTipoHonorario() == TipoHonorario.SUCUMBENCIA || contrato.getTipoHonorario() == TipoHonorario.EXITO) {
            contrato.setStatus(StatusContrato.EM_ANDAMENTO);
            return contratoRepository.save(contrato);
        }

        contrato.setStatus(StatusContrato.ATIVO);
        ContratoHonorario contratoSalvo = contratoRepository.save(contrato);
        int totalParcelas = (contratoSalvo.getTotalParcelas() != null) ? contratoSalvo.getTotalParcelas().getQuantidade() : 1;

        List<Fatura> faturasGeradas = new ArrayList<>();
        BigDecimal valorParcela = contratoSalvo.getValorTotal().divide(BigDecimal.valueOf(totalParcelas), 2, java.math.RoundingMode.HALF_UP);
        
        LocalDate vencimentoParcela = contratoSalvo.getDataPrimeiroVencimento();
        LocalDate dataTermino = contratoSalvo.getDataTerminoCobranca();
        int contador = 1;

        while (contador <= totalParcelas && (dataTermino == null || !vencimentoParcela.isAfter(dataTermino))) {
            Fatura fatura = new Fatura();
            fatura.setContrato(contratoSalvo);
            fatura.setNumeroParcela(contador + "/" + totalParcelas);
            fatura.setValor(valorParcela);
            fatura.setDataVencimento(vencimentoParcela);
            fatura.setStatus(StatusFatura.PENDENTE);
            fatura.setMeioPagamento(contratoSalvo.getMeioPagamentoPadrao());
            fatura.setLinkPagamentoUrl("Aguardando geração do link...");
            faturasGeradas.add(fatura);

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
        
        if (!savedFaturas.isEmpty()) {
            contratoSalvo.setDataTerminoCobranca(savedFaturas.get(savedFaturas.size() - 1).getDataVencimento());
            contratoSalvo = contratoRepository.save(contratoSalvo);
        }
        
        contratoSalvo.setFaturas(savedFaturas);
        
        final ContratoHonorario contratoFinal = contratoSalvo;

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                notificacaoService.dispararCargaInicial(contratoFinal, savedFaturas);
                for (Fatura faturaGerada : savedFaturas) {
                    asaasIntegrationService.emitirCobranca(faturaGerada, cliente);
                }
            }
        });

        return contratoSalvo;
    }

    @Transactional
    public ContratoHonorario executarCobrancaSucumbencia(Long contratoId, VencerCausaDTO dto) {
        ContratoHonorario contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new RegraNegocioException("Contrato não encontrado"));

        if (contrato.getStatus() != StatusContrato.AGUARDANDO_EMISSAO_SUCUMBENCIA) {
            throw new RegraNegocioException("Contrato não está aguardando emissão de sucumbência.");
        }

        contrato.setStatus(StatusContrato.ATIVO);
        contrato.setValorTotal(dto.valorFinalArbitrado());
        
        // Aqui precisaríamos salvar a parte perdedora no Asaas para emitir o boleto no nome dela.
        // Para simplificar, vou utilizar uma entidade Cliente "Fake" na memória para o Asaas,
        // ou registrar o pagadorSucumbencia direto.
        // O ideal é que o AsaasIntegrationService tenha um método que receba os metadados
        // do pagador da sucumbência e devolva o ID do Asaas.
        
        Cliente clientePagador = new Cliente();
        clientePagador.setNomeCliente(contrato.getNomePagadorSucumbencia());
        clientePagador.setCpf(contrato.getDocumentoPagadorSucumbencia());
        String asaasPagadorId = asaasIntegrationService.criarClienteNoAsaas(clientePagador);
        clientePagador.setAsaasCustomerId(asaasPagadorId);
        
        int totalParcelas = dto.parcelas();
        BigDecimal valorParcela = dto.valorFinalArbitrado().divide(BigDecimal.valueOf(totalParcelas), 2, java.math.RoundingMode.HALF_UP);
        LocalDate vencimentoParcela = dto.primeiroVencimento();
        
        List<Fatura> faturasGeradas = new ArrayList<>();
        
        for (int i = 1; i <= totalParcelas; i++) {
            Fatura fatura = new Fatura();
            fatura.setContrato(contrato);
            fatura.setNumeroParcela(i + "/" + totalParcelas);
            fatura.setValor(valorParcela);
            fatura.setDataVencimento(vencimentoParcela);
            fatura.setStatus(StatusFatura.PENDENTE);
            fatura.setMeioPagamento(contrato.getMeioPagamentoPadrao());
            fatura.setLinkPagamentoUrl("Aguardando geração do link...");
            faturasGeradas.add(fatura);
            
            vencimentoParcela = vencimentoParcela.plusMonths(1);
        }
        
        List<Fatura> savedFaturas = faturaRepository.saveAll(faturasGeradas);
        contrato.setFaturas(savedFaturas);
        contrato = contratoRepository.save(contrato);

        ContratoHonorario contratoFinal = contrato;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                notificacaoService.dispararCargaInicial(contratoFinal, savedFaturas);
                for (Fatura f : savedFaturas) {
                    asaasIntegrationService.emitirCobranca(f, clientePagador);
                }
            }
        });

        return contratoFinal;
    }
}
