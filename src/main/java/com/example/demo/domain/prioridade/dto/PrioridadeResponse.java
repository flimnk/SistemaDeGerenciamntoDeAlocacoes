package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponseSimples;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.PrioridadeNivel;
import com.example.demo.domain.professor.dto.ProfessorResponseSimples;

public record PrioridadeResponse(
        Long id,
        MatrizDisciplinaResponseSimples matrizDisciplinaResponseSimples,
        ProfessorResponseSimples professorResponseSimples,
        PrioridadeNivel prioridade
) {
    public PrioridadeResponse(Prioridade prioridade) {
        this(
            prioridade.getId(),
            new MatrizDisciplinaResponseSimples(prioridade.getMatrizDisciplina()),
            new ProfessorResponseSimples(prioridade.getProfessor()),
            prioridade.getPrioridadeNivel()
        );
    }
}