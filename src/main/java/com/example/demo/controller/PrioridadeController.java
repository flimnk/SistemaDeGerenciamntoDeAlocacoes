package com.example.demo.controller;

import com.example.demo.domain.prioridade.dto.PrioridadeCreateRequest;
import com.example.demo.domain.prioridade.dto.PrioridadeResponse;
import com.example.demo.domain.prioridade.dto.PrioridadeUpdateRequest;
import com.example.demo.service.PrioridadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prioridades")

@RequiredArgsConstructor
public class PrioridadeController {

    private final PrioridadeService prioridadeService;


    @PostMapping
    @Transactional
    public ResponseEntity<PrioridadeResponse> criar(
            @RequestBody @Valid PrioridadeCreateRequest request) {
        PrioridadeResponse response = prioridadeService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PrioridadeResponse>> buscarTodos() {
        List<PrioridadeResponse> prioridades = prioridadeService.buscarTodos();
        

        return ResponseEntity.ok(prioridades);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PrioridadeResponse> buscarPorId(@PathVariable Long id) {
        PrioridadeResponse response = prioridadeService.buscarPorId(id);
        

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<PrioridadeResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PrioridadeUpdateRequest request) {
        PrioridadeResponse response = prioridadeService.atualizar(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prioridadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}