package com.example.demo.domain.professor.dto;

import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.escola.dto.EscolaResponse;
import com.example.demo.domain.escola.dto.EscolaResponseSimples;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.vo.Cpf;

import java.util.Set;
import java.util.stream.Collectors;

public record ProfessorResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String registro,
        Boolean ativo,
        Set<EscolaResponseSimples> escolas
) {
    public  ProfessorResponse(Professor professor){
        this(professor.getId(), professor.getNome(), professor.getCpf().getNumero(),professor.getEmail().getEndereco(),
                professor.getRegistro(), professor.isAtivo(), professor.getEscolas().stream().map(EscolaResponseSimples::new).collect(Collectors.toSet()));
    }
}