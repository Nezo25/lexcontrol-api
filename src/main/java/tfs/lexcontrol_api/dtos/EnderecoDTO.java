package tfs.lexcontrol_api.dtos;

// DTO auxiliar para o endereço
public record EnderecoDTO(
        String logradouro, String numero, String complemento,
        String bairro, String cidade, String estado, String cep
) {}
