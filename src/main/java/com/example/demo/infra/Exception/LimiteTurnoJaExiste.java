package com.example.demo.infra.Exception;

public class LimiteTurnoJaExiste extends RegrasDeNegocioException {
    public LimiteTurnoJaExiste(String s) {
        super(s);
    }
}
