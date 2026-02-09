package tfs.lexcontrol_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.dtos.ClienteRequestDTO;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.models.Endereco;
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

        // 1. Mapeamento de campos obrigatórios (Evita o erro de CPF null)
        cliente.setNomeCliente(dto.nomeCliente());
        cliente.setCpf(dto.cpf());
        cliente.setRg(dto.rg());
        cliente.setDataDeVencimento(dto.dataDeVencimento());
        cliente.setTelefone(dto.telefone());

        // 2. Mapeamento de campos da causa e honorários
        cliente.setCausa(dto.causa());
        cliente.setStatusPagamento(dto.statusPagamento());
        cliente.setModeloDePagamento(dto.modeloDePagamento());

        // Conversões seguras de tipos numéricos
        cliente.setValorCausa(BigDecimal.valueOf(dto.valorCausa()));
        cliente.setValorParcela(BigDecimal.valueOf(dto.valorParcela()));
        cliente.setTotalHonorarios(BigDecimal.valueOf(dto.totalHonorarios()));

        // 3. Mapeamento do Endereço (@Embedded)
        if (dto.endereco() != null) {
            var enderecoModel = new Endereco();
            // Usando BeanUtils para simplificar o mapeamento do endereço
            BeanUtils.copyProperties(dto.endereco(), enderecoModel);
            cliente.setEndereco(enderecoModel);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(cliente));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getOne(@PathVariable Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado."));
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossível atualizar: Cliente não encontrado."));


        cliente.setNomeCliente(dto.nomeCliente());
        cliente.setCpf(dto.cpf());
        cliente.setRg(dto.rg());
        cliente.setDataDeVencimento(dto.dataDeVencimento());
        cliente.setCausa(dto.causa());
        cliente.setStatusPagamento(dto.statusPagamento());
        cliente.setValorCausa(BigDecimal.valueOf(dto.valorCausa()));

        if (dto.endereco() != null) {
            var enderecoModel = new Endereco();
            BeanUtils.copyProperties(dto.endereco(), enderecoModel);
            cliente.setEndereco(enderecoModel);
        }

        return ResponseEntity.ok(repository.save(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossível deletar: Cliente não encontrado."));

        repository.delete(cliente);
        return ResponseEntity.ok("Cliente removido com sucesso!");
    }

}