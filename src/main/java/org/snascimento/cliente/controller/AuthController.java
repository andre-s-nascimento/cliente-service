package org.snascimento.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.snascimento.cliente.dto.AuthResponse;
import org.snascimento.cliente.dto.LoginRequest;
import org.snascimento.cliente.dto.RegisterRequest;
import org.snascimento.cliente.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth") // Requer autenticação via Bearer Token
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(
      summary = "Registrar um novo cliente",
      description =
          "Este endpoint cria um novo cliente. O cliente precisa fornecer um email e senha válidos. "
              + "Ao se registrar, o cliente recebe automaticamente um papel de 'USER'.")
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @Parameter(description = "Dados de registro do cliente (email, senha, etc.)")
          @Valid
          @RequestBody
          RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @Operation(
      summary = "Login de cliente",
      description =
          "Este endpoint permite que o cliente faça login no sistema e receba um token JWT. "
              + "O cliente precisa fornecer um email e senha válidos.")
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Parameter(description = "Credenciais do cliente (email e senha)") @Valid @RequestBody
          LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
