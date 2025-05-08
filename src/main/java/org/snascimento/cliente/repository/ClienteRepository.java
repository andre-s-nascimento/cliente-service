package org.snascimento.cliente.repository;

import java.util.Optional;

import org.snascimento.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
