package tfs.lexcontrol_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tfs.lexcontrol_api.models.Notificacao;
import tfs.lexcontrol_api.repositories.NotificacaoRepository;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository repository;

    // Esse é o método que vai tirar o erro 404 do Insomnia
    @GetMapping
    public List<Notificacao> listarTodas() {
        return repository.findAll();
    }

    // Endpoint extra para o seu "Sininho" no React depois: buscar só as não lidas
    @GetMapping("/novas")
    public List<Notificacao> listarNaoLidas() {
        return repository.findByLidaFalse();
    }

    // Marcar como lida para sumir do alerta
    @PutMapping("/{id}/lida")
    public void marcarComoLida(@PathVariable Long id) {
        repository.findById(id).ifPresent(n -> {
            n.setLida(true);
            repository.save(n);
        });
    }
}