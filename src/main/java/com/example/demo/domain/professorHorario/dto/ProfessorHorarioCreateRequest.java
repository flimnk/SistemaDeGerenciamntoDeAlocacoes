package com.example.demo.domain.professorHorario.dto;

import jakarta.validation.constraints.NotNull;

public record ProfessorHorarioCreateRequest(
        @NotNull(message = "O ID do Professor é obrigatório.")
        Long professorId,

        @NotNull(message = "O ID do Horário é obrigatório.")
        Long horarioId

) {}