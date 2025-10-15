package com.example.demo.service.validation;

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Turno;

import com.example.demo.domain.limiteTurno.LimiteTurno;
import com.example.demo.infra.Exception.HorarioForaDoTurnoException;
import com.example.demo.infra.Exception.HorarioInicioMaiorHorarioFinalExcpetion;
import com.example.demo.service.LimiteTurnoService; // Import necessário
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class HorarioValidator {

    // 1. Injetar o serviço de limites
    private final LimiteTurnoService limiteTurnoService;

    public HorarioValidator(LimiteTurnoService limiteTurnoService) {
        this.limiteTurnoService = limiteTurnoService;
    }


    public void validar(DiaSemana diaSemana, Turno turno, LocalTime inicio, LocalTime fim) {

        if (diaSemana == null) throw new IllegalArgumentException("O dia da semana é obrigatório.");
        if (turno == null) throw new IllegalArgumentException("O turno é obrigatório.");
        if (inicio == null) throw new IllegalArgumentException("O horário de início é obrigatório.");
        if (fim == null) throw new IllegalArgumentException("O horário de fim é obrigatório.");


        if (!inicio.isBefore(fim)) {
            throw new HorarioInicioMaiorHorarioFinalExcpetion(inicio, fim);
        }

        // 3. Chamar a verificação de coerência de turno
        verificarCoerenciaTurno(turno, inicio, fim);

    }

    private void verificarCoerenciaTurno(Turno turno, LocalTime inicio, LocalTime fim) {

        LimiteTurno limites = limiteTurnoService.buscarLimitePorTurno(turno);

        LocalTime limiteMin = limites.getLimiteInicio();
        LocalTime limiteMax = limites.getLimiteFim();

        if (inicio.isBefore(limiteMin)) {

            throw new HorarioForaDoTurnoException("inicio", "anterior", inicio, turno, limiteMin);
        }

        if (fim.isAfter(limiteMax)) {
            // Ajustar a exceção para refletir a fonte dinâmica (LimiteMax)
            throw new HorarioForaDoTurnoException("fim", "posterior", fim, turno, limiteMax);
        }
    }
}