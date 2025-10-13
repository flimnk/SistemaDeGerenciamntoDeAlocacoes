package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.PrioridadeNivel;

public record PrioridadeResponse(
        Long id,
        Long matrizDisciplinaId,
        Long professorId,
        PrioridadeNivel prioridade
) {
    public PrioridadeResponse(Prioridade prioridade) {
        this(
            prioridade.getId(),
            prioridade.getMatrizDisciplina().getId(),
            prioridade.getProfessor().getId(),
            prioridade.getPrioridade()
        );
    }
}