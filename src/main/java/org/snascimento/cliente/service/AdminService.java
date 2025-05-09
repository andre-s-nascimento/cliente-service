package org.snascimento.cliente.service;

import java.util.List;
import java.util.stream.Collectors;
import org.snascimento.cliente.dto.ClienteAdminDTO;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
  private final ClienteRepository clienteRepository;

  public AdminService(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  public List<ClienteAdminDTO> getAllClientes() {
    List<Cliente> clientes = clienteRepository.findAll();
    return clientes.stream()
        .map(
            cliente ->
                new ClienteAdminDTO(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getRole(),
                    cliente.getCriadoEm()))
        .collect(Collectors.toList());
  }
}
