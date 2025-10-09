package com.example.demo.domain.pessoa;

import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;




    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "cpf_numero", unique = true,nullable = false))
    })
    private Cpf cpf;

    @AttributeOverrides({
        @AttributeOverride(name = "endereco", column = @Column(name = "email_endereco", unique = true,nullable = false)),

    })
    private Email email;

    public Pessoa(String nome, Email email, Cpf cpf) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (email == null) {
            throw new IllegalArgumentException("O e-mail não pode ser nulo.");
        }
        if (cpf == null) {
            throw new IllegalArgumentException("O CPF não pode ser nulo.");
        }
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }
}