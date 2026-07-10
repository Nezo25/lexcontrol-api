package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
    private String telefone;

    @Column(name = "asaas_customer_id", length = 50)
    private String asaasCustomerId;

    @Embedded
    private Endereco endereco;
    
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "cliente_advogado",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "advogado_id")
    )
    private List<Advogado> advogados = new ArrayList<>();
}