package com.example.demo.controller;

import com.example.demo.domain.professorHorario.dto.ProfessorHorarioCreateRequest;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioResponse;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioUpdateRequest;
import com.example.demo.service.ProfessorHorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/professor-horarios")
@RequiredArgsConstructor
public class ProfessorHorarioController {

    private final ProfessorHorarioService phService;

    // POST /professor-horarios
    @PostMapping
    public ResponseEntity<ProfessorHorarioResponse> criar(
            @RequestBody @Valid ProfessorHorarioCreateRequest request
          ) {

        ProfessorHorarioResponse response = phService.criar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /professor-horarios
    @GetMapping
    public ResponseEntity<List<ProfessorHorarioResponse>> buscarTodosAtivos() {
        List<ProfessorHorarioResponse> horarios = phService.buscarTodosAtivos();


        return ResponseEntity.ok(horarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProfessorHorarioResponse> buscarPorId(@PathVariable Long id) {
        ProfessorHorarioResponse response = phService.buscarPorId(id);


        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProfessorHorarioResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProfessorHorarioUpdateRequest request) {

        ProfessorHorarioResponse response = phService.atualizar(id, request);


        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        phService.deletar(id);


        return ResponseEntity.noContent().build();
    }
}