package org.snascimento.cliente.controller;

import org.snascimento.cliente.dto.ClienteDTO;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.model.Role;
import org.snascimento.cliente.repository.ClienteRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/dados")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO getClienteDados(@AuthenticationPrincipal Cliente cliente) {
        // Verifica se o cliente é ADMIN
        if (cliente.getRole() != null && cliente.getRole() == Role.ADMIN) {
            // Se for ADMIN, retorna todos os dados, incluindo o role
            return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getRole(),
                    cliente.getCriadoEm());
        }

        // Se for USER ou se não tiver um role atribuído, retorna todos os dados, exceto
        // a senha e a role
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getCriadoEm());
    }
}
