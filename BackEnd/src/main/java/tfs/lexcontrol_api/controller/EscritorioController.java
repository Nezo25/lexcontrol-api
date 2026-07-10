package tfs.lexcontrol_api.controller;

import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.EscritorioRequestDTO;
import tfs.lexcontrol_api.dtos.EscritorioResponseDTO;
import tfs.lexcontrol_api.services.EscritorioService;

import java.util.List;

@RestController
@RequestMapping("/escritorios")
public class EscritorioController {

    @Autowired
    private EscritorioService escritorioService;

    @PostMapping
    public ResponseEntity<StandardResponseDTO<EscritorioResponseDTO>> criarEscritorio(@RequestBody @Valid EscritorioRequestDTO dto) {
        EscritorioResponseDTO escritorioCriado = escritorioService.criar(dto);
        
        String logNarrativo = String.format("Unidade/Escritório [%s] (CNPJ: %s) configurado no ecossistema LexControl.", 
                escritorioCriado.nomeEscritorio(), escritorioCriado.cnpj());
                
        StandardResponseDTO<EscritorioResponseDTO> response = StandardResponseDTO.success(
                "Escritório cadastrado com sucesso!", 
                logNarrativo, 
                escritorioCriado
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EscritorioResponseDTO>> listarEscritorios() {
        return ResponseEntity.ok(escritorioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscritorioResponseDTO> buscarEscritorioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(escritorioService.buscarPorId(id));
    }
}
