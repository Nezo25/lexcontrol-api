package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import tfs.lexcontrol_api.enums.ModeloDePagamento;
import tfs.lexcontrol_api.enums.StatusPagamento;

import java.math.BigDecimal;
import java.util.Date;

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
    @Column(name= "cpf", nullable = false)
    private String cpf;
    @Column(name ="rg", nullable = false)
    private String rg;
    @Column(name = "data_de_vencimento", nullable = false)
    private Date dataDeVencimento;

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
    private Endereço endereco;
}