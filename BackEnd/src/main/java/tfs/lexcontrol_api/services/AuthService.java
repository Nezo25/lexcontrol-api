package tfs.lexcontrol_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfs.lexcontrol_api.dtos.AuthDTOs.*;
import tfs.lexcontrol_api.infra.security.token.JwtService;
import tfs.lexcontrol_api.models.RefreshToken;
import tfs.lexcontrol_api.models.Usuario;
import tfs.lexcontrol_api.repositories.UsuarioRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + request.email());
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(request.role())
                .ativo(true)
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("[LexControl] Novo usuário registrado: {} ({})", usuario.getEmail(), usuario.getRole());

        return emitirTokens(usuario);
    }

    @Transactional
    public AuthResponse autenticar(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );
        } catch (AuthenticationException ex) {
            log.warn("[LexControl] Falha de autenticação para: {}", request.email());
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        log.info("[LexControl] Login efetuado: {}", usuario.getEmail());
        return emitirTokens(usuario);
    }

    @Transactional
    public AuthResponse renovarToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validarERotacionar(request.refreshToken());
        Usuario usuario = refreshToken.getUsuario();

        log.debug("[LexControl] Token renovado para: {}", usuario.getEmail());
        return emitirTokens(usuario);
    }

    @Transactional
    public void logout(Usuario usuario) {
        refreshTokenService.revogarTodosPorUsuario(usuario);
        log.info("[LexControl] Logout efetuado: {}", usuario.getEmail());
    }

    private AuthResponse emitirTokens(Usuario usuario) {
        String accessToken = jwtService.gerarAccessToken(usuario);
        RefreshToken refreshToken = refreshTokenService.criar(usuario);

        return AuthResponse.of(
                accessToken,
                refreshToken.getToken(),
                jwtService.getExpiracaoMs(),
                usuario
        );
    }
}