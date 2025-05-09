package org.snascimento.cliente.controller;

import java.util.List;
import org.snascimento.cliente.dto.ClienteAdminDTO;
import org.snascimento.cliente.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth") // Requer autenticação via Bearer Token
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Lista todos os clientes cadastrados", description = "Este endpoint lista todos os clientes cadastrados no sistema. Apenas usuários com o papel 'ADMIN' podem acessar essa informação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado, necessário papel 'ADMIN'")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteAdminDTO>> getAllClientes(
            @Parameter(description = "Bearer Token necessário para autenticação", required = true) @RequestHeader("Authorization") String authorizationHeader) {
        List<ClienteAdminDTO> clientes = adminService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }
}
