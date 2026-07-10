package tfs.lexcontrol_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfs.lexcontrol_api.enums.AreaJuridica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advogados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advogado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_advogado")
    private Long idAdvogado;

    @Column(nullable = false, unique = true, length = 20)
    private String oab;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(unique = true, length = 18)
    private String cnpj;

    @Column(name = "email_profissional", length = 150)
    private String emailProfissional;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private AreaJuridica especialidade;

    @Column(name = "data_de_cadastro", nullable = false)
    private LocalDate dataDeCadastro;

    @ManyToOne
    @JoinColumn(name = "escritorio_id")
    private Escritorio escritorio;

    @Embedded
    private Endereco endereco;

    @JsonIgnore
    @ManyToMany(mappedBy = "advogados")
    private List<Cliente> clientes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (dataDeCadastro == null) {
            dataDeCadastro = LocalDate.now();
        }
    }
}
