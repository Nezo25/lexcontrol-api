package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_extrato_comissao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtratoComissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id", nullable = false)
    private Fatura fatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advogado_id", nullable = false)
    private Advogado advogado;

    @Column(name = "valor_receber", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorReceber;

    @Column(name = "status_pagamento", nullable = false, length = 30)
    private String statusPagamento = "PENDENTE";

}
