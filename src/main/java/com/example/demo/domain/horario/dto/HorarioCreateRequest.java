package com.example.demo.domain.horario.dto;

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Turno;
import com.example.demo.infra.Exception.HorarioInicioMaiorHorarioFinalExcpetion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioCreateRequest(

        @NotNull(message = "O  dia do horario é obrigatório")
        DiaSemana diaSemana,

        @NotNull(message = "A turma do horario é obrigatório")
        Turno turno,

        @NotNull(message = "Hora inical do horario é obrigatório")
        LocalTime horarioInicio,

        @NotNull(message = "Hora inical do horario é obrigatório")
        LocalTime horarioFinal


) {
    public HorarioCreateRequest {

        if (horarioInicio != null && horarioFinal != null) {

            if (horarioInicio.isAfter(horarioFinal)) {
                throw new HorarioInicioMaiorHorarioFinalExcpetion(horarioInicio, horarioFinal);
            }
        }

    }
    @Override
    public String toString() {
        return diaSemana + " " + turno + " (" + horarioInicio + " - " + horarioFinal + ")";
    }

}