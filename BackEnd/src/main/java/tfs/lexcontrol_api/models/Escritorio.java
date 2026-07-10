package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "escritorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Escritorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_escritorio", nullable = false, length = 150)
    private String nomeEscritorio;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Embedded
    private Endereco endereco;
}
