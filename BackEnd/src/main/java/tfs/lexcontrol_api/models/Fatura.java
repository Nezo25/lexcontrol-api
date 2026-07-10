package tfs.lexcontrol_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfs.lexcontrol_api.enums.MeioPagamento;
import tfs.lexcontrol_api.enums.StatusFatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_fatura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    @JsonIgnore
    private ContratoHonorario contrato;

    @Column(name = "numero_parcela", nullable = false, length = 20)
    private String numeroParcela;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_fatura", nullable = false, length = 30)
    private StatusFatura status;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento", nullable = false, length = 30)
    private MeioPagamento meioPagamento;

    @Column(name = "link_pagamento_url")
    private String linkPagamentoUrl;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }
}
