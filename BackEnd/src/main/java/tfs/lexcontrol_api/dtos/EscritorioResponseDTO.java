package tfs.lexcontrol_api.dtos;

import tfs.lexcontrol_api.models.Escritorio;

public record EscritorioResponseDTO(
        Long id,
        String nomeEscritorio,
        String cnpj,
        String razaoSocial,
        EnderecoDTO endereco
) {
    public EscritorioResponseDTO(Escritorio escritorio) {
        this(
                escritorio.getId(),
                escritorio.getNomeEscritorio(),
                escritorio.getCnpj(),
                escritorio.getRazaoSocial(),
                escritorio.getEndereco() != null ? new EnderecoDTO(
                        escritorio.getEndereco().getLogradouro(),
                        escritorio.getEndereco().getNumero(),
                        escritorio.getEndereco().getComplemento(),
                        escritorio.getEndereco().getBairro(),
                        escritorio.getEndereco().getCidade(),
                        escritorio.getEndereco().getEstado(),
                        escritorio.getEndereco().getCep()
                ) : null
        );
    }
}
