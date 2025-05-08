package org.snascimento.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Nome é campo obrigatório")
    private String nome;

    @NotBlank(message = "Email é campo obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é campo obrigatório")
    @Size(min = 6, message = "A senha precisa ter pelo menos 6 caracteres")
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
