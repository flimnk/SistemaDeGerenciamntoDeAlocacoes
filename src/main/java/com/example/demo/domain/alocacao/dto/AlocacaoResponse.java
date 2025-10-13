package com.example.demo.domain.alocacao.dto;

import com.example.demo.domain.alocacao.Alocacao;

public record AlocacaoResponse(
    Long id,
    Long professorId,
    Long matrizDisciplinaId,
    Long horarioId,
    Long turmaId
) {
    public AlocacaoResponse(Alocacao alocacao) {
        this(
            alocacao.getId(),
            alocacao.getProfessor().getId(),
            alocacao.getMatrizDisciplina().getId(),
            alocacao.getHorario().getId(),
            alocacao.getTurma().getId()
        );
    }
}