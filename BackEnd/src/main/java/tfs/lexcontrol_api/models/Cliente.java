package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import tfs.lexcontrol_api.enums.ModeloDePagamento;
import tfs.lexcontrol_api.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cliente")
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;
    @Column(unique = true, nullable = false)
    private String cpf;
    @Column(unique = true, nullable = false)
    private String rg;
    @Column(name = "data_de_vencimento", nullable = false)
    private LocalDate dataDeVencimento;
    private String telefone;

    private String causa;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Column(name = "valor_causa", precision = 10, scale = 2)
    private BigDecimal valorCausa;
    @Enumerated(EnumType.STRING)
    private ModeloDePagamento modeloDePagamento;
    @Column(name = "valorParcela")
    private BigDecimal valorParcela;
    @Column(name= "totalHonorarios")
    private BigDecimal totalHonorarios;
    @Embedded
    private Endereco endereco;
    @ManyToMany
    @JoinTable(
            name = "cliente_advogado",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "advogado_id")
    )
    private List<Advogado> advogados = new ArrayList<>();
}