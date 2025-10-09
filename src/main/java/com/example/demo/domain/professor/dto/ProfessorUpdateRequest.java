package com.example.demo.domain.professor.dto;

import jakarta.validation.constraints.Email;

import java.util.Set;


public record ProfessorUpdateRequest(
        @Email(message = "Email inválido")
        String email,

        Long formacaoId,

        String nome,

        Set<Long> escolasIds
) {}
