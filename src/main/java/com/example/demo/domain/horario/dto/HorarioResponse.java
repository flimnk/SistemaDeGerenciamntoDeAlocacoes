package com.example.demo.domain.horario.dto;

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.horario.Turno;

import java.time.LocalTime;

public record HorarioResponse(
        Long id,
        DiaSemana diaSemana,
        Turno turno,
        LocalTime horarioInicio,
        LocalTime horarioFinal


) {
    public HorarioResponse(Horario horario){
        this(horario.getId(),horario.getDiaSemana(),horario.getTurno(),horario.getHorarioInicio(),horario.getHorarioFim());
    }
}