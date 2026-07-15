package tfs.lexcontrol_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfs.lexcontrol_api.enums.MeioPagamento;
import tfs.lexcontrol_api.enums.ModalidadeParcelamento;
import tfs.lexcontrol_api.enums.FrequenciaCobranca;
import tfs.lexcontrol_api.enums.CanalNotificacao;
import tfs.lexcontrol_api.enums.TipoHonorario;
import tfs.lexcontrol_api.enums.TotalParcelas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_contrato_honorario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoHonorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @Column(name = "processo_id")
    private Long processoId;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_parcelas", nullable = false, length = 10)
    private TotalParcelas totalParcelas;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade_parcelamento", nullable = false, length = 50)
    private ModalidadeParcelamento modalidadeParcelamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento_padrao", nullable = false, length = 30)
    private MeioPagamento meioPagamentoPadrao;

    @Column(name = "data_primeiro_vencimento", nullable = false)
    private LocalDate dataPrimeiroVencimento;

    @Column(name = "descricao_cobranca", nullable = false, length = 255)
    private String descricaoCobranca;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_honorario", nullable = false, length = 50)
    private TipoHonorario tipoHonorario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private tfs.lexcontrol_api.enums.StatusContrato status = tfs.lexcontrol_api.enums.StatusContrato.ATIVO;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia", length = 50)
    private FrequenciaCobranca frequencia;

    @Column(name = "data_termino_cobranca")
    private LocalDate dataTerminoCobranca;

    @Column(name = "percentual_juros_mes", precision = 5, scale = 2)
    private BigDecimal percentualJurosMes = BigDecimal.ZERO;

    @Column(name = "percentual_multa", precision = 5, scale = 2)
    private BigDecimal percentualMulta = BigDecimal.ZERO;

    @Column(name = "multa_valor_fixo")
    private Boolean multaValorFixo = false;

    @Column(name = "nome_pagador_sucumbencia", length = 150)
    private String nomePagadorSucumbencia;

    @Column(name = "documento_pagador_sucumbencia", length = 20)
    private String documentoPagadorSucumbencia;

    @ElementCollection(targetClass = CanalNotificacao.class)
    @CollectionTable(name = "tb_contrato_canais_notificacao", joinColumns = @JoinColumn(name = "contrato_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "canal_notificacao")
    private Set<CanalNotificacao> canaisNotificacao;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fatura> faturas = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
