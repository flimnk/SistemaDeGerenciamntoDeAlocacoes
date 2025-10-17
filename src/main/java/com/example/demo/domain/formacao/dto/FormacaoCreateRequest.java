package com.example.demo.domain.formacao.dto;

import com.example.demo.domain.formacao.CategoriaDaTitulacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record FormacaoCreateRequest(
        @NotBlank(message = "Nome do curso é obrigatório")
        String nomeCurso,

        @NotNull(message = "Ano de conclusão é obrigatório")
        LocalDate anoConclusao,

        @NotBlank(message = "Nome da instituição é obrigatório")
        String nomeInstituicao,

        @NotNull(message = "Categoria da titulação é obrigatória")
        CategoriaDaTitulacao categoria

) {}
