package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.Size;

// DTO auxiliar para o endereço
public record EnderecoDTO(
        @Size(max = 150) String logradouro, 
        @Size(max = 20) String numero, 
        @Size(max = 100) String complemento,
        @Size(max = 100) String bairro, 
        @Size(max = 100) String cidade, 
        @Size(max = 2) String estado, 
        @Size(max = 9) String cep
) {}
