package com.example.demo.controller;

import com.example.demo.domain.escola.dto.EscolaCreateRequest;
import com.example.demo.domain.escola.dto.EscolaResponse;
import com.example.demo.domain.escola.dto.EscolaUpdateRequest;
import com.example.demo.service.EscolaService;
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
import java.util.List;

@RestController
@RequestMapping("admin/api/escolas")
@PreAuthorize("hasRole('ADMIN')")
public class EscolaController {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }


    @PostMapping
    public ResponseEntity<EscolaResponse> criar(
            @RequestBody @Valid EscolaCreateRequest request

    ) {
        EscolaResponse response = escolaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<Page<EscolaResponse>> buscarTodos(
            @PageableDefault(size = 10, sort = {"categoriaEscola"}) Pageable pageable
    ) {
        Page<EscolaResponse> page = escolaService.buscarTodos(pageable);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EscolaResponse>  buscarTodos(@PathVariable Long id) {
        EscolaResponse response = escolaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<EscolaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EscolaUpdateRequest request
    ) {
        EscolaResponse response = escolaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        escolaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<EscolaResponse>> buscarAtivas() {
        List<EscolaResponse> escolas = escolaService.buscarAtivas();
        return ResponseEntity.ok(escolas);
    }


    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        escolaService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Desativar
    @PutMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        escolaService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}