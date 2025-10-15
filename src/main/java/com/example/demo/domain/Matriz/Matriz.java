package com.example.demo.domain.Matriz;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.disciplina.Disciplina;

import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;

import com.example.demo.domain.matrizDisciplina.Obrigatoriedade;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(
        name = "matriz",
        uniqueConstraints = @UniqueConstraint(columnNames = {"curso_id","ano_vigencia"})
)
public class Matriz  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @Column(name = "nome", nullable = false,unique = true)
    private  String nome;

    @Column(name = "ano_vigencia", nullable = false)
    private Year anoVigencia;



    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "matriz", cascade = CascadeType.ALL)
    private Set<MatrizDisciplina> disciplinas = new HashSet<>();


    public  Matriz (Curso curso ,   Year anoVigencia){
        if(curso == null) throw  new IllegalArgumentException("Matriz deve estar em um curso");
        if(anoVigencia == null ) throw  new IllegalArgumentException("Matriz deve estar em um curso");

        curso.adicionarMatriz(this);
        this.anoVigencia = anoVigencia;
        this.nome = gerarNameMatriz(curso.getNome(),anoVigencia);
    }

    public  static String gerarNameMatriz(String nomeCurso , Year anoVigencia) {
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




    @Override
    public String toString() {
        return "Matriz{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", anoVigencia=" + anoVigencia +
                '}';
    }
}
