package com.example.demo.domain.vo;

import com.example.demo.infra.Exception.CpfInvalidoException;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Cpf {

    private final String numero;

    public Cpf(String numero) {
        if (!isValid(numero)) {

            throw new CpfInvalidoException("O CPF informado é inválido.");
        }

        this.numero = format(numero);
    }

    private boolean isValid(String numero) {
        String cpf = numero.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;


        if (cpf.chars().distinct().count() == 1) return false;

        return validarDigitos(cpf);
    }


    private String format(String numero) {
        return numero.replaceAll("[^0-9]", "");
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return Objects.equals(numero, cpf.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    private boolean validarDigitos(String cpf) {

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        if (digito1 != (cpf.charAt(9) - '0')) return false;


        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        return digito2 == (cpf.charAt(10) - '0');
    }

}
