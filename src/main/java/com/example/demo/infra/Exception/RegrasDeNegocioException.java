package com.example.demo.infra.Exception;

public abstract class RegrasDeNegocioException  extends  RuntimeException{
    public  RegrasDeNegocioException (String msg){
        super(msg);
    }
}
