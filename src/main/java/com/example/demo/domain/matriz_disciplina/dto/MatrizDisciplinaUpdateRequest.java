// MatrizDisciplinaUpdateRequest.java
package com.example.demo.domain.matriz_disciplina.dto;

import com.example.demo.domain.matriz_disciplina.Obrigatoriedade;
import jakarta.validation.constraints.Min;

public record MatrizDisciplinaUpdateRequest(
        Long matrizId,
        Long disciplinaId,
        @Min(1) Integer cargaHoraria,
        @Min(1) Integer semestre,
        Obrigatoriedade obrigatoria
) {}
