package tfs.lexcontrol_api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
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

    @Column(unique = true, nullable = false, length = 20)
    private String oab;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(unique = true, length = 18)
    private String cnpj; // Corrigido de cpnj para cnpj

    @Column(name = "email_profissional", length = 150)
    private String emailProfissional;

    @Column(name = "data_de_cadastro", nullable = false)
    private LocalDate dataDeCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "especialidade", length = 50)
    private AreaJuridica especialidade;

    @Embedded
    private Endereco endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escritorio_id")
    @JsonBackReference
    private Escritorio escritorio;

    @ManyToMany(mappedBy = "advogados")
    private List<Cliente> clientes = new ArrayList<>();

    // Método auxiliar para garantir que a data seja preenchida na criação
    @PrePersist
    protected void onCreate() {
        if (this.dataDeCadastro == null) {
            this.dataDeCadastro = LocalDate.now();
        }
    }
}