package com.example.demo.infra.Exception;

import java.time.LocalTime;

public class HorarioInicioMaiorHorarioFinalExcpetion extends RegrasDeNegocioException {
    public HorarioInicioMaiorHorarioFinalExcpetion(LocalTime hora_inicio, LocalTime hora_final) {
        super("Horario inicio: "+hora_inicio +" deve ser menor que: " + hora_final );
    }
}