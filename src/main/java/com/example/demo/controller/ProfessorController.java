package com.example.demo.controller;

import com.example.demo.domain.professor.dto.ProfessorCreateRequest;
import com.example.demo.domain.professor.dto.ProfessorDisponibilidadeReportResponse;
import com.example.demo.domain.professor.dto.ProfessorResponse;
import com.example.demo.domain.professor.dto.ProfessorUpdateRequest;
import com.example.demo.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import necessário
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    // 1. CADASTRO (Item 1.2: Ação exclusiva do Administrador)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorResponse> criar(
            @RequestBody @Valid ProfessorCreateRequest request
    ) {
        ProfessorResponse response = professorService.criar(request);
        // O UriComponentsBuilder é bom, mas vamos simplificar o retorno por enquanto.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. CONSULTAS GERAIS (Admin pode ver todos; Professor só vê ativos, se necessário)

    // Listar TODOS (Admin apenas)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfessorResponse>> buscarTodos() {
        List<ProfessorResponse> professores = professorService.buscarTodos();
        return ResponseEntity.ok(professores);
    }

    // Listar APENAS ATIVOS (Admin e, potencialmente, Professor para consultas)
    @GetMapping("/ativos")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')") // Admin ou Professor pode ver quem está ativo
    public ResponseEntity<List<ProfessorResponse>> buscarTodosAtivos() {
        List<ProfessorResponse> professoresAtivos = professorService.buscarTodosAtivos();
        return ResponseEntity.ok(professoresAtivos);
    }

    // 3. CONSULTA POR ID (Admin vê qualquer um; Professor vê apenas o seu)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<ProfessorResponse> buscarPorId(@PathVariable Long id) {
        ProfessorResponse professor = professorService.buscarPorId(id);
        return ResponseEntity.ok(professor);
    }



    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public ResponseEntity<ProfessorResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProfessorUpdateRequest request
    ) {
        ProfessorResponse response = professorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }




    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> ativarProfessor(@PathVariable Long id) {
        professorService.ativarProfessor(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/desativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativarProfessor(@PathVariable Long id) {
        professorService.desativarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    // Exclusão (Geralmente evitada, mas mantida para CRUD completo, exclusivo do Admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio-disponibilidade")
    public ResponseEntity<List<ProfessorDisponibilidadeReportResponse>> buscarRelatorioDisponibilidade() {
        List<ProfessorDisponibilidadeReportResponse> relatorio = professorService.buscarRelatorioDisponibilidadeInteresse();

        return ResponseEntity.ok(relatorio);

    }
}