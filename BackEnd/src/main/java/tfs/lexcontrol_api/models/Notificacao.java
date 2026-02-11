package tfs.lexcontrol_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "notificacoes")
@Getter @Setter
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;

    // Altere para bater com o nome que o Repository espera
    private LocalDateTime dataNotificacao = LocalDateTime.now();

    private boolean lida = false;
    private Long clienteId;
}