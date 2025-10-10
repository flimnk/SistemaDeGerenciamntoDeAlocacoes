package com.example.demo.domain.escola.dto;

import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.escola.Escola;

import java.util.Set;
import java.util.stream.Collectors;

public record EscolaResponse(
        Long id,
        CategoriaEscola categoriaEscola,
        boolean ativo,

        Set<Long> cursoIds,
        Set<Long> professorIds
) {
    public EscolaResponse(Escola escola) {
        this(
            escola.getId(),
            escola.getCategoriaEscola(),
            escola.isAtivo(),
            escola.getCursos().stream().map(curso -> curso.getId()).collect(Collectors.toSet()),
            escola.getProfessores().stream().map(professor -> professor.getId()).collect(Collectors.toSet())
        );
    }
}