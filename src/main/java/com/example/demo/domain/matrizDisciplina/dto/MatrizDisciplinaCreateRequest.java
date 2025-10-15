package com.example.demo.domain.matrizDisciplina.dto;

import com.example.demo.domain.matrizDisciplina.Obrigatoriedade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MatrizDisciplinaCreateRequest(
        @NotNull(message = "O ID da matriz é obrigatório") Long matrizId,
        @NotNull(message = "O ID da disciplina é obrigatório") Long disciplinaId,
        @Min(value = 1, message = "A carga horária deve ser maior que zero") int cargaHoraria,
        @Min(value = 1, message = "O semestre deve ser maior que zero") int semestre,
        @NotNull(message = "A obrigatoriedade é obrigatória") Obrigatoriedade obrigatoria
) {}
