package tfs.lexcontrol_api.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.AdvogadoRequestDTO;
import tfs.lexcontrol_api.models.Advogado;
import tfs.lexcontrol_api.services.AdvogadoService;

import java.util.List;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {
    @Autowired
    private AdvogadoService service;

    @PostMapping
    public ResponseEntity<Advogado> create(@RequestBody @Valid AdvogadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<Advogado>> getAll() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
