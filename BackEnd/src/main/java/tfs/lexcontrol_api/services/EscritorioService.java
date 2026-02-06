package tfs.lexcontrol_api.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.dtos.EscritorioRequestDTO;
import tfs.lexcontrol_api.models.Endereco;
import tfs.lexcontrol_api.models.Escritorio;
import tfs.lexcontrol_api.repositories.EscritorioRepository;

import java.util.List;

@Service
public class EscritorioService {
    @Autowired
    private EscritorioRepository repository;

    public Escritorio salvar(EscritorioRequestDTO dto) {
        var escritorio = new Escritorio();
        BeanUtils.copyProperties(dto, escritorio);

        if (dto.endereco() != null) {
            var endereco = new Endereco();
            BeanUtils.copyProperties(dto.endereco(), endereco);
            escritorio.setEndereco(endereco);
        }
        return repository.save(escritorio);
    }

    public List<Escritorio> listarTodos() {
        return repository.findAll();
    }

    public Escritorio buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Escritório não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}