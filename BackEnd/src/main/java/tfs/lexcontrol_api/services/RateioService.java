package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.models.ExtratoComissao;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.models.RegraRateio;
import tfs.lexcontrol_api.repositories.ExtratoComissaoRepository;
import tfs.lexcontrol_api.repositories.RegraRateioRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateioService {

    private final RegraRateioRepository regraRateioRepository;
    private final ExtratoComissaoRepository extratoComissaoRepository;

    @Transactional
    public void processarRateioDaFatura(Fatura fatura) {
        log.info("Processando rateio para Fatura ID: {}", fatura.getId());
        
        List<RegraRateio> regras = regraRateioRepository.findByContrato(fatura.getContrato());
        if (regras.isEmpty()) {
            log.info("Nenhuma regra de rateio encontrada para o Contrato ID: {}", fatura.getContrato().getId());
            return;
        }

        for (RegraRateio regra : regras) {
            BigDecimal comissaoCalculada = fatura.getValor()
                    .multiply(regra.getPercentualComissao())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);

            ExtratoComissao extrato = new ExtratoComissao();
            extrato.setFatura(fatura);
            extrato.setAdvogado(regra.getAdvogado());
            extrato.setValorReceber(comissaoCalculada);
            extrato.setStatusPagamento("PENDENTE");

            extratoComissaoRepository.save(extrato);
            log.info("Rateio gerado: Advogado ID {} recebe R$ {} (Fatura ID {})", 
                    regra.getAdvogado().getIdAdvogado(), comissaoCalculada, fatura.getId());
        }
    }
}
