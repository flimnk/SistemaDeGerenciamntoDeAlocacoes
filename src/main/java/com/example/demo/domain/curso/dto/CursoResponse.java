package com.example.demo.domain.curso.dto;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.escola.dto.EscolaResponseSimples; // Presume que você tem um DTO simples para Escola

public record CursoResponse(
        Long id,
        String nome,
        Integer duracaoEmSemestre,
        EscolaResponseSimples escola
) {
    public CursoResponse(Curso curso) {
        this(
            curso.getId(),
            curso.getNome(),
            curso.getDuracaoEmSemestre(),
            new EscolaResponseSimples(curso.getEscola().getId(), curso.getEscola().getCategoriaEscola())
        );
    }
}