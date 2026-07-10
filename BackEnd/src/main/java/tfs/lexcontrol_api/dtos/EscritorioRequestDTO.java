package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record EscritorioRequestDTO(
        @NotBlank String nomeEscritorio,
        @NotBlank String cnpj,
        @NotBlank String razaoSocial,
        EnderecoDTO endereco
) {}