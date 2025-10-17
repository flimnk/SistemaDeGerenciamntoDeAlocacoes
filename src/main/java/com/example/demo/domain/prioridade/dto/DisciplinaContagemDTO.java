package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.prioridade.PrioridadeNivel;

// DTO para a projeção da consulta (apenas campos simples)
public record DisciplinaContagemDTO(
    Long matrizDisciplinaId,
    String disciplinaNome,
    Long quantidadeProfessoresInteressados

) {
    public DisciplinaContagemDTO(Long matrizDisciplinaId, String disciplinaNome, Long quantidadeProfessoresInteressados) {
        this.matrizDisciplinaId = matrizDisciplinaId;
        this.disciplinaNome = disciplinaNome;
        this.quantidadeProfessoresInteressados = quantidadeProfessoresInteressados;
    }

    @Override
    public String toString() {
        return "DisciplinaContagemDTO{" +
                "disciplinaId=" + matrizDisciplinaId +
                ", disciplinaNome='" + disciplinaNome + '\'' +
                ", quantidadeProfessoresInteressados=" + quantidadeProfessoresInteressados +

                '}';
    }
}