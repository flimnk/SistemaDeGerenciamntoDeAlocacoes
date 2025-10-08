package com.example.demo.repository;

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.horario.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    boolean existsByDiaSemanaAndTurnoAndHorarioInicioAndHorarioFim(
            DiaSemana diaSemana,
            Turno turno,
            LocalTime horarioInicio,
            LocalTime horarioFim
    );

    boolean existsByDiaSemanaAndTurnoAndHorarioInicioAndHorarioFimAndIdNot(DiaSemana diaSemana, Turno turno, LocalTime localTime, LocalTime localTime1, Long id);
}