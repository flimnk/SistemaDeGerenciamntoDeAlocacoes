package com.example.demo.domain.curso.dto;

import com.example.demo.domain.curso.Curso;

public record CursoResponseSimples (
        Long id,
        String nome,
        Integer duracaoEmSemestre
){
    public  CursoResponseSimples (Curso curso){
        this(curso.getId(),curso.getNome(),curso.getDuracaoEmSemestre());
    }
}
