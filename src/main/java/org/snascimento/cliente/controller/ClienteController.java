package org.snascimento.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.snascimento.cliente.dto.ClienteDTO;
import org.snascimento.cliente.dto.UpdateNameRequest;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.model.Role;
import org.snascimento.cliente.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
@SecurityRequirement(name = "bearerAuth") // Requer autenticação via Bearer Token
public class ClienteController {

  private final ClienteRepository clienteRepository;

  public ClienteController(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  @Operation(
      summary = "Retorna os dados do cliente autenticado",
      description =
          "Este endpoint retorna os dados do cliente autenticado. Para usuários do tipo 'ADMIN', retorna todos os dados, incluindo o 'role'. Para usuários 'USER', retorna todos os dados exceto a senha e o 'role'.")
  @GetMapping("/dados")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ClienteDTO getClienteDados(
      @Parameter(description = "Cliente autenticado", required = true) @AuthenticationPrincipal
          Cliente cliente) {
    // Verifica se o cliente é ADMIN
    if (cliente.getRole() != null && cliente.getRole() == Role.ADMIN) {
      // Se for ADMIN, retorna todos os dados, incluindo o role
      return new ClienteDTO(
          cliente.getId(),
          cliente.getNome(),
          cliente.getEmail(),
          cliente.getRole(),
          cliente.getCriadoEm());
    }

    // Se for USER ou se não tiver um role atribuído, retorna todos os dados, exceto
    // a senha e a role
    return new ClienteDTO(
        cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getCriadoEm());
  }

  // Novo endpoint para atualizar o nome
  @PutMapping("/nome")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(
      summary = "Altera os dados do cliente autenticado",
      description = "Este endpoint altera os dados do cliente autenticado.")
  public ResponseEntity<ClienteDTO> updateNome(
      @AuthenticationPrincipal Cliente cliente,
      @Valid @RequestBody UpdateNameRequest updateNameRequest) {
    cliente.setNome(updateNameRequest.getNome());
    clienteRepository.save(cliente);

    // Retorna os dados atualizados, sem a senha
    ClienteDTO clienteDTO =
        new ClienteDTO(
            cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getCriadoEm());
    return ResponseEntity.ok(clienteDTO);
  }
}
