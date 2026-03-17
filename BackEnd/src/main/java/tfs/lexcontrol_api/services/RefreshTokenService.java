package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.infra.security.token.JwtProperties;
import tfs.lexcontrol_api.infra.security.token.JwtService;
import tfs.lexcontrol_api.models.RefreshToken;
import tfs.lexcontrol_api.models.Usuario;
import tfs.lexcontrol_api.repositories.RefreshTokenRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional
    public RefreshToken criar(Usuario usuario) {
        String tokenString = jwtService.gerarRefreshToken(usuario);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenString)
                .usuario(usuario)
                .expiraEm(LocalDateTime.now().plusSeconds(
                        jwtProperties.getRefreshExpirationMs() / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken validarERotacionar(String tokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token não encontrado."));

        if (!refreshToken.isValido()) {
            log.warn("[LexControl] Refresh token inválido/expirado para usuário: {}",
                    refreshToken.getUsuario().getEmail());
            throw new IllegalArgumentException("Refresh token expirado ou revogado. Faça login novamente.");
        }

        refreshToken.setRevogado(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Transactional
    public void revogarTodosPorUsuario(Usuario usuario) {
        refreshTokenRepository.revogarTodosPorUsuario(usuario);
        log.info("[LexControl] Todos os refresh tokens revogados para: {}", usuario.getEmail());
    }
}