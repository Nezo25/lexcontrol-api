package tfs.lexcontrol_api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tfs.lexcontrol_api.infra.exceptions.RegraNegocioException;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.repositories.ClienteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void naoDeveSalvarClienteSeCpfJaExiste() {
        // Arrange (Preparar)
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678900");

        when(clienteRepository.existsByCpf("12345678900")).thenReturn(true);

        // Act & Assert (Agir e Afirmar)
        assertThrows(RegraNegocioException.class, () -> clienteService.salvar(cliente));
        
        // Verifica se o método save NUNCA foi chamado
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveSalvarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678900");
        cliente.setNomeCliente("João Teste");

        when(clienteRepository.existsByCpf("12345678900")).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente salvo = clienteService.salvar(cliente);

        // Assert
        assertNotNull(salvo);
        assertEquals("João Teste", salvo.getNomeCliente());
        verify(clienteRepository, times(1)).save(cliente);
    }
}
