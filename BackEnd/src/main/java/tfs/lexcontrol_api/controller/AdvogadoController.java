package tfs.lexcontrol_api.controller;

import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.AdvogadoRequestDTO;
import tfs.lexcontrol_api.dtos.AdvogadoResponseDTO;
import tfs.lexcontrol_api.services.AdvogadoService;

import java.util.List;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {

    @Autowired
    private AdvogadoService advogadoService;

    @PostMapping
    public ResponseEntity<StandardResponseDTO<AdvogadoResponseDTO>> criarAdvogado(@RequestBody @Valid AdvogadoRequestDTO dto) {
        AdvogadoResponseDTO advogadoCriado = advogadoService.criar(dto);
        
        String logNarrativo = String.format("Advogado (OAB: %s) integrado ao corpo jurídico do escritório com sucesso.", 
                advogadoCriado.oab());
                
        StandardResponseDTO<AdvogadoResponseDTO> response = StandardResponseDTO.success(
                "Advogado cadastrado com sucesso!", 
                logNarrativo, 
                advogadoCriado
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdvogadoResponseDTO>> listarAdvogados() {
        return ResponseEntity.ok(advogadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvogadoResponseDTO> buscarAdvogadoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(advogadoService.buscarPorId(id));
    }
}
