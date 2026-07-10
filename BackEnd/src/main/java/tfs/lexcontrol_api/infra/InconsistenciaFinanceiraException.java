package tfs.lexcontrol_api.infra;

public class InconsistenciaFinanceiraException extends RuntimeException {
    public InconsistenciaFinanceiraException(String message) {
        super(message);
    }
}
