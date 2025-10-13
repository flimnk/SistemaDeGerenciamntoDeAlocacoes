package com.example.demo.domain.disciplina;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor

@NoArgsConstructor
@Table (name = "disciplina")
@Where(clause = "ativo = true")
public class Disciplina implements Ativavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false,unique = true)
    private  String nome;

    @Column(name = "descricao",nullable = false,unique = true)
    private  String descricao;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;


    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatrizDisciplina> matrizes = new HashSet<>();




    public Disciplina( String nome, String descricao) {
        if(nome == null || nome.isBlank()) throw  new IllegalArgumentException("Disciplina deve ter um nome");
        if(descricao == null || descricao.isBlank()) throw  new IllegalArgumentException("Disciplina deve ter uma descrição");
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
    }

    public void adicionarMatriz(MatrizDisciplina matrizDisciplina) {
        this.matrizes.add(matrizDisciplina);


        if (matrizDisciplina.getDisciplina() != this) {
            matrizDisciplina.setDisciplina(this);
        }
    }
    public void removerMatriz(MatrizDisciplina matrizDisciplina) {
        this.matrizes.remove(matrizDisciplina);


        if (matrizDisciplina.getDisciplina() != null && matrizDisciplina.getDisciplina().equals(this)) {
            matrizDisciplina.setDisciplina(null);
        }
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
