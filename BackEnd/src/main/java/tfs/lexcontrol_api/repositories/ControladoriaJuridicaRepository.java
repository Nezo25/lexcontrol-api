package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.repositories.projections.InadimplenciaPorAreaProjection;

import java.util.List;

@Repository
public interface ControladoriaJuridicaRepository extends JpaRepository<Fatura, Long> {

    @Query(value = "SELECT p.area_direito AS areaDireito, " +
                   "(SUM(CASE WHEN f.status_fatura = 'ATRASADA' THEN 1 ELSE 0 END) * 100.0 / COUNT(f.id)) AS taxaPercentual " +
                   "FROM tb_fatura f " +
                   "JOIN tb_contrato_honorario c ON f.contrato_id = c.id " +
                   "JOIN tb_processo p ON p.contrato_id = c.id " +
                   "GROUP BY p.area_direito", nativeQuery = true)
    List<InadimplenciaPorAreaProjection> calcularTaxaInadimplenciaPorArea();

    @Query(value = "SELECT AVG(DATEDIFF(f.atualizado_em, c.atualizado_em)) " +
                   "FROM tb_fatura f " +
                   "JOIN tb_contrato_honorario c ON f.contrato_id = c.id " +
                   "WHERE c.tipo_honorario = 'SUCUMBENCIA' AND f.status_fatura = 'PAGA'", nativeQuery = true)
    Double calcularMediaAgingSucumbencia();

}
