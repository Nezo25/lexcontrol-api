package tfs.lexcontrol_api.infra.exceptions;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
