package com.example.demo.controller;

import com.example.demo.domain.matriz_disciplina.dto.MatrizDisciplinaCreateRequest;
import com.example.demo.domain.matriz_disciplina.dto.MatrizDisciplinaResponse;
import com.example.demo.domain.matriz_disciplina.dto.MatrizDisciplinaUpdateRequest;
import com.example.demo.service.MatrizDisciplinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/matriz-disciplinas")
@RequiredArgsConstructor
public class MatrizDisciplinaController {

    private final MatrizDisciplinaService service;

    @PostMapping
    public ResponseEntity<MatrizDisciplinaResponse> criar(@RequestBody @Valid MatrizDisciplinaCreateRequest request) {
        MatrizDisciplinaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatrizDisciplinaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MatrizDisciplinaUpdateRequest request
    ) {
        MatrizDisciplinaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatrizDisciplinaResponse> buscarPorId(@PathVariable Long id) {
        MatrizDisciplinaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MatrizDisciplinaResponse>> listarTodas() {
        List<MatrizDisciplinaResponse> lista = service.buscarTodos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
