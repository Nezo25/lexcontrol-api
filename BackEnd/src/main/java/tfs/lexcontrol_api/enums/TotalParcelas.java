package tfs.lexcontrol_api.enums;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Getter
public enum TotalParcelas {
    X1(1), X2(2), X3(3), X4(4), X5(5), X6(6),
    X7(7), X8(8), X9(9), X10(10), X11(11), X12(12),
    X13(13), X14(14), X15(15), X16(16), X17(17), X18(18),
    X19(19), X20(20), X21(21), X22(22), X23(23), X24(24),
    X25(25), X26(26), X27(27), X28(28), X29(29), X30(30),
    X31(31), X32(32), X33(33), X34(34), X35(35), X36(36);

    @JsonValue
    private final int quantidade;

    TotalParcelas(int quantidade) {
        this.quantidade = quantidade;
    }

    @JsonCreator
    public static TotalParcelas fromValue(int value) {
        for (TotalParcelas tp : values()) {
            if (tp.quantidade == value) {
                return tp;
            }
        }
        throw new IllegalArgumentException("Número de parcelas inválido. Deve ser entre 1 e 36. Você enviou: " + value);
    }
}
