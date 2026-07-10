package tfs.lexcontrol_api.enums;

import lombok.Getter;

@Getter
public enum ModalidadeParcelamento {
    AVISTA("Pagamento integral em parcela única"),
    PARCELADO_MENSUAL("Parcelamento cronológico fixo em parcelas mensais"),
    POR_FASE_PROCESSUAL("Faturamento atrelado a marcos judiciais (Entrada, Audiência, Sentença)"),
    RECORRENTE_ASSINATURA("Modelo de mensalidade/fee mensal para assessoria jurídica contínua"),
    EXITO_FINAL("Honorários condicionados ao ganho de causa (Sucesso judicial)");

    private final String descricao;

    ModalidadeParcelamento(String descricao) {
        this.descricao = descricao;
    }
}
