package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tfs.lexcontrol_api.enums.ModeloDePagamento;
import tfs.lexcontrol_api.enums.StatusPagamento;

import java.time.LocalDate;

public record ClienteRequestDTO(
        @NotBlank String nomeCliente,
        @NotBlank String cpf,
        @NotBlank String rg,
        @NotNull LocalDate dataDeVencimento,
        @NotBlank String causa,
        StatusPagamento statusPagamento,
        ModeloDePagamento modeloDePagamento,
        double valorCausa,
        double valorParcela,
        double totalHonorarios,
        EnderecoDTO endereco
) {}