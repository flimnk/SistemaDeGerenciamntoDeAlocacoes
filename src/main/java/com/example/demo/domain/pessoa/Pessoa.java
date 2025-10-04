package com.example.demo.domain.pessoa;

import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "idade",nullable = false) // Exemplo de nomeação
    private Integer idade;




    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "cpf_numero", unique = true,nullable = false))
    })
    private Cpf cpf;

    @AttributeOverrides({
        @AttributeOverride(name = "endereco", column = @Column(name = "email_endereco", unique = true,nullable = false)),

    })
    private Email email;



}