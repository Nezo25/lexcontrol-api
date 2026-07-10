package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.dtos.AdvogadoRequestDTO;
import tfs.lexcontrol_api.dtos.AdvogadoResponseDTO;
import tfs.lexcontrol_api.models.Advogado;
import tfs.lexcontrol_api.models.Endereco;
import tfs.lexcontrol_api.models.Escritorio;
import tfs.lexcontrol_api.repositories.AdvogadoRepository;
import tfs.lexcontrol_api.repositories.EscritorioRepository;

import java.util.List;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private EscritorioRepository escritorioRepository;

    public AdvogadoResponseDTO criar(AdvogadoRequestDTO dto) {
        if (advogadoRepository.findByOab(dto.oab()).isPresent()) {
            throw new RuntimeException("Advogado com esta OAB já cadastrado");
        }
        if (advogadoRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new RuntimeException("Advogado com este CPF já cadastrado");
        }

        Advogado advogado = new Advogado();
        advogado.setOab(dto.oab());
        advogado.setCpf(dto.cpf());
        advogado.setCnpj(dto.cnpj());
        advogado.setEspecialidade(dto.especialidade());
        
        if (dto.escritorioId() != null) {
            Escritorio escritorio = escritorioRepository.findById(dto.escritorioId())
                    .orElseThrow(() -> new RuntimeException("Escritório não encontrado"));
            advogado.setEscritorio(escritorio);
        }

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(dto.endereco().logradouro());
            endereco.setNumero(dto.endereco().numero());
            endereco.setComplemento(dto.endereco().complemento());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setEstado(dto.endereco().estado());
            endereco.setCep(dto.endereco().cep());
            advogado.setEndereco(endereco);
        }

        advogado = advogadoRepository.save(advogado);
        return new AdvogadoResponseDTO(advogado);
    }

    public List<AdvogadoResponseDTO> listarTodos() {
        return advogadoRepository.findAll().stream()
                .map(AdvogadoResponseDTO::new)
                .toList();
    }

    public AdvogadoResponseDTO buscarPorId(Long id) {
        Advogado advogado = advogadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advogado não encontrado"));
        return new AdvogadoResponseDTO(advogado);
    }
}
