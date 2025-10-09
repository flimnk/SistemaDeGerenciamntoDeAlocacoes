package com.example.demo.domain.turma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TurmaCreateRequest(

    @NotNull(message = "O ID do curso é obrigatório.")
    Long cursoId,

    @NotBlank(message = "O código da turma não pode estar vazio.")
    String codigo

) {
    @Override
    public String toString() {
        return "TurmaCreateRequest{" +
                "cursoId=" + cursoId +
                ", codigo='" + codigo + '\'' +
                '}';
    }
}
