package tfs.lexcontrol_api.controller;

import org.springframework.http.HttpStatus;
import tfs.lexcontrol_api.dtos.StandardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.models.ContratoHonorario;
import tfs.lexcontrol_api.models.Cliente;
import tfs.lexcontrol_api.repositories.ClienteRepository;
import tfs.lexcontrol_api.services.CobrancaService;

@RestController
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
public class CobrancaController {

    private final CobrancaService cobrancaService;
    private final ClienteRepository clienteRepository;

    @PostMapping("/emitir/{clienteId}")
    public ResponseEntity<StandardResponseDTO<ContratoHonorario>> emitirContrato(
            @PathVariable Long clienteId,
            @RequestBody ContratoHonorario contrato) {
            
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                
        contrato.setCliente(cliente);
        
        ContratoHonorario gerado = cobrancaService.emitirContratoComCobrancas(contrato);
        
        String destinatario = (gerado.getTipoHonorario() == tfs.lexcontrol_api.enums.TipoHonorario.SUCUMBENCIA)
                ? gerado.getNomePagadorSucumbencia()
                : cliente.getNomeCliente();

        String logNarrativo = String.format("Motor de faturamento gerou um contrato tipo [%s] de R$ %,.2f particionado em %d fatura(s). Destinatário: %s.", 
                gerado.getTipoHonorario(), 
                gerado.getValorTotal(), 
                gerado.getFaturas().size(),
                destinatario);
                
        StandardResponseDTO<ContratoHonorario> response = StandardResponseDTO.success(
                "Cobrança emitida com sucesso!", 
                logNarrativo, 
                gerado
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
