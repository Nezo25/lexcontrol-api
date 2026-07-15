package tfs.lexcontrol_api.events.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.enums.StatusContrato;
import tfs.lexcontrol_api.enums.TipoHonorario;
import tfs.lexcontrol_api.events.SentencaProcedenteEvent;
import tfs.lexcontrol_api.models.ContratoHonorario;
import tfs.lexcontrol_api.models.Processo;
import tfs.lexcontrol_api.repositories.ContratoHonorarioRepository;
import tfs.lexcontrol_api.repositories.ProcessoRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContratoHonorarioEventListener {

    private final ProcessoRepository processoRepository;
    private final ContratoHonorarioRepository contratoRepository;

    @Async
    @EventListener
    @Transactional
    public void handleSentencaProcedente(SentencaProcedenteEvent event) {
        log.info("Processando evento de Sentença Procedente para o CNJ: {}", event.getCnj());
        
        List<Processo> processos = processoRepository.findByCnj(event.getCnj());
        
        for (Processo processo : processos) {
            ContratoHonorario contrato = processo.getContrato();
            if (contrato != null && (contrato.getTipoHonorario() == TipoHonorario.SUCUMBENCIA || contrato.getTipoHonorario() == TipoHonorario.EXITO)) {
                if (contrato.getStatus() == StatusContrato.EM_ANDAMENTO) {
                    contrato.setStatus(StatusContrato.AGUARDANDO_EMISSAO_SUCUMBENCIA);
                    contratoRepository.save(contrato);
                    log.info("Contrato ID {} atualizado para AGUARDANDO_EMISSAO_SUCUMBENCIA.", contrato.getId());
                    // Aqui entraríamos com um disparo de E-mail ou Push Notification para o advogado avisando do ganho de causa
                }
            }
        }
    }
}
