package com.example.demo.controller;

import com.example.demo.domain.curso.dto.CursoCreateRequest;
import com.example.demo.domain.curso.dto.CursoResponse;
import com.example.demo.domain.curso.dto.CursoUpdateRequest;
import com.example.demo.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("admin/api/cursos")
@PreAuthorize("hasRole('ADMIN')")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }


    @PostMapping
    public ResponseEntity<CursoResponse> create(
            @RequestBody @Valid CursoCreateRequest request

    ) {
        CursoResponse response = cursoService.criar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<Page<CursoResponse>> buscarTodos(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable pageable
    ) {
        Page<CursoResponse> page = cursoService.buscarTodos(pageable);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> buscarPorId(@PathVariable Long id) {
        CursoResponse response = cursoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<CursoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CursoUpdateRequest request
    ) {
        CursoResponse response = cursoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}