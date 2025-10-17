package com.example.demo.domain.prioridade.dto;

import com.example.demo.domain.prioridade.PrioridadeNivel;

// Usamos Record, mas permitimos campos nulos para a operação PATCH (atualização parcial)
public record PrioridadeUpdateRequest(
        Long matrizDisciplinaId,

        PrioridadeNivel prioridade
) {}