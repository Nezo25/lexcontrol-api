package tfs.lexcontrol_api.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List; // OBRIGATÓRIO
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor: " + ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(tfs.lexcontrol_api.infra.exceptions.RegraNegocioException.class)
    public ResponseEntity<ErrorResponse> handleRegraNegocio(tfs.lexcontrol_api.infra.exceptions.RegraNegocioException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(tfs.lexcontrol_api.infra.exceptions.RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNaoEncontrado(tfs.lexcontrol_api.infra.exceptions.RecursoNaoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InconsistenciaFinanceiraException.class)
    public ResponseEntity<ErrorResponse> handleInconsistencia(InconsistenciaFinanceiraException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos",
                System.currentTimeMillis(),
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflito de dados: Registro já existente no MySQL.",
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}