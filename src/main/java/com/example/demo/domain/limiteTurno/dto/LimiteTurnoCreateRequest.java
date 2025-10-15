package com.example.demo.domain.limiteTurno.dto;// package com.example.demo.domain.limitesturno.dto;

import com.example.demo.domain.horario.Turno;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record LimiteTurnoCreateRequest(
    
    @NotNull(message = "O turno é obrigatório.")
    Turno turno,
    
    @NotNull(message = "O limite de início é obrigatório.")
    LocalTime limiteInicio,
    
    @NotNull(message = "O limite de fim é obrigatório.")
    LocalTime limiteFim
) {}