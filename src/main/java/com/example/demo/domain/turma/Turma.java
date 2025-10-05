package com.example.demo.domain.turma;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table (name = "turma")
public class Turma  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "codigo",nullable = false)
    private  String codigo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso ;

    @OneToMany(mappedBy = "turma", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alocacao> alocacoes = new HashSet<>();



    public Turma(Curso curso , String codigo) {
        if(curso== null ) throw  new IllegalArgumentException("Turma deve estar vinculada a um curso");
        if(codigo== null ) throw  new IllegalArgumentException("Turma deve ter um código");

        this.curso = curso;
        this.codigo = codigo;
    }


    public void adicionarAlocacao(Alocacao alocacao) {

        this.alocacoes.add(alocacao);


        if (alocacao.getTurma() != this) {
            alocacao.setTurma(this);
        }
    }





    public void removerAlocacao(Alocacao alocacao) {
        this.alocacoes.remove(alocacao);
        if (alocacao.getTurma() != null && alocacao.getTurma().equals(this)) {
            alocacao.setTurma(null);
        }
    }
}
