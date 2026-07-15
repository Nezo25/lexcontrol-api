package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.RegraRateio;
import tfs.lexcontrol_api.models.ContratoHonorario;
import java.util.List;

@Repository
public interface RegraRateioRepository extends JpaRepository<RegraRateio, Long> {
    List<RegraRateio> findByContrato(ContratoHonorario contrato);
}
