package com.example.demo.controller;

import com.example.demo.domain.Matriz.dto.MatrizCreateRequest;
import com.example.demo.domain.Matriz.dto.MatrizUpdateRequest;
import com.example.demo.domain.Matriz.dto.MatrizResponse;

import com.example.demo.service.MatrizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("admin/api/matrizes")
@PreAuthorize("hasRole('ADMIN')")
public class MatrizController {

    private final MatrizService matrizService;

    public MatrizController(MatrizService matrizService) {
        this.matrizService = matrizService;
    }

    @PostMapping
    public ResponseEntity<MatrizResponse> criar(@RequestBody @Valid MatrizCreateRequest request) {
        MatrizResponse response = matrizService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MatrizResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MatrizUpdateRequest request
    ) {
        MatrizResponse response = matrizService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatrizResponse> buscarPorId(@PathVariable Long id) {
        MatrizResponse response = matrizService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MatrizResponse>> buscarTodas() {
        List<MatrizResponse> matrizes = matrizService.buscarTodas();
        return ResponseEntity.ok(matrizes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        matrizService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
