package com.example.demo.domain.escola;

import com.example.demo.domain.curso.escola.Curso;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Novo: Para o construtor padrão
import lombok.AllArgsConstructor; // Novo: Opcional, mas útil

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "escola")
@Getter
@Setter

public class Escola implements Ativavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ativo",nullable = false)
    private boolean ativo = true;

    @Column(name =" categoria" ,nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CategoriaEscola categoriaEscola;


    @ManyToMany(mappedBy = "escolas",fetch = FetchType.LAZY)
    private Set<Professor> professores = new HashSet<>();



    @OneToMany(mappedBy = "escola")
    private Set<Curso> cursos = new HashSet<>();


    protected  Escola(){};
    public Escola(CategoriaEscola categoriaEscola) {
        if(categoriaEscola == null){
            throw new IllegalArgumentException("Escola deve ter uma categoria");
        }
        this.categoriaEscola = categoriaEscola;
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



    public void adicionarProfessor(Professor professor) {
        this.professores.add(professor);
        professor.getEscolas().add(this);
    }

    public void removerProfessor(Professor professor) {
        this.professores.remove(professor);
        professor.getEscolas().remove(this);
    }


}