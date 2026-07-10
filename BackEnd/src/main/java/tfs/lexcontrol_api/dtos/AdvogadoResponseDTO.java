package tfs.lexcontrol_api.dtos;

import tfs.lexcontrol_api.enums.AreaJuridica;
import tfs.lexcontrol_api.models.Advogado;

import java.time.LocalDate;

public record AdvogadoResponseDTO(
        Long idAdvogado,
        String oab,
        String cpf,
        String cnpj,
        String emailProfissional,
        AreaJuridica especialidade,
        LocalDate dataDeCadastro,
        EscritorioResponseDTO escritorio,
        EnderecoDTO endereco
) {
    public AdvogadoResponseDTO(Advogado advogado) {
        this(
                advogado.getIdAdvogado(),
                advogado.getOab(),
                advogado.getCpf(),
                advogado.getCnpj(),
                advogado.getEmailProfissional(),
                advogado.getEspecialidade(),
                advogado.getDataDeCadastro(),
                advogado.getEscritorio() != null ? new EscritorioResponseDTO(advogado.getEscritorio()) : null,
                advogado.getEndereco() != null ? new EnderecoDTO(
                        advogado.getEndereco().getLogradouro(),
                        advogado.getEndereco().getNumero(),
                        advogado.getEndereco().getComplemento(),
                        advogado.getEndereco().getBairro(),
                        advogado.getEndereco().getCidade(),
                        advogado.getEndereco().getEstado(),
                        advogado.getEndereco().getCep()
                ) : null
        );
    }
}
