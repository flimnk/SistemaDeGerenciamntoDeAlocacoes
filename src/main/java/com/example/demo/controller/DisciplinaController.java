package com.example.demo.controller;


import com.example.demo.domain.disciplina.DisciplinaCreateRequest;
import com.example.demo.domain.disciplina.DisciplinaResponse;
import com.example.demo.domain.disciplina.DisciplinaUpdateRequest;
import com.example.demo.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Use javax.validation.Valid if you're on an older Jakarta EE version
import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;


    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criarDisciplina(@RequestBody @Valid DisciplinaCreateRequest disciplinaRequest) {
        DisciplinaResponse novaDisciplina = disciplinaService.criar(disciplinaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDisciplina);
    }


    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> buscarTodasDisciplinas() {
        List<DisciplinaResponse> disciplinas = disciplinaService.buscarTodas();
        return ResponseEntity.ok(disciplinas);
    }


    @GetMapping("/ativas")
    public ResponseEntity<List<DisciplinaResponse>> buscarTodasDisciplinasAtivas() {
        List<DisciplinaResponse> disciplinasAtivas = disciplinaService.buscarTodasAtivas();
        return ResponseEntity.ok(disciplinasAtivas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> buscarDisciplinaPorId(@PathVariable Long id) {
        DisciplinaResponse disciplina = disciplinaService.buscarPorId(id);

        return ResponseEntity.ok(disciplina);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable Long id) {
        disciplinaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> atualizarDisciplina(@PathVariable Long id, @RequestBody @Valid DisciplinaUpdateRequest disciplinaRequest) {
        DisciplinaResponse disciplinaAtualizada = disciplinaService.atualizar(id, disciplinaRequest);
        return ResponseEntity.ok(disciplinaAtualizada);
    }
}