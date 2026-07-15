package tfs.lexcontrol_api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.controller.ControladoriaJuridicaController;
import tfs.lexcontrol_api.controller.TribunalWebhookController;
import tfs.lexcontrol_api.controller.webhook.AsaasWebhookController;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import tfs.lexcontrol_api.enums.*;
import tfs.lexcontrol_api.models.*;
import tfs.lexcontrol_api.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@SpringBootTest(properties = {
        "asaas.webhook.token=test-token",
        "asaas.api.key=test-key"
})
public class EcosystemIntegrationTest {

    @Autowired private TribunalWebhookController tribunalController;
    @Autowired private AsaasWebhookController asaasController;
    @Autowired private ControladoriaJuridicaController biController;
    
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AdvogadoRepository advogadoRepository;
    @Autowired private ContratoHonorarioRepository contratoRepository;
    @Autowired private ProcessoRepository processoRepository;
    @Autowired private FaturaRepository faturaRepository;
    @Autowired private RegraRateioRepository regraRateioRepository;
    @Autowired private ExtratoComissaoRepository extratoComissaoRepository;

    private ContratoHonorario contrato;
    private Fatura fatura;
    private Advogado advogado;

    @AfterEach
    void tearDown() {
        extratoComissaoRepository.deleteAll();
        regraRateioRepository.deleteAll();
        faturaRepository.deleteAll();
        processoRepository.deleteAll();
        contratoRepository.deleteAll();
        advogadoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Empresa Teste SA");
        cliente.setCpf("12345678909");
        cliente.setRg("1234567");
        cliente.setTelefone("11999999999");
        cliente = clienteRepository.save(cliente);

        advogado = new Advogado();
        advogado.setOab("12345/SP");
        advogado.setCpf("98765432100");
        advogado.setEspecialidade(AreaJuridica.CIVIL);
        advogado = advogadoRepository.save(advogado);

        contrato = new ContratoHonorario();
        contrato.setCliente(cliente);
        contrato.setValorTotal(BigDecimal.valueOf(10000.00));
        contrato.setTipoHonorario(TipoHonorario.SUCUMBENCIA);
        contrato.setStatus(StatusContrato.EM_ANDAMENTO);
        contrato.setDescricaoCobranca("Honorarios");
        contrato.setDataPrimeiroVencimento(LocalDate.now());
        contrato.setTotalParcelas(TotalParcelas.X1);
        contrato.setModalidadeParcelamento(ModalidadeParcelamento.AVISTA);
        contrato.setMeioPagamentoPadrao(MeioPagamento.BOLETO);
        contrato = contratoRepository.save(contrato);

        Processo processo = new Processo();
        processo.setCnj("0000000-00.0000.0.00.0000");
        processo.setAreaDireito("Cível");
        processo.setContrato(contrato);
        processoRepository.save(processo);

        fatura = new Fatura();
        fatura.setContrato(contrato);
        fatura.setValor(BigDecimal.valueOf(10000.00));
        fatura.setDataVencimento(LocalDate.now().plusDays(5));
        fatura.setStatus(StatusFatura.PENDENTE);
        fatura.setNumeroParcela("1");
        fatura.setMeioPagamento(MeioPagamento.PIX);
        fatura = faturaRepository.save(fatura);

        RegraRateio regra = new RegraRateio();
        regra.setContrato(contrato);
        regra.setAdvogado(advogado);
        regra.setPercentualComissao(BigDecimal.valueOf(15.00));
        regraRateioRepository.save(regra);
    }

    @Test
    void testarFluxoTribunalParaSucumbencia() throws Exception {
        Map<String, String> payload = Map.of(
                "numeroProcesso", "0000000-00.0000.0.00.0000",
                "tipoAndamento", "PROCEDENTE"
        );

        ResponseEntity<StandardResponseDTO<String>> response = tribunalController.receberPushTribunal(payload);
        assert response.getStatusCode().is2xxSuccessful();

        Thread.sleep(1000); // Aguardar o Async do EventListener

        ContratoHonorario updatedContrato = contratoRepository.findById(contrato.getId()).orElseThrow();
        assert updatedContrato.getStatus() == StatusContrato.AGUARDANDO_EMISSAO_SUCUMBENCIA;
    }

    @Test
    void testarFluxoAsaasSplitRateio() throws Exception {
        String paymentPayload = """
                {
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "externalReference": "%s"
                  }
                }
                """.formatted(fatura.getId());

        ResponseEntity<StandardResponseDTO<Void>> response = asaasController.receberNotificacao("test-token", paymentPayload);
        assert response.getStatusCode().is2xxSuccessful();

        Fatura updatedFatura = faturaRepository.findById(fatura.getId()).orElseThrow();
        assert updatedFatura.getStatus() == StatusFatura.PAGA;

        List<ExtratoComissao> extratos = extratoComissaoRepository.findAll();
        assert !extratos.isEmpty();
        assert extratos.get(0).getValorReceber().compareTo(BigDecimal.valueOf(1500.00)) == 0;
    }

    @Test
    void testarEndpointControladoriaJuridica() throws Exception {
        fatura.setStatus(StatusFatura.PAGA);
        faturaRepository.save(fatura);

        var responseAging = biController.getAgingSucumbencia();
        assert responseAging.getStatusCode().is2xxSuccessful();

        var responseInadimplencia = biController.getInadimplenciaPorArea();
        assert responseInadimplencia.getStatusCode().is2xxSuccessful();
    }
}
