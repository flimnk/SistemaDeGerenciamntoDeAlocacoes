package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.prioridade.PrioridadeNivel;
import jakarta.validation.constraints.NotNull;

public record PrioridadeCreateRequest(
        @NotNull(message = "O ID da Matriz Disciplina é obrigatório.")
        Long matrizDisciplinaId,

        @NotNull(message = "O ID do Professor é obrigatório.")
        Long professorId,

        @NotNull(message = "O nível de prioridade é obrigatório.")
        PrioridadeNivel prioridade
) {}