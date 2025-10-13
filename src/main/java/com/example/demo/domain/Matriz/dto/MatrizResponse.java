package com.example.demo.domain.Matriz.dto;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.curso.dto.CursoResponse;
import com.example.demo.domain.curso.dto.CursoResponseSimples;

import java.time.Year;

public record MatrizResponse(
        Long id,
        String nome,
        Year anoVigencia,
        CursoResponseSimples curso
) {
    public MatrizResponse(Matriz matriz) {
        this(matriz.getId(), matriz.getNome(),matriz.getAnoVigencia(),new CursoResponseSimples(matriz.getCurso()));
    }
}
