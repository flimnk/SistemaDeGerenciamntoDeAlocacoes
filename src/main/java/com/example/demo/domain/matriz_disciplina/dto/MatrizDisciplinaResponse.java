// MatrizDisciplinaResponse.java
package com.example.demo.domain.matriz_disciplina.dto;

import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;

public record MatrizDisciplinaResponse(
        Long id,
        Long matrizId,
        Long disciplinaId,
        int cargaHoraria,
        int semestre,
        String obrigatoria
) {
    public MatrizDisciplinaResponse(MatrizDisciplina md) {
        this(md.getId(),
             md.getMatriz().getId(),
             md.getDisciplina().getId(),
             md.getCargaHoraria(),
             md.getSemestre(),
             md.getObrigatoria().name());
    }
}
