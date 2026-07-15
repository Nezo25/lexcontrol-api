package tfs.lexcontrol_api.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentencaProcedenteEvent extends ApplicationEvent {
    
    private final String cnj;

    public SentencaProcedenteEvent(Object source, String cnj) {
        super(source);
        this.cnj = cnj;
    }
}
