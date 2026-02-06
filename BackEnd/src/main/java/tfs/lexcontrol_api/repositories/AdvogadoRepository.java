package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Advogado;

import java.util.Optional;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
    Optional<Advogado> findByOab(String oab);
}