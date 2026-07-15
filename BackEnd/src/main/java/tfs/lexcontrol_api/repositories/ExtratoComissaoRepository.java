package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.ExtratoComissao;

@Repository
public interface ExtratoComissaoRepository extends JpaRepository<ExtratoComissao, Long> {
}
