package com.example.demo.domain.escola.dto;

import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.escola.Escola;

import java.util.Set;

public record EscolaResponseSimples(
        Long id,
        CategoriaEscola categoriaEscola

) {
    public  EscolaResponseSimples (Escola escola){
        this(escola.getId(),escola.getCategoriaEscola());
    }

}