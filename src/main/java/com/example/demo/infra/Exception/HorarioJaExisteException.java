package com.example.demo.infra.Exception;

public class HorarioJaExisteException extends RegrasDeNegocioException {
    public HorarioJaExisteException(String detalhe) {
        super("Já existe um horário cadastrado: " + detalhe);
    }
}
