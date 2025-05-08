package org.snascimento.cliente.service;

import org.snascimento.cliente.dto.AuthResponse;
import org.snascimento.cliente.dto.LoginRequest;
import org.snascimento.cliente.dto.RegisterRequest;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.repository.ClienteRepository;
import org.snascimento.cliente.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(ClienteRepository clienteRepository,
    PasswordEncoder passwordEncoder,
    JwtUtil jwtUtil){
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request){
        if(clienteRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email já está em uso");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setSenha(passwordEncoder.encode(request.getSenha()));

        clienteRepository.save(cliente);

        String token = jwtUtil.generateToken(cliente.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request){
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(), cliente.getSenha())){
            throw new RuntimeException("Senha Inválida");
        }

        String token = jwtUtil.generateToken(cliente.getEmail());
        return new AuthResponse(token);
    }
}
