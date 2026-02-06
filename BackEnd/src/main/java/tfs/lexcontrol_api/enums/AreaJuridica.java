package tfs.lexcontrol_api.enums;

import lombok.Getter;

@Getter
public enum AreaJuridica {
    TRABALHISTA("Trabalhista"),
    CIVIL("Cível"),
    PENAL("Penal/Criminal"),
    TRIBUTARIO("Tributário"),
    FAMILIA("Família e Sucessões"),
    PREVIDENCIARIO("Previdenciário"),
    CONSUMIDOR("Direito do Consumidor"),
    EMPRESARIAL("Empresarial"),
    ADMINISTRATIVO("Administrativo");

    private final String descricao;

    AreaJuridica(String descricao) {
        this.descricao = descricao;
    }
}