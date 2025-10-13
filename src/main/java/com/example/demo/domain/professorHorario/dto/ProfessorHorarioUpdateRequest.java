package com.example.demo.domain.professorHorario.dto;

public record ProfessorHorarioUpdateRequest(
        Long professorId,
        Long horarioId,
        Boolean disponivel // Permite atualizar apenas a disponibilidade
) {}