package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Processo;
import java.util.List;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    List<Processo> findByCnj(String cnj);
}
