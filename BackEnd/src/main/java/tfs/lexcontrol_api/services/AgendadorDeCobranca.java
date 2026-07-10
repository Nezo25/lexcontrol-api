package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.enums.StatusFatura;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.models.Notificacao;
import tfs.lexcontrol_api.repositories.FaturaRepository;
import tfs.lexcontrol_api.repositories.NotificacaoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendadorDeCobranca {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Scheduled(cron = "0/30 * * * * *") // A cada 30 segundos (em prod, mude para rodar 1x por dia)
    @Transactional
    public void rotinaAutomatica() {
        LocalDate hoje = LocalDate.now();

        // Busca faturas PENDENTES que a data de vencimento seja anterior a hoje
        List<Fatura> faturasVencidas = faturaRepository.findAllByDataVencimentoBeforeAndStatus(hoje, StatusFatura.PENDENTE);

        for (Fatura fatura : faturasVencidas) {
            fatura.setStatus(StatusFatura.ATRASADA);
            faturaRepository.saveAndFlush(fatura);

            Notificacao n = new Notificacao();
            String nomeCliente = fatura.getContrato().getCliente().getNomeCliente();
            n.setMensagem("Alerta: A parcela " + fatura.getNumeroParcela() + " do cliente " + nomeCliente + " venceu!");
            n.setDataNotificacao(LocalDateTime.now());
            n.setClienteId(fatura.getContrato().getCliente().getId());
            notificacaoRepository.save(n);

            System.out.println("Fatura atrasada registrada para: " + nomeCliente);
        }
    }
}