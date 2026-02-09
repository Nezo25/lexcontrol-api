package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Notificacao;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    // Busca apenas as notificações que o usuário ainda não visualizou
    List<Notificacao> findByLidaFalseOrderByDataNotificacaoDesc();

    // Caso queira contar quantas notificações novas existem para colocar uma "bolinha" no ícone
    long countByLidaFalse();

    List<Notificacao> findByLidaFalse();
}