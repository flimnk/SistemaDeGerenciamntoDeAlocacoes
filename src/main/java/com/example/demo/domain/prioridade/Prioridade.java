package com.example.demo.domain.prioridade;

import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.professor.Professor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prioridade",
    uniqueConstraints = @UniqueConstraint(columnNames =
            {"matriz_disciplina_id","professor_id"})
)
public class Prioridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "matriz_disciplina_id", nullable = false)
    private MatrizDisciplina matrizDisciplina;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    @JsonIgnore
    private Professor professor;

    // Campo do Enum que define o nível de prioridade (Alta, Média, Baixa)
    @Column(name = "prioridade_nivel", nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadeNivel prioridadeNivel;

    public Prioridade(MatrizDisciplina matrizDisciplina, PrioridadeNivel prioridade, Professor professor){
        if(matrizDisciplina == null) throw  new IllegalArgumentException("Prioridade deve ter uma matrizDisciplina");
        if(prioridade == null) throw  new IllegalArgumentException("Prioridade deve possuir um prioridadeNivel");
        if(professor == null) throw  new IllegalArgumentException("Prioridade deve possuir um professor");

        this.prioridadeNivel = prioridade;
        this.professor = professor;
        this.matrizDisciplina = matrizDisciplina;
    }

}