package org.snascimento.cliente.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snascimento.cliente.dto.AuthResponse;
import org.snascimento.cliente.dto.LoginRequest;
import org.snascimento.cliente.dto.RegisterRequest;
import org.snascimento.cliente.exceptions.ClienteException;
import org.snascimento.cliente.exceptions.NotFoundException;
import org.snascimento.cliente.exceptions.UnauthorizedException;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.repository.ClienteRepository;
import org.snascimento.cliente.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final ClienteRepository clienteRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(
      ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.clienteRepository = clienteRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public AuthResponse register(RegisterRequest request) {
    logger.info("Tentativa de registro para o email: {}", request.getEmail());
    try {

      if (clienteRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new ClienteException("Email já cadastrado");
      }

      Cliente cliente = new Cliente();
      cliente.setNome(request.getNome());
      cliente.setEmail(request.getEmail());
      cliente.setSenha(passwordEncoder.encode(request.getSenha()));
      cliente.setRole(request.getRole()); // Converter para maiúsculo para evitar erros de case-sensitive

      clienteRepository.save(cliente);

      String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRole().name());
      logger.info("Usuário registrado com sucesso: {}", request.getEmail());
      return new AuthResponse(token);
    } catch (Exception e) {
      logger.error("Erro no registro para o email {}: {}", request.getEmail(), e.getMessage());
      throw e;
    }
  }

  public AuthResponse login(LoginRequest request) {
    logger.info("Tentativa de login para o email: {}", request.getEmail());
    try {
      Cliente cliente = clienteRepository
          .findByEmail(request.getEmail())
          .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

      if (!passwordEncoder.matches(request.getSenha(), cliente.getSenha())) {
        throw new UnauthorizedException("Credenciais inválidas");
      }

      String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRole().name());
      logger.info("Login bem-sucedido para: {}", request.getEmail());
      return new AuthResponse(token);
    } catch (Exception e) {
      logger.error("Falha no login para {}: {}", request.getEmail(), e.getMessage());
      throw e;
    }
  }
}
