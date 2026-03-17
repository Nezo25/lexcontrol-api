package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.RefreshToken;
import tfs.lexcontrol_api.models.Usuario;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revogado = true WHERE rt.usuario = :usuario AND rt.revogado = false")
    void revogarTodosPorUsuario(Usuario usuario);
}