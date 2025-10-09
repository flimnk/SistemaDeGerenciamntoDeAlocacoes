package com.example.demo.domain.curso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CursoCreateRequest(
        @NotBlank(message = "O nome do curso é obrigatório")
        String nome,

        @NotNull(message = "A duração em semestres é obrigatória")
        @Positive(message = "A duração deve ser um número positivo")
        Integer duracaoEmSemestre,

        @NotNull(message = "O ID da Escola é obrigatório")
        Long escolaId
) {}