package tfs.lexcontrol_api.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String gerarLogFalha(String detalhe) {
        String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return String.format("LOG_TRACE [%s]: FALHA - %s", dataFormatada, detalhe);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponseDTO<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String camposComErro = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s: %s]", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        String detalhes = "Erros de validação encontrados: " + camposComErro;
        String logCompleto = gerarLogFalha(detalhes);

        StandardResponseDTO<Void> response = new StandardResponseDTO<>(
                false,
                "Erro de validação nos dados enviados.",
                logCompleto,
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardResponseDTO<Void>> handleRuntimeExceptions(RuntimeException ex) {
        String logCompleto = gerarLogFalha(ex.getMessage());

        StandardResponseDTO<Void> response = new StandardResponseDTO<>(
                false,
                "Ocorreu um erro no processamento da requisição.",
                logCompleto,
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardResponseDTO<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String mensagemExata = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String logCompleto = gerarLogFalha("Conflito de integridade no banco de dados (provável duplicidade de CPF/RG). " + mensagemExata);

        StandardResponseDTO<Void> response = new StandardResponseDTO<>(
                false,
                "Já existe um registro com estes dados únicos no sistema.",
                logCompleto,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponseDTO<Void>> handleGenericExceptions(Exception ex) {
        String logCompleto = gerarLogFalha("Erro interno inesperado no servidor: " + ex.getClass().getSimpleName());

        StandardResponseDTO<Void> response = new StandardResponseDTO<>(
                false,
                "Erro interno do servidor. Por favor, contate o administrador.",
                logCompleto,
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
