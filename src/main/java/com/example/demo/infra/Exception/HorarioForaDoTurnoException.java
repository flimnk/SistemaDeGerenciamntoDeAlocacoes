package com.example.demo.infra.Exception;

import com.example.demo.domain.horario.Turno;

import java.time.LocalTime;

public class HorarioForaDoTurnoException extends  RegrasDeNegocioException {
    public HorarioForaDoTurnoException(String campo1, String campo2 ,LocalTime inicio , Turno turno , LocalTime limiteMin) {
        super(
                "O horário de " + campo1 +" (" + inicio + ") não pode ser " +campo2+" ao limite do turno " +
                        turno.name() + " (" + limiteMin + ")."
        );

    }
}
