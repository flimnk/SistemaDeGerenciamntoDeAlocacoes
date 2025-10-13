package com.example.demo.domain.alocacao.dto;

public record AlocacaoUpdateRequest(
    Long professorId,
    Long matrizDisciplinaId,
    Long horarioId,
    Long turmaId
) {}