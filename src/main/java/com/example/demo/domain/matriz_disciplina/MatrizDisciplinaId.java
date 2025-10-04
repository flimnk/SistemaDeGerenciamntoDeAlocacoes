package com.example.demo.domain.matriz_disciplina;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MatrizDisciplinaId implements Serializable {
    private Long matrizId;
    private Long disciplinaId;

    public MatrizDisciplinaId(Long matrizId, Long disciplinaId) {
        this.matrizId = matrizId;
        this.disciplinaId = disciplinaId;
    }
}
