package tfs.lexcontrol_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tfs.lexcontrol_api.models.Usuario;
import tfs.lexcontrol_api.enums.UsuarioRole;

public final class AuthDTOs {

    private AuthDTOs() {}

    public record LoginRequest(
            @NotBlank(message = "E-mail obrigatório")
            @Email(message = "Formato de e-mail inválido")
            String email,

            @NotBlank(message = "Senha obrigatória")
            String senha
    ) {}

    public record RegisterRequest(
            @NotBlank(message = "Nome obrigatório")
            @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
            String nome,

            @NotBlank(message = "E-mail obrigatório")
            @Email(message = "Formato de e-mail inválido")
            String email,

            @NotBlank(message = "Senha obrigatória")
            @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
            String senha,

            @NotNull(message = "Role obrigatório")
            UsuarioRole role
    ) {}

    public record RefreshTokenRequest(
            @NotBlank(message = "Refresh token obrigatório")
            String refreshToken
    ) {}

    public record AuthResponse(
            String accessToken,
            String refreshToken,
            String tipo,
            Long expiracaoMs,
            UsuarioInfo usuario
    ) {
        public static AuthResponse of(String accessToken,
                                      String refreshToken,
                                      Long expiracaoMs,
                                      Usuario usuario) {
            return new AuthResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    expiracaoMs,
                    new UsuarioInfo(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole())
            );
        }

        public record UsuarioInfo(Long id, String nome, String email, UsuarioRole role) {}
    }
}