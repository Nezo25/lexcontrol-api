package tfs.lexcontrol_api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VencerCausaDTO(
        BigDecimal valorFinalArbitrado,
        int parcelas,
        LocalDate primeiroVencimento
) {}
