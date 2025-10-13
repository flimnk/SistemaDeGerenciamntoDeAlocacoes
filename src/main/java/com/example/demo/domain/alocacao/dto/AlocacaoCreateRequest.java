package com.example.demo.domain.alocacao.dto;

import jakarta.validation.constraints.NotNull;

public record AlocacaoCreateRequest(
    @NotNull(message = "O ID do Professor é obrigatório.")
    Long professorId,

    @NotNull(message = "O ID da Matriz Disciplina é obrigatório.")
    Long matrizDisciplinaId,

    @NotNull(message = "O ID do Horário é obrigatório.")
    Long horarioId,

    @NotNull(message = "O ID da Turma é obrigatório.")
    Long turmaId
) {}