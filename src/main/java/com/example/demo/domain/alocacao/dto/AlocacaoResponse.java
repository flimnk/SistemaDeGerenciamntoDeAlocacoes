package com.example.demo.domain.alocacao.dto;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponseSimples;
import com.example.demo.domain.professor.dto.ProfessorResponseSimples;
import com.example.demo.domain.turma.dto.TurmaResponse;

public record AlocacaoResponse(
    Long id,
    ProfessorResponseSimples professorResponseSimples ,
    MatrizDisciplinaResponseSimples matrizDisciplinaResponseSimples,
    HorarioResponse horarioResponse ,
    TurmaResponse turmaResponse
) {
    public AlocacaoResponse(Alocacao alocacao) {
        this(
            alocacao.getId(),
            new ProfessorResponseSimples(alocacao.getProfessor()),
            new MatrizDisciplinaResponseSimples( alocacao.getMatrizDisciplina()),
            new HorarioResponse(alocacao.getHorario()),
            new TurmaResponse(alocacao.getTurma())

        );
    }
}