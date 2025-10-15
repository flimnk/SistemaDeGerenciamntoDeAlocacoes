package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponseSimples;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.PrioridadeNivel;

public record PrioridadeResponseSimples(
        Long id,
        MatrizDisciplinaResponseSimples matrizDisciplinaResponseSimples,
        PrioridadeNivel prioridade
) {
    public PrioridadeResponseSimples(Prioridade prioridade) {
        this(
                prioridade.getId(),
                prioridade.getMatrizDisciplina() != null
                        ? new MatrizDisciplinaResponseSimples(prioridade.getMatrizDisciplina())
                        : null,
                prioridade.getPrioridade()
        );
    }
}
