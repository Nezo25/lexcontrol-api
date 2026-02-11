package tfs.lexcontrol_api.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String mensagem;
    private long timestamp;
    private List<String> errors;

    public ErrorResponse(int status, String mensagem, long timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.errors = null;
    }
}