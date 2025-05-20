package org.snascimento.cliente.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.repository.ClienteRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  private final JwtUtil jwtUtil;
  private final ClienteRepository clienteRepository;

  public JwtAuthFilter(JwtUtil jwtUtil, ClienteRepository clienteRepository) {
    this.jwtUtil = jwtUtil;
    this.clienteRepository = clienteRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      logger.debug("Processando token JWT para autenticação");

      try {

        if (jwtUtil.isValid(token)) {
          String email = jwtUtil.extractEmail(token);
          String role = jwtUtil.extractRole(token);

          Cliente cliente = clienteRepository.findByEmail(email).orElse(null);
          if (cliente != null) {
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(cliente, null,
                authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
          }
          logger.info("Autenticação bem-sucedida para o usuário: {}", email);
        }
      } catch (JwtException e) {
        logger.warn("Token JWT inválido: {}", e.getMessage());
        throw e;
      }
    }

    filterChain.doFilter(request, response);
  }
}
