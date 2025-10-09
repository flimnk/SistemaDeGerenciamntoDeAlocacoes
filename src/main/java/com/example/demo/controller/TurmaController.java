package com.example.demo.controller;

import com.example.demo.domain.turma.dto.TurmaCreateRequest;
import com.example.demo.domain.turma.dto.TurmaResponse;

import com.example.demo.domain.turma.dto.TurmaUpdateRequest;
import com.example.demo.service.TurmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponse>> listarTodas() {
        return ResponseEntity.ok(turmaService.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turmaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@RequestBody TurmaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.criarTurma(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TurmaResponse> atualizar(@PathVariable Long id, @RequestBody TurmaUpdateRequest request) {
        return ResponseEntity.ok(turmaService.atualizarTurma(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        turmaService.deletarTurma(id);
        return ResponseEntity.noContent().build();
    }
}
