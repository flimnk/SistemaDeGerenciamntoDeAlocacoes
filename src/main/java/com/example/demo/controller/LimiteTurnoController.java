package com.example.demo.controller;

import com.example.demo.domain.limiteTurno.dto.LimiteTurnoCreateRequest;
import com.example.demo.domain.limiteTurno.dto.LimiteTurnoResponse;
import com.example.demo.domain.limiteTurno.dto.LimiteTurnoUpdateRequest;
import com.example.demo.service.LimiteTurnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import necessário
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/limites-turno") // Rota clara para ADMIN
@PreAuthorize("hasRole('ADMIN')")
public class LimiteTurnoController {

    private final LimiteTurnoService limiteTurnoService;

    public LimiteTurnoController(LimiteTurnoService limiteTurnoService) {
        this.limiteTurnoService = limiteTurnoService;
    }

    @PostMapping
    public ResponseEntity<LimiteTurnoResponse> criar(@RequestBody @Valid LimiteTurnoCreateRequest request) {
        LimiteTurnoResponse response = limiteTurnoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LimiteTurnoResponse>> buscarTodos() {
        List<LimiteTurnoResponse> limites = limiteTurnoService.buscarTodos();
        return ResponseEntity.ok(limites);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LimiteTurnoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid LimiteTurnoUpdateRequest request
    ) {
        LimiteTurnoResponse response = limiteTurnoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        limiteTurnoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}