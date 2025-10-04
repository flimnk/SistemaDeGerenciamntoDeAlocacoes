package com.example.demo.domain.professor;

import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.formacao.Formacao;
import com.example.demo.domain.interfaces.Ativavel;

import com.example.demo.domain.pessoa.Pessoa;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professor")
public class Professor  extends Pessoa implements Ativavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "professor_ecola",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "escola_id")
    )

    private Set<Escola> escolas = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String registro;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Formacao> formacoes= new HashSet<>();

    @Column(name= "false", nullable = false)
    private boolean ativo;

    protected Professor() {}

    public Professor( String registro, Set<Formacao> formacao) {
        if (registro == null || registro.isBlank()) throw new IllegalArgumentException("Registro não pode ser nulo ou vazio");
        if (formacao == null) throw new IllegalArgumentException("Formação é obrigatória");

        this.registro = registro;
        this.formacoes = formacao;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public Set<Escola> getEscolas() {
        return escolas;
    }

    @Override
    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public void ativar() {
        this.ativo = true;
    }

    @Override
    public void desativar() {
        this.ativo = false;
    }
}
