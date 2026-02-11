package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.Notificacao;
import tfs.lexcontrol_api.repositories.ClienteRepository;
import tfs.lexcontrol_api.repositories.NotificacaoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component // Esta anotação diz ao Spring: "Crie este robô ao iniciar"
public class AgendadorDeCobranca {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Scheduled(cron = "0/30 * * * * *") // A cada 30 segundos
    @Transactional
    public void rotinaAutomatica() {
        LocalDate hoje = LocalDate.now();


        List<Cliente> vencidos = repository.findAllByDataDeVencimentoBeforeAndStatusNot(hoje, "ATRASADO");

        for (Cliente c : vencidos) {
            c.setStatus("ATRASADO");
            repository.saveAndFlush(c); // <--- Use o Flush aqui!

            Notificacao n = new Notificacao();
            n.setMensagem("Alerta: O boleto de " + c.getNomeCliente() + " venceu!");
            n.setDataNotificacao(LocalDateTime.now());
            n.setClienteId(c.getId());
            notificacaoRepository.save(n);

            System.out.println("Status persistido para: " + c.getNomeCliente());
        }
    }
}