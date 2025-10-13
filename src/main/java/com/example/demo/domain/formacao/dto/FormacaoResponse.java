package com.example.demo.domain.formacao.dto;

import com.example.demo.domain.formacao.CategoriaDaTitulacao;
import com.example.demo.domain.formacao.Formacao;
import com.example.demo.domain.professor.dto.ProfessorResponse;
import com.example.demo.domain.professor.dto.ProfessorResponseSimples;

import java.time.LocalDate;

public record FormacaoResponse(
        Long id,
        String nomeCurso,
        LocalDate anoConclusao,
        String nomeInstituicao,
        CategoriaDaTitulacao categoria,
        ProfessorResponseSimples professorResponse
) {
    public FormacaoResponse(Formacao formacao) {
        this(formacao.getId(),
             formacao.getNomeCurso(),
             formacao.getAnoConclusao(),
             formacao.getNomeInstituicao(),
             formacao.getCategoria(),
             new ProfessorResponseSimples(formacao.getProfessor())   );
    }
}
