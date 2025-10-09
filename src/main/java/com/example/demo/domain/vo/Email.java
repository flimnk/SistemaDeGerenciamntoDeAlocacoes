package com.example.demo.domain.vo;


import com.example.demo.infra.Exception.EmailInvalidoException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;
@NoArgsConstructor
@Embeddable
public class Email {
    

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private String endereco;

    public Email(String endereco) {


        if (!isValid(endereco)) {
            throw new EmailInvalidoException("Formato de e-mail inválido: " + endereco);
        }
        

        this.endereco = endereco.toLowerCase();
    }
    
    private boolean isValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(endereco, email.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }
}