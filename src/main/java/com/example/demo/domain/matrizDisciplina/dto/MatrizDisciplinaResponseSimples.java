// MatrizDisciplinaResponse.java
package com.example.demo.domain.matrizDisciplina.dto;

import com.example.demo.domain.disciplina.DisciplinaResponse;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;

public record MatrizDisciplinaResponseSimples(
        Long id,
        DisciplinaResponse disciplinaResponse,
        int cargaHoraria,
        int semestre,
        String obrigatoria
) {
    public MatrizDisciplinaResponseSimples(MatrizDisciplina md) {
        this(md.getId(),
            new DisciplinaResponse(md.getDisciplina()),
             md.getCargaHoraria(),
             md.getSemestre(),
             md.getObrigatoria().name());
    }

    public MatrizDisciplinaResponseSimples(Long id, DisciplinaResponse disciplinaResponse, int cargaHoraria, int semestre, String obrigatoria) {
        this.id = id;
        this.disciplinaResponse = disciplinaResponse;
        this.cargaHoraria = cargaHoraria;
        this.semestre = semestre;
        this.obrigatoria = obrigatoria;
    }
}
