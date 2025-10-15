package com.example.demo.domain.professorHorario.dto;

import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.professor.dto.ProfessorResponseSimples;
import com.example.demo.domain.professorHorario.ProfessorHorario;

public record ProfessorHorarioResponse(
        Long id,
        ProfessorResponseSimples professorResponseSimples,
        HorarioResponse horarioResponse,
        boolean disponivel
) {
    public ProfessorHorarioResponse(ProfessorHorario ph) {
        this(
            ph.getId(),
            new ProfessorResponseSimples(ph.getProfessor()),
           new HorarioResponse( ph.getHorario()),
            ph.isDisponivel()
        );
    }
}