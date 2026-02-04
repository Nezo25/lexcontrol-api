package tfs.lexcontrol_api.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String mensagem;
    private long timestamp;
}