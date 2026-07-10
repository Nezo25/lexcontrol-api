package tfs.lexcontrol_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.dtos.EscritorioRequestDTO;
import tfs.lexcontrol_api.dtos.EscritorioResponseDTO;
import tfs.lexcontrol_api.models.Endereco;
import tfs.lexcontrol_api.models.Escritorio;
import tfs.lexcontrol_api.repositories.EscritorioRepository;

import java.util.List;

@Service
public class EscritorioService {

    @Autowired
    private EscritorioRepository escritorioRepository;

    public EscritorioResponseDTO criar(EscritorioRequestDTO dto) {
        Escritorio escritorio = new Escritorio();
        escritorio.setNomeEscritorio(dto.nomeEscritorio());
        escritorio.setCnpj(dto.cnpj());
        escritorio.setRazaoSocial(dto.razaoSocial());

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(dto.endereco().logradouro());
            endereco.setNumero(dto.endereco().numero());
            endereco.setComplemento(dto.endereco().complemento());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setEstado(dto.endereco().estado());
            endereco.setCep(dto.endereco().cep());
            escritorio.setEndereco(endereco);
        }

        escritorio = escritorioRepository.save(escritorio);
        return new EscritorioResponseDTO(escritorio);
    }

    public List<EscritorioResponseDTO> listarTodos() {
        return escritorioRepository.findAll().stream()
                .map(EscritorioResponseDTO::new)
                .toList();
    }

    public EscritorioResponseDTO buscarPorId(Long id) {
        Escritorio escritorio = escritorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escritório não encontrado"));
        return new EscritorioResponseDTO(escritorio);
    }
}
