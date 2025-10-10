package com.example.demo.domain.escola.dto;

import com.example.demo.domain.escola.CategoriaEscola;
import jakarta.validation.constraints.NotNull;

public record EscolaCreateRequest(
        @NotNull(message = "A categoria da escola é obrigatória")
        CategoriaEscola categoriaEscola
) {}