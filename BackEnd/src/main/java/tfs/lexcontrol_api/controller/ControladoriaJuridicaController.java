package tfs.lexcontrol_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import tfs.lexcontrol_api.repositories.projections.InadimplenciaPorAreaProjection;
import tfs.lexcontrol_api.services.ControladoriaJuridicaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class ControladoriaJuridicaController {

    private final ControladoriaJuridicaService controladoriaService;

    @GetMapping("/inadimplencia-por-area")
    public ResponseEntity<StandardResponseDTO<List<InadimplenciaPorAreaProjection>>> getInadimplenciaPorArea() {
        List<InadimplenciaPorAreaProjection> dados = controladoriaService.obterInadimplenciaPorArea();
        return ResponseEntity.ok(StandardResponseDTO.success(
                "Inadimplência por área calculada com sucesso",
                "Módulo de BI processou a query nativa de inteligência de risco.",
                dados
        ));
    }

    @GetMapping("/aging-sucumbencia")
    public ResponseEntity<StandardResponseDTO<Double>> getAgingSucumbencia() {
        Double aging = controladoriaService.obterMediaAgingSucumbencia();
        return ResponseEntity.ok(StandardResponseDTO.success(
                "Média de Aging de Sucumbência calculada",
                "Módulo de BI calculou o tempo médio de pagamento de sucumbências em dias.",
                aging
        ));
    }
}
