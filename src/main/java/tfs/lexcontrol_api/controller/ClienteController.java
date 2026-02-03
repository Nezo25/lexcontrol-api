package tfs.lexcontrol_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.ClienteRequestDTO;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.Endereço;
import tfs.lexcontrol_api.repositories.ClienteRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody @Valid ClienteRequestDTO dto) {
        var cliente = new Cliente();

        // Mapeamento manual dos campos simples
        cliente.setNomeCliente(dto.nomeCliente());
        cliente.setCausa(dto.causa());
        cliente.setStatusPagamento(dto.statusPagamento());

        // Conversão de double (DTO) para BigDecimal (Entity)
        cliente.setValorCausa(BigDecimal.valueOf(dto.valorCausa()));

        // Mapeamento do Endereço
        if (dto.endereco() != null) {
            var enderecoModel = new Endereço();
            enderecoModel.setLogradouro(dto.endereco().logradouro());
            enderecoModel.setNumero(dto.endereco().numero());
            enderecoModel.setBairro(dto.endereco().bairro());
            enderecoModel.setCidade(dto.endereco().cidade());
            enderecoModel.setEstado(dto.endereco().estado());
            enderecoModel.setCep(dto.endereco().cep());
            enderecoModel.setComplemento(dto.endereco().complemento());

            cliente.setEndereco(enderecoModel);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(cliente));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getOne(@PathVariable Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado."));
        return ResponseEntity.ok(cliente);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossível atualizar: Cliente não encontrado."));

        BeanUtils.copyProperties(dto, cliente);
        cliente.setId(id); // Garante que o ID não mude durante a cópia

        return ResponseEntity.ok(repository.save(cliente));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossível deletar: Cliente não encontrado."));

        repository.delete(cliente);
        return ResponseEntity.ok("Cliente removido com sucesso!");
    }
}