package tfs.lexcontrol_api.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "escritorios")
@Setter
@Getter
@NoArgsConstructor
public class Escritorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_escritorio", nullable = false)
    private String nomeEscritorio;

    @Column(unique = true, nullable = false)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @OneToMany(mappedBy = "escritorio", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Advogado> advogados = new ArrayList<>();

    @Embedded
    private Endereco endereco;
}