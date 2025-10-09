package com.example.demo.infra.Exception;

public class TurmaJaExisteException extends RegrasDeNegocioException {
    public TurmaJaExisteException(String detalhe) {
        super("Já existe uma turma cadastrado: " + detalhe);
    }
}
