package tfs.lexcontrol_api.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tfs.lexcontrol_api.models.Usuario;
import tfs.lexcontrol_api.repositories.UsuarioRepository;
import tfs.lexcontrol_api.enums.UsuarioRole;
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o admin já existe para não duplicar toda vez que ligar a API
        if (usuarioRepository.findByEmail("admin@lexcontrol.com.br").isEmpty()) {

            Usuario admin = new Usuario();
            admin.setNome("Administrador LexControl");
            admin.setEmail("admin@lexcontrol.com.br");

            // Aqui o Java usa o seu BCrypt(12) para gerar o hash seguro
            admin.setSenha(passwordEncoder.encode("admin123"));

            // Certifique-se de que o seu model Usuario tenha esse campo
            admin.setRole(UsuarioRole.ADMIN);

            usuarioRepository.save(admin);

            System.out.println("-----------------------------------------");
            System.out.println("Usuário Admin criado com sucesso!");
            System.out.println("E-mail: admin@lexcontrol.com.br");
            System.out.println("Senha: admin123");
            System.out.println("-----------------------------------------");
        }
    }
}