package tfs.lexcontrol_api.enums;

public enum UsuarioRole {

    ADMIN,
    ADVOGADO,
    ASSISTENTE;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}