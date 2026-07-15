package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_regra_rateio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegraRateio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private ContratoHonorario contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advogado_id", nullable = false)
    private Advogado advogado;

    @Column(name = "percentual_comissao", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualComissao;

}
