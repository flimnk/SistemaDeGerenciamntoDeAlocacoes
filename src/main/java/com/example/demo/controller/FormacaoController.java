package com.example.demo.controller;

import com.example.demo.domain.formacao.dto.FormacaoCreateRequest;
import com.example.demo.domain.formacao.dto.FormacaoResponse;
import com.example.demo.domain.formacao.dto.FormacaoUpdateRequest;
import com.example.demo.service.FormacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formacoes")

public class FormacaoController {

    private final FormacaoService formacaoService;

    public FormacaoController(FormacaoService formacaoService) {
        this.formacaoService = formacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormacaoResponse criar(@Valid @RequestBody FormacaoCreateRequest request) {
        return formacaoService.criar(request);
    }

    @GetMapping
    public List<FormacaoResponse> listar() {
        return formacaoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public FormacaoResponse buscar(@PathVariable Long id) {
        return formacaoService.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    public FormacaoResponse atualizar(@PathVariable Long id, @RequestBody FormacaoUpdateRequest request) {
        return formacaoService.atualizar( request);
    }
    @GetMapping("/minhas")
    public FormacaoResponse buscarFormacoesPeloProfessor() {
        return formacaoService.buscarMinhasFormacoes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        formacaoService.deletar(id);
    }
}
