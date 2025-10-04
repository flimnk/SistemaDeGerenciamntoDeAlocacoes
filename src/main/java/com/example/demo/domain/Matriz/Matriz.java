package com.example.demo.domain.Matriz;

import com.example.demo.domain.curso.escola.Curso;
import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;

import com.example.demo.domain.matriz_disciplina.Obrigatoriedade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
@Getter
@Entity
@Setter
@Table(name = "matriz")
public class Matriz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @Column(name = "nome", nullable = false)
    private  String nome;

    @Column(name = "ano_vigencia", nullable = false)
    private Year anoVigencia;


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "matriz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatrizDisciplina> disciplinas = new HashSet<>();

    protected Matriz (){};
    public  Matriz (Curso curso ,   Year anoVigencia){
        if(curso == null) throw  new IllegalArgumentException("Matriz deve estar em um curso");
        if(anoVigencia == null ) throw  new IllegalArgumentException("Matriz deve estar em um curso");

        this.curso = curso;
        this.anoVigencia = anoVigencia;
        this.nome = gerarNameMatriz(curso.getNome(),anoVigencia);
    }

    public String gerarNameMatriz(String nomeCurso , Year anoVigencia) {
        return  "%s%s".formatted(nomeCurso, anoVigencia.getValue());

    }
    public void adicionarDisciplina(Disciplina disciplina, int cargaHoraria, int periodo, Obrigatoriedade obrigatoriedade) {
        MatrizDisciplina md = new MatrizDisciplina(this, disciplina, cargaHoraria, periodo,obrigatoriedade);
        disciplinas.add(md);
        disciplina.getMatrizes().add(md);
    }

    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.removeIf(md -> md.getDisciplina().equals(disciplina));
        disciplina.getMatrizes().removeIf(md -> md.getMatriz().equals(this));
    }
}
