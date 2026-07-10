package tfs.lexcontrol_api.services;

import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.models.ContratoHonorario;
import tfs.lexcontrol_api.models.Fatura;

import java.util.List;

@Service
public class NotificacaoCobrancaService {

    public void dispararCargaInicial(ContratoHonorario contrato, List<Fatura> faturasGeradas) {
        System.out.println("Disparando notificações iniciais de faturamento...");
        if (contrato.getCliente() != null) {
            System.out.println("Cliente Base: " + contrato.getCliente().getNomeCliente());
            // Aqui o sistema vai ler telefone/email e disparar para os canais
            if (contrato.getCanaisNotificacao() != null) {
                System.out.println("Canais ativos: " + contrato.getCanaisNotificacao());
            }
        }
        System.out.println("Total de faturas geradas: " + faturasGeradas.size());
    }
}
