package com.example.demo.domain.curso.escola;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.escola.Escola;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "curso")
@Getter
@Setter
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "nome", nullable = false)
    private  String nome;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @Column(name="duracao_em_semestre", nullable = false )
    private Integer duracaoEmSemestre;

    @OneToMany(mappedBy = "curso", fetch =  FetchType.LAZY)
    private Set<Matriz> matrizes  =  new HashSet<>();


    protected Curso(){};
    public  Curso(Escola escola, String nome, Integer duracaoEmSemestre){
        if(escola == null) throw  new IllegalArgumentException("Curso deve possuir uma escola");
        if(duracaoEmSemestre == null)  throw  new IllegalArgumentException("Curso deve possuir a quantidades de semestre");
        if(nome == null || nome.isBlank()) throw  new IllegalArgumentException("Curso deve possuir nome");

        this.escola = escola;
        this.nome  = nome;
        this.duracaoEmSemestre = duracaoEmSemestre;
    }


}