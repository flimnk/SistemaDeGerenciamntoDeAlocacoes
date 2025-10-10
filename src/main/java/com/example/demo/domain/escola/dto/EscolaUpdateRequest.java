package com.example.demo.domain.escola.dto;

import com.example.demo.domain.escola.CategoriaEscola;
import jakarta.validation.constraints.NotNull;

public record EscolaUpdateRequest(
        CategoriaEscola categoriaEscola

) {}