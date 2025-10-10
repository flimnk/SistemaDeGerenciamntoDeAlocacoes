package com.example.demo.domain.escola;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "escola")
@Data
@NoArgsConstructor
public class Escola implements Ativavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ativo",nullable = false)
    private boolean ativo = true;

    @Column(name ="categoria" ,nullable = false, length = 50,unique = true)
    @Enumerated(EnumType.STRING)
    private CategoriaEscola categoriaEscola;


    @ManyToMany(mappedBy = "escolas",fetch = FetchType.LAZY)
    private Set<Professor> professores = new HashSet<>();



    @OneToMany(mappedBy = "escola",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Curso> cursos = new HashSet<>();



    public Escola(CategoriaEscola categoriaEscola) {
        if(categoriaEscola == null){
            throw new IllegalArgumentException("Escola deve ter uma categoria");
        }
        this.categoriaEscola = categoriaEscola;
        this.ativo = true;
    }
    public void adicionarCurso(Curso curso) {
        this.cursos.add(curso);


        if (curso.getEscola() != this) {
            curso.setEscola(this);
        }
    }


    public void removerCurso(Curso curso) {
        this.cursos.remove(curso);


        if (curso.getEscola() != null && curso.getEscola().equals(this)) {
            curso.setEscola(null);
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



    public void adicionarProfessor(Professor professor) {
        this.professores.add(professor);
        professor.getEscolas().add(this);
    }

    public void removerProfessor(Professor professor) {
        this.professores.remove(professor);
        professor.getEscolas().remove(this);
    }


}