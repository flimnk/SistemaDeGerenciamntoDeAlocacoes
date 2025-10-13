package com.example.demo.domain.professorHorario.dto;

import com.example.demo.domain.professorHorario.ProfessorHorario;

public record ProfessorHorarioResponse(
        Long id,
        Long professorId,
        Long horarioId,
        boolean disponivel
) {
    public ProfessorHorarioResponse(ProfessorHorario ph) {
        this(
            ph.getId(),
            ph.getProfessor().getId(),
            ph.getHorario().getId(),
            ph.isDisponivel()
        );
    }
}