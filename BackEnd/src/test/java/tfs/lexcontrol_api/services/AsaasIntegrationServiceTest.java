package tfs.lexcontrol_api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tfs.lexcontrol_api.enums.StatusFatura;
import tfs.lexcontrol_api.models.Fatura;
import tfs.lexcontrol_api.repositories.FaturaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AsaasIntegrationServiceTest {

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private RateioService rateioService;

    private ObjectMapper objectMapper = new ObjectMapper(); // Não mockamos o parser de JSON

    private AsaasIntegrationService asaasIntegrationService;

    @BeforeEach
    void setUp() {
        asaasIntegrationService = new AsaasIntegrationService("http://fake-api", "fake-key", faturaRepository, objectMapper, rateioService);
    }

    @Test
    void deveAtualizarFaturaParaPagaAoReceberWebhook() {
        // Arrange
        String jsonWebhook = """
            {
              "event": "PAYMENT_RECEIVED",
              "payment": {
                "externalReference": "100"
              }
            }
        """;

        Fatura faturaMock = new Fatura();
        faturaMock.setId(100L);
        faturaMock.setStatus(StatusFatura.PENDENTE);

        when(faturaRepository.findById(100L)).thenReturn(Optional.of(faturaMock));

        // Act
        asaasIntegrationService.processarWebhook(jsonWebhook);

        // Assert
        ArgumentCaptor<Fatura> faturaCaptor = ArgumentCaptor.forClass(Fatura.class);
        verify(faturaRepository).save(faturaCaptor.capture());

        Fatura faturaSalva = faturaCaptor.getValue();
        assertEquals(StatusFatura.PAGA, faturaSalva.getStatus(), "A fatura deveria ter o status PAGA");
    }

    @Test
    void naoDeveFazerNadaSeExternalReferenceNaoExistirNoJson() {
        // Arrange
        String jsonWebhook = """
            {
              "event": "PAYMENT_RECEIVED",
              "payment": {
                "id": "pay_123"
              }
            }
        """;

        // Act
        asaasIntegrationService.processarWebhook(jsonWebhook);

        // Assert
        verify(faturaRepository, never()).findById(anyLong());
        verify(faturaRepository, never()).save(any());
    }
}
