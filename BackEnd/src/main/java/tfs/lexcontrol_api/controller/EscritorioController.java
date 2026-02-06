package tfs.lexcontrol_api.controller;

import jakarta.validation.Valid;
import java.util.List; // Importação correta do Java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.EscritorioRequestDTO;
import tfs.lexcontrol_api.models.Escritorio;
import tfs.lexcontrol_api.services.EscritorioService;

@RestController
@RequestMapping("/escritorios")
public class EscritorioController {

    @Autowired
    private EscritorioService service;

    @PostMapping
    public ResponseEntity<Escritorio> create(@RequestBody @Valid EscritorioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<Escritorio>> getAll() {
        return ResponseEntity.ok(service.listarTodos());
    }
}