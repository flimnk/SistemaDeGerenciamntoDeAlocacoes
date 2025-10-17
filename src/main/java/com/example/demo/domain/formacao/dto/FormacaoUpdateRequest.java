package com.example.demo.domain.formacao.dto;

import com.example.demo.domain.formacao.CategoriaDaTitulacao;

import java.time.LocalDate;

public record FormacaoUpdateRequest(
        String nomeCurso,
        LocalDate anoConclusao,
        String nomeInstituicao,
        CategoriaDaTitulacao categoria

) {}
