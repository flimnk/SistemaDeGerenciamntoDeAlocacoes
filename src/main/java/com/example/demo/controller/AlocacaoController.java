package com.example.demo.controller;

import com.example.demo.domain.alocacao.dto.AlocacaoCreateRequest;
import com.example.demo.domain.alocacao.dto.AlocacaoResponse;
import com.example.demo.domain.alocacao.dto.AlocacaoUpdateRequest;
import com.example.demo.domain.professor.dto.ProfessorResponse;
import com.example.demo.service.AlocacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/alocacoes")
@RequiredArgsConstructor
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    // POST /alocacoes
    @PostMapping
    public ResponseEntity<AlocacaoResponse> criar(
            @RequestBody @Valid AlocacaoCreateRequest request
          ) {

        AlocacaoResponse response = alocacaoService.criar(request);



        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /alocacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AlocacaoResponse> buscarPorId(@PathVariable Long id) {
        AlocacaoResponse response = alocacaoService.buscarPorId(id);

        // Retorna 200 OK (ou 404 se não encontrado, tratado pelo ControllerAdvice)
        return ResponseEntity.ok(response);
    }

    // GET /alocacoes
    @GetMapping
    public ResponseEntity<List<AlocacaoResponse>> buscarTodas() {
        List<AlocacaoResponse> alocacoes = alocacaoService.buscarTodas();

        // Retorna 200 OK
        return ResponseEntity.ok(alocacoes);
    }
    
    // PATCH /alocacoes/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<AlocacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AlocacaoUpdateRequest request) {

        AlocacaoResponse response = alocacaoService.atualizar(id, request);

        // Retorna 200 OK
        return ResponseEntity.ok(response);
    }

    // DELETE /alocacoes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alocacaoService.deletar(id);


        return ResponseEntity.noContent().build();
    }

}