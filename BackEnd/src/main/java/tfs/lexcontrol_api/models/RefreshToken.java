package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    @Builder.Default
    private Boolean revogado = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(this.expiraEm);
    }

    public boolean isValido() {
        return !Boolean.TRUE.equals(this.revogado) && !isExpirado();
    }
}