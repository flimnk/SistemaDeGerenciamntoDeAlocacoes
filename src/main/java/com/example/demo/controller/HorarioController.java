package com.example.demo.controller;

import com.example.demo.domain.horario.dto.HorarioCreateRequest;
import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.horario.dto.HorarioUpdateRequest;
import com.example.demo.service.HorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }


    @PostMapping
    public ResponseEntity<HorarioResponse> criar(@RequestBody HorarioCreateRequest request) {
        HorarioResponse response = new HorarioResponse(horarioService.criar(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HorarioResponse> atualizar(
            @PathVariable Long id,
            @RequestBody HorarioUpdateRequest request
    ) {
        HorarioResponse response = new HorarioResponse(horarioService.atualizar(id, request));
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        horarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<HorarioResponse>> buscarTodos() {
        List<HorarioResponse> responseList = horarioService.buscarTodos();
        return ResponseEntity.ok(responseList);
    }
}
