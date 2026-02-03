package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tfs.lexcontrol_api.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);
}