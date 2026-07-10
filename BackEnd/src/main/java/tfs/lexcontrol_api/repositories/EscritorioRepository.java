package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Escritorio;

@Repository
public interface EscritorioRepository extends JpaRepository<Escritorio, Long> {
}
