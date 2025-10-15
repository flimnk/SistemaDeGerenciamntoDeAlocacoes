package com.example.demo.domain.professorHorario.dto;

import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.professor.dto.ProfessorResponseSimples;
import com.example.demo.domain.professorHorario.ProfessorHorario;

public record ProfessorHorarioResponseSimples(
        Long id,
        HorarioResponse horarioResponse,
        boolean disponivel
) {
    public ProfessorHorarioResponseSimples(ProfessorHorario ph) {
        this(
                ph.getId(),
                ph.getHorario() != null ? new HorarioResponse(ph.getHorario()) : null,
                ph.isDisponivel()
        );
    }
}
