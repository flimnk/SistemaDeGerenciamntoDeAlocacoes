package com.example.demo.domain.matriz_disciplina;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.disciplina.Disciplina;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "matriz_disciplina")
public class MatrizDisciplina {

    @EmbeddedId
    private MatrizDisciplinaId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("matrizId")
    @JoinColumn(name = "matriz_id", nullable = false)
    private Matriz matriz;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("disciplinaId")
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "carga_horaria", nullable = false)
    private int cargaHoraria;

    @Column(name = "semestre", nullable = false)
    private int semestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "obrigatoria", nullable = false)
    private Obrigatoriedade obrigatoria;

    protected MatrizDisciplina() {}

    public MatrizDisciplina(Matriz matriz,
                            Disciplina disciplina,
                            int cargaHoraria,
                            int semestre,
                            Obrigatoriedade obrigatoria) {

        if (matriz == null) throw new IllegalArgumentException("Matriz não pode ser nula");
        if (disciplina == null) throw new IllegalArgumentException("Disciplina não pode ser nula");
        if (cargaHoraria <= 0) throw new IllegalArgumentException("Carga horária deve ser maior que zero");
        if (semestre <= 0) throw new IllegalArgumentException("Semestre deve ser maior que zero");
        if (obrigatoria == null) throw new IllegalArgumentException("Obrigatoriedade não pode ser nula");

        this.matriz = matriz;
        this.disciplina = disciplina;
        this.cargaHoraria = cargaHoraria;
        this.semestre = semestre;
        this.obrigatoria = obrigatoria;

        this.id = new MatrizDisciplinaId(matriz.getId(), disciplina.getId());
    }
}
