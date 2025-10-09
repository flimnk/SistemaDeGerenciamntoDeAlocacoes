package com.example.demo.domain.curso.dto;

import jakarta.validation.constraints.Positive;

public record CursoUpdateRequest(
        String nome,

        @Positive(message = "A duração deve ser um número positivo")
        Integer duracaoEmSemestre,

        Long escolaId
) {}