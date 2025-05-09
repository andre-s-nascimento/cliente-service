package org.snascimento.cliente.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateNameRequest {

  @NotBlank(message = "O nome não pode ser vazio")
  private String nome;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
