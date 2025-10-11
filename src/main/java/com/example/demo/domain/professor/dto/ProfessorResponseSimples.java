package com.example.demo.domain.professor.dto;

import com.example.demo.domain.professor.Professor;

public record ProfessorResponseSimples(
        Long id,
        String nome,
        String cpf

) {
    public ProfessorResponseSimples(Professor professor){
        this(professor.getId(), professor.getNome(), professor.getCpf().getNumero());
    }
}