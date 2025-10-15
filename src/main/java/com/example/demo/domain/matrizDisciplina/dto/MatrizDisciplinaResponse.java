// MatrizDisciplinaResponse.java
package com.example.demo.domain.matrizDisciplina.dto;

import com.example.demo.domain.Matriz.dto.MatrizResponse;
import com.example.demo.domain.disciplina.DisciplinaResponse;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;

public record MatrizDisciplinaResponse(
        Long id,
        MatrizResponse matrizResponse,
        DisciplinaResponse disciplinaResponse,
        int cargaHoraria,
        int semestre,
        String obrigatoria
) {
    public MatrizDisciplinaResponse(MatrizDisciplina md) {
        this(md.getId(),
            new MatrizResponse(md.getMatriz()),
            new DisciplinaResponse(md.getDisciplina()),
             md.getCargaHoraria(),
             md.getSemestre(),
             md.getObrigatoria().name());
    }
}
