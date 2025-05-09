package org.snascimento.cliente.dto;

import java.time.LocalDateTime;

import org.snascimento.cliente.model.Role;

public class ClienteAdminDTO {

    private Long id;
    private String nome;
    private String email;
    private Role role;
    private LocalDateTime criadoEm;

    // Construtor, getters e setters

    public ClienteAdminDTO(Long id, String nome, String email, Role role, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
