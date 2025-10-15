package com.example.demo.domain.limiteTurno.dto;// package com.example.demo.domain.limitesturno.dto;

import com.example.demo.domain.horario.Turno;
import com.example.demo.domain.limiteTurno.LimiteTurno;

import java.time.LocalTime;

public record LimiteTurnoResponse(
    Long id,
    Turno turno,
    LocalTime limiteInicio,
    LocalTime limiteFim
) {
    public LimiteTurnoResponse(LimiteTurno limite) {
        this(limite.getId(), limite.getTurno(), limite.getLimiteInicio(), limite.getLimiteFim());
    }
}