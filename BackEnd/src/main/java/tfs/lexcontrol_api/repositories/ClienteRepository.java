package tfs.lexcontrol_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfs.lexcontrol_api.models.Cliente;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    List<Cliente> findByDataDeVencimentoBefore(LocalDate hoje);

    List<Cliente> findAllByDataDeVencimentoBeforeAndStatusNot(LocalDate hoje, String atrasado);
}