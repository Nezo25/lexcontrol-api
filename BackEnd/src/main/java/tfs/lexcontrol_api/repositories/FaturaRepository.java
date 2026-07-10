package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Fatura;

import tfs.lexcontrol_api.enums.StatusFatura;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    List<Fatura> findAllByDataVencimentoBeforeAndStatus(LocalDate data, StatusFatura status);
}
