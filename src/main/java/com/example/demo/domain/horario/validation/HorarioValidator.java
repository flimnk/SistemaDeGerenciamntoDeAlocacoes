package com.example.demo.domain.horario.validation;// com.example.demo.domain.horario/HorarioValidator.java

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Turno;
import com.example.demo.infra.Exception.HorarioForaDoTurnoException;
import com.example.demo.infra.Exception.HorarioInicioMaiorHorarioFinalExcpetion;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class HorarioValidator {

    public static void validar(DiaSemana diaSemana, Turno turno, LocalTime inicio, LocalTime fim) {

        if (diaSemana == null) throw new IllegalArgumentException("O dia da semana é obrigatório.");
        if (turno == null) throw new IllegalArgumentException("O turno é obrigatório.");
        if (inicio == null) throw new IllegalArgumentException("O horário de início é obrigatório.");
        if (fim == null) throw new IllegalArgumentException("O horário de fim é obrigatório.");


        if (!inicio.isBefore(fim)) {
            throw new HorarioInicioMaiorHorarioFinalExcpetion(inicio,fim);
        }

        verificarCoerenciaTurno(turno, inicio, fim);
    }

    private static void verificarCoerenciaTurno(Turno turno, LocalTime inicio, LocalTime fim) {

        LocalTime limiteMin = turno.getLimiteInicio();
        LocalTime limiteMax = turno.getLimiteFim();

        if (inicio.isBefore(limiteMin)) {
            throw new HorarioForaDoTurnoException("inicio", "anterior",inicio,turno ,limiteMin);
        }

        if (fim.isAfter(limiteMax)) {
            throw new HorarioForaDoTurnoException("fim", "posterior",fim,turno ,limiteMax);
        }
    }
}