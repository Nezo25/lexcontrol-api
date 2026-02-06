package tfs.lexcontrol_api.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.dtos.AdvogadoRequestDTO;
import tfs.lexcontrol_api.models.Advogado;
import tfs.lexcontrol_api.models.Endereco;
import tfs.lexcontrol_api.repositories.AdvogadoRepository;
import tfs.lexcontrol_api.repositories.EscritorioRepository;

import java.util.List;

@Service
public class AdvogadoService {
    @Autowired
    private AdvogadoRepository repository;
    @Autowired
    private EscritorioRepository escritorioRepository;

    public Advogado salvar(AdvogadoRequestDTO dto) {
        var advogado = new Advogado();
        BeanUtils.copyProperties(dto, advogado);
        advogado.setCnpj(dto.cnpj()); // Mapeamento manual para sua variável 'cpnj'

        // Regra de Negócio: Buscar e associar escritório
        var escritorio = escritorioRepository.findById(dto.escritorioId())
                .orElseThrow(() -> new RuntimeException("Escritório ID " + dto.escritorioId() + " não existe."));
        advogado.setEscritorio(escritorio);

        if (dto.endereco() != null) {
            var endereco = new Endereco();
            BeanUtils.copyProperties(dto.endereco(), endereco);
            advogado.setEndereco(endereco);
        }
        return repository.save(advogado);
    }

    public List<Advogado> listarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}