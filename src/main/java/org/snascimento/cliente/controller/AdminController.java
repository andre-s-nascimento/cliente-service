package org.snascimento.cliente.controller;

import java.util.List;

import org.snascimento.cliente.dto.ClienteAdminDTO;
import org.snascimento.cliente.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteAdminDTO>> getAllClientes() {
        List<ClienteAdminDTO> clientes = adminService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }
}
