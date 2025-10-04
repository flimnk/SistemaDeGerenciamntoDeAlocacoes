package com.example.demo.domain.disciplina;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table (name = "disciplina")
public class Disciplina implements Ativavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false)
    private  String nome;

    @Column(name = "descricao",nullable = false)
    private  String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;


    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatrizDisciplina> matrizes = new HashSet<>();


    protected  Disciplina(){};

    public Disciplina( String nome, String descricao) {
        if(nome == null || nome.isBlank()) throw  new IllegalArgumentException("Disciplina deve ter um nome");
        if(descricao == null || descricao.isBlank()) throw  new IllegalArgumentException("Disciplina deve ter uma descrição");
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
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
