package com.example.demo.domain.horario.dto;




import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Turno;
import com.example.demo.infra.Exception.HorarioInicioMaiorHorarioFinalExcpetion;

import java.time.LocalTime;

public record HorarioUpdateRequest(
        DiaSemana diaSemana,
        Turno turno,
        LocalTime horarioInicio,
        LocalTime horarioFinal
        ) {
    public HorarioUpdateRequest {
        if (horarioInicio.isAfter(horarioFinal)) {
            throw new HorarioInicioMaiorHorarioFinalExcpetion(horarioInicio,horarioFinal);
        }
    }
}