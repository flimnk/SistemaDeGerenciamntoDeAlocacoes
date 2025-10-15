package com.example.demo.domain.limiteTurno.dto;// package com.example.demo.domain.limitesturno.dto;

import java.time.LocalTime;

public record LimiteTurnoUpdateRequest(
    

    LocalTime limiteInicio,
    
    LocalTime limiteFim
) {}