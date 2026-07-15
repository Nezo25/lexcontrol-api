package tfs.lexcontrol_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import tfs.lexcontrol_api.events.SentencaProcedenteEvent;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks/tribunais")
@RequiredArgsConstructor
@Slf4j
public class TribunalWebhookController {

    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/push")
    public ResponseEntity<StandardResponseDTO<String>> receberPushTribunal(@RequestBody Map<String, String> payload) {
        log.info("Payload recebido do Tribunal: {}", payload);

        String cnj = payload.get("numeroProcesso");
        String tipoAndamento = payload.get("tipoAndamento");

        if (cnj == null || tipoAndamento == null) {
            return ResponseEntity.badRequest().body(StandardResponseDTO.error("Payload inválido", "Ausência de CNJ ou Tipo de Andamento"));
        }

        if ("SENTENCA_PRO_REU".equalsIgnoreCase(tipoAndamento) || "PROCEDENTE".equalsIgnoreCase(tipoAndamento)) {
            // Dispara evento interno de forma desacoplada
            eventPublisher.publishEvent(new SentencaProcedenteEvent(this, cnj));
            
            return ResponseEntity.ok(StandardResponseDTO.success(
                    "Evento processado com sucesso",
                    "Sentença favorável detectada. Motor de contratos ativado.",
                    cnj
            ));
        }

        return ResponseEntity.ok(StandardResponseDTO.success(
                "Evento ignorado",
                "Andamento processual não é uma sentença terminativa de ganho.",
                cnj
        ));
    }
}
