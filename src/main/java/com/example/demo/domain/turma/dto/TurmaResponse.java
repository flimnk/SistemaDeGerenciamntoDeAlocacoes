package com.example.demo.domain.turma.dto;

import com.example.demo.domain.turma.Turma;

public record TurmaResponse(
    Long id,
    String codigo,
    String nomeCurso
) {
    public TurmaResponse(Turma turma) {
        this(
            turma.getId(),
            turma.getCodigo(),
            turma.getCurso() != null ? turma.getCurso().getNome() : null
        );
    }
}
