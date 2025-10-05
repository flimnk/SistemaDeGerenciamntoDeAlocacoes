package com.example.demo.domain.prioridade;

import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prioridade")
public class Prioridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "matriz_disciplina_id", nullable = false)
    private MatrizDisciplina matrizDisciplina;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // Campo do Enum que define o nível de prioridade (Alta, Média, Baixa)
    @Column(name = "prioridade_nivel", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadeNivel prioridade;

    public Prioridade(MatrizDisciplina matrizDisciplina, PrioridadeNivel prioridade, Professor professor){
        if(matrizDisciplina == null) throw  new IllegalArgumentException("Prioridade deve ter uma matrizDisciplina");
        if(prioridade == null) throw  new IllegalArgumentException("Prioridade deve possuir um prioridadeNivel");
        if(professor == null) throw  new IllegalArgumentException("Prioridade deve possuir um professor");

        this.prioridade = prioridade;
        this.professor = professor;
        this.matrizDisciplina = matrizDisciplina;
    }

}