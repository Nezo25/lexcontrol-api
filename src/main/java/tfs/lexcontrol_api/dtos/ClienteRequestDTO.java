package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.NotBlank;
import tfs.lexcontrol_api.enums.StatusPagamento;

public record ClienteRequestDTO(
        @NotBlank String nomeCliente,
        @NotBlank String causa,
        StatusPagamento statusPagamento,
        double valorCausa,
        EnderecoDTO endereco
) {}

