package tfs.lexcontrol_api.infra.security.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tfs.lexcontrol_api.models.Usuario;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String gerarAccessToken(Usuario usuario) {
        Map<String, Object> claims = Map.of(
                "role", usuario.getRole().name(),
                "nome", usuario.getNome()
        );
        return construirToken(claims, usuario.getEmail(), jwtProperties.getExpirationMs());
    }

    public String gerarRefreshToken(Usuario usuario) {
        return construirToken(Map.of(), usuario.getEmail(), jwtProperties.getRefreshExpirationMs());
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        try {
            final String subject = extrairSubject(token);
            return subject.equals(userDetails.getUsername()) && !isTokenExpirado(token);
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("[LexControl JWT] Token inválido: {}", ex.getMessage());
            return false;
        }
    }

    public boolean isTokenEstruturalmenteValido(String token) {
        try {
            parsearClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("[LexControl JWT] Falha estrutural no token: {}", ex.getMessage());
            return false;
        }
    }

    public String extrairSubject(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public Date extrairExpiracao(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    public <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parsearClaims(token));
    }

    public long getExpiracaoMs() {
        return jwtProperties.getExpirationMs();
    }

    private boolean isTokenExpirado(String token) {
        return extrairExpiracao(token).before(Date.from(Instant.now()));
    }

    private String construirToken(Map<String, Object> claimsExtras, String subject, long expiracaoMs) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .claims(claimsExtras)
                .subject(subject)
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusMillis(expiracaoMs)))
                .issuer("lexcontrol")
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                Base64.getEncoder().encodeToString(
                        jwtProperties.getSecretKey().getBytes()
                )
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }
}