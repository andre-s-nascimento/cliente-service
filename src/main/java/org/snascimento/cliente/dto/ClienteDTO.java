package org.snascimento.cliente.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import org.snascimento.cliente.model.Role;

public class ClienteDTO {

  private Long id;
  private String nome;
  private String email;

  @JsonInclude(Include.NON_NULL)
  private Role role;

  private LocalDateTime criadoEm;

  public ClienteDTO(Long id, String nome, String email, LocalDateTime criadoEm) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.criadoEm = criadoEm;
  }

  public ClienteDTO(Long id, String nome, String email, Role role, LocalDateTime criadoEm) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.role = role;
    this.criadoEm = criadoEm;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
