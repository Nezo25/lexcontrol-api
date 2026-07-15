package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.repositories.ControladoriaJuridicaRepository;
import tfs.lexcontrol_api.repositories.projections.InadimplenciaPorAreaProjection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ControladoriaJuridicaService {

    private final ControladoriaJuridicaRepository controladoriaRepository;

    public List<InadimplenciaPorAreaProjection> obterInadimplenciaPorArea() {
        return controladoriaRepository.calcularTaxaInadimplenciaPorArea();
    }

    public Double obterMediaAgingSucumbencia() {
        Double aging = controladoriaRepository.calcularMediaAgingSucumbencia();
        return aging != null ? aging : 0.0;
    }
}
