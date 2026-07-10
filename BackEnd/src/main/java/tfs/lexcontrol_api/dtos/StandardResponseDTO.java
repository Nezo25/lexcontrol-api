package tfs.lexcontrol_api.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record StandardResponseDTO<T>(
    boolean sucesso,
    String mensagem,
    String logDetalhado,
    T dados
) {
    public static <T> StandardResponseDTO<T> success(String mensagem, String logNarrativo, T dados) {
        String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String logCompleto = String.format("LOG_TRACE [%s]: %s", dataFormatada, logNarrativo);
        return new StandardResponseDTO<>(true, mensagem, logCompleto, dados);
    }
}
