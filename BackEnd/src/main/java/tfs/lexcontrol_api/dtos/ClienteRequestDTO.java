package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank @Size(max = 150) String nomeCliente,
        @NotBlank @Size(min = 11, max = 14) String cpf,
        @NotBlank @Size(max = 20) String rg,
        @NotBlank @Size(max = 20) String telefone,
        EnderecoDTO endereco
) {}