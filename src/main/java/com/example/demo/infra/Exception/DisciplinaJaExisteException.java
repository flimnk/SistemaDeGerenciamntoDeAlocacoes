package com.example.demo.infra.Exception;

public class DisciplinaJaExisteException extends RegrasDeNegocioException {
    public DisciplinaJaExisteException(String campo, String  valor) {
        super("Já existe uma disciplina com o mesmo " +campo + ": " + valor);
    }
}
