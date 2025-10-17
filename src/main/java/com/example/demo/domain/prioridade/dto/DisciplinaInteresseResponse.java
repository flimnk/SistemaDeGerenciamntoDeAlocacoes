package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponseSimples;
import com.example.demo.domain.prioridade.PrioridadeNivel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;


public record DisciplinaInteresseResponse(
       MatrizDisciplinaResponseSimples matrizDisciplina,
        Long quantidadeProfessoresInteressados

) {
    public DisciplinaInteresseResponse( MatrizDisciplinaResponseSimples matrizDisciplina,
                                        Long quantidadeProfessoresInteressados) {

        this.matrizDisciplina = matrizDisciplina;
        this.quantidadeProfessoresInteressados = quantidadeProfessoresInteressados;

    }
}
