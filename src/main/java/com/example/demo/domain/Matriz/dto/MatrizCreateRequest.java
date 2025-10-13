package com.example.demo.domain.Matriz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Year;

public record MatrizCreateRequest(
        @NotNull(message = "Ano de vigência é obrigatório")
        Year anoVigencia,
        @NotNull(message = "CursoId é obrigatório")
        Long cursoId
) {}
