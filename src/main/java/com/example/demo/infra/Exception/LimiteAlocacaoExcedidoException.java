package com.example.demo.infra.Exception;// package com.example.demo.infra.Exception;

public class LimiteAlocacaoExcedidoException extends RegrasDeNegocioException {
    public LimiteAlocacaoExcedidoException(String message) {
        super(message);
    }
}