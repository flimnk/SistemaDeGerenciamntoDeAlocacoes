package com.example.demo.domain.horario;

import java.time.LocalTime;

public enum Turno {
    MANHA(LocalTime.of(7, 0), LocalTime.of(12, 30)),
    TARDE(LocalTime.of(13, 30), LocalTime.of(16, 0)),
    NOITE(LocalTime.of(19, 30), LocalTime.of(21, 40));

    private final LocalTime limiteInicio;
    private final LocalTime limiteFim;

    Turno(LocalTime limiteInicio, LocalTime limiteFim) {
        this.limiteInicio = limiteInicio;
        this.limiteFim = limiteFim;
    }

    public LocalTime getLimiteInicio() {
        return limiteInicio;
    }

    public LocalTime getLimiteFim() {
        return limiteFim;
    }
}