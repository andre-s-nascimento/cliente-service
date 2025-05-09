package org.snascimento.cliente.security;

import java.io.IOException;
import java.util.Collections;

import org.snascimento.cliente.model.Cliente;
import org.snascimento.cliente.repository.ClienteRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ClienteRepository clienteRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, ClienteRepository clienteRepository) {
        this.jwtUtil = jwtUtil;
        this.clienteRepository = clienteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isValid(token)) {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                Cliente cliente = clienteRepository.findByEmail(email).orElse(null);
                if (cliente != null) {
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            cliente, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
