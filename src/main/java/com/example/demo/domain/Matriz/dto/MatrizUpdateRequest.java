package com.example.demo.domain.Matriz.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Year;

public record MatrizUpdateRequest(

        Year anoVigencia,

        Long cursoId
) {}
