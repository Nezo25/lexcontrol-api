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
        
        String destinatario = (gerado.getTipoHonorario() == tfs.lexcontrol_api.enums.TipoHonorario.SUCUMBENCIA || gerado.getTipoHonorario() == tfs.lexcontrol_api.enums.TipoHonorario.EXITO)
                ? gerado.getNomePagadorSucumbencia()
                : cliente.getNomeCliente();

        String logNarrativo = String.format("Motor de faturamento processou um contrato tipo [%s] de R$ %,.2f. Destinatário: %s.", 
                gerado.getTipoHonorario(), 
                gerado.getValorTotal(), 
                destinatario);
                
        StandardResponseDTO<ContratoHonorario> response = StandardResponseDTO.success(
                "Operação realizada com sucesso!", 
                logNarrativo, 
                gerado
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/contratos/{contratoId}/vencer-causa")
    public ResponseEntity<StandardResponseDTO<ContratoHonorario>> vencerCausa(
            @PathVariable Long contratoId,
            @RequestBody tfs.lexcontrol_api.dtos.VencerCausaDTO dto) {
            
        ContratoHonorario executado = cobrancaService.executarCobrancaSucumbencia(contratoId, dto);
        
        String logNarrativo = String.format("Aviso de ganho de causa recebido! Contrato ativado no valor de R$ %,.2f. Faturas geradas para o perdedor: %s.", 
                executado.getValorTotal(), 
                executado.getNomePagadorSucumbencia());
                
        StandardResponseDTO<ContratoHonorario> response = StandardResponseDTO.success(
                "Ganho de causa registrado e cobranças emitidas!", 
                logNarrativo, 
                executado
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
