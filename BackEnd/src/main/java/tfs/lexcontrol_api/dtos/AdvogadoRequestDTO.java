package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tfs.lexcontrol_api.enums.AreaJuridica;
import java.time.LocalDate;

public record AdvogadoRequestDTO(
        @NotBlank String oab,
        @NotBlank String cnpj,
        @NotBlank String cpf,
    @NotNull AreaJuridica especialidade,
        Long escritorioId,
        EnderecoDTO endereco
) {}