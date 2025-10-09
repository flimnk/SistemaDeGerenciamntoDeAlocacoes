package com.example.demo.domain.professor.dto;

import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.vo.Cpf;

public record ProfessorResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String registro,
        Boolean ativo
) {
    public  ProfessorResponse(Professor professor){
        this(professor.getId(), professor.getNome(), professor.getCpf().getNumero(),professor.getEmail().getEndereco(), professor.getRegistro(), professor.isAtivo());
    }
}