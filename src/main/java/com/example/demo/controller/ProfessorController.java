package com.example.demo.controller;

import com.example.demo.domain.professor.dto.ProfessorCreateRequest;
import com.example.demo.domain.professor.dto.ProfessorResponse;
import com.example.demo.domain.professor.dto.ProfessorUpdateRequest;
import com.example.demo.infra.Exception.ProfessorJaExisteException;
import com.example.demo.service.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/professores")
public class ProfessorController {

    private final ProfessorService professorService;


    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(
            @RequestBody @Valid ProfessorCreateRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        ProfessorResponse response = professorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> buscarTodos() {
        List<ProfessorResponse> professores = professorService.buscarTodos();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> buscarPorId(@PathVariable Long id) {
        // O service já lança EntityNotFoundException se não encontrar
        ProfessorResponse professor = professorService.buscarPorId(id);
        return ResponseEntity.ok(professor);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProfessorResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProfessorUpdateRequest request
    ) {
        ProfessorResponse response = professorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProfessorResponse>> buscarTodosAtivos() {
        List<ProfessorResponse> professoresAtivos = professorService.buscarTodosAtivos();
        return ResponseEntity.ok(professoresAtivos);
    }


    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarProfessor(@PathVariable Long id) {
        professorService.ativarProfessor(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarProfessor(@PathVariable Long id) {
        professorService.desativarProfessor(id);
        return ResponseEntity.noContent().build();
    }

}