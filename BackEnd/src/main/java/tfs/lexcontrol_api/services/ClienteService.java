package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.repositories.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        // Regra de Negócio: Verificar se o CPF já existe antes de salvar
        if (repository.existsByCpf(cliente.getCpf())) {
            throw new tfs.lexcontrol_api.infra.exceptions.RegraNegocioException("Já existe um cliente cadastrado com este CPF.");
        }
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new tfs.lexcontrol_api.infra.exceptions.RecursoNaoEncontradoException("Cliente não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        return repository.findById(id).map(cliente -> {
            cliente.setNomeCliente(clienteAtualizado.getNomeCliente());
            cliente.setCpf(clienteAtualizado.getCpf());
            cliente.setEndereco(clienteAtualizado.getEndereco());
            return repository.save(cliente);
        }).orElseThrow(() -> new tfs.lexcontrol_api.infra.exceptions.RecursoNaoEncontradoException("Cliente não encontrado para atualização."));
    }
}