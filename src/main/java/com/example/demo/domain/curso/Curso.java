package com.example.demo.domain.curso;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.interfaces.Ativavel;
import com.example.demo.domain.turma.Turma;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "nome", nullable = false,unique = true)
    private  String nome;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "escola_id")
    private Escola escola;


    @Column(name="duracao_em_semestre", nullable = false )
    private Integer duracaoEmSemestre;

    @OneToMany(mappedBy = "curso", fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Matriz> matrizes  =  new HashSet<>();


    @OneToMany(mappedBy = "curso",fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private  Set<Turma> turmas =  new HashSet<>();


    public  Curso(Escola escola, String nome, Integer duracaoEmSemestre){
        if(escola == null) throw  new IllegalArgumentException("Curso deve possuir uma escola");
        if(duracaoEmSemestre == null)  throw  new IllegalArgumentException("Curso deve possuir a quantidades de semestre");
        if(nome == null || nome.isBlank()) throw  new IllegalArgumentException("Curso deve possuir nome");

        this.escola = escola;
        this.nome  = nome;
        this.duracaoEmSemestre = duracaoEmSemestre;
    }

    public void adicionarTurma(Turma turma) {
        this.turmas.add(turma);
        turma.setCurso(this);
    }

    public void removerTurma(Turma turma) {
        this.turmas.remove(turma);
        if (turma.getCurso() != null && turma.getCurso().equals(this)) {
            turma.setCurso(null);
        }
    }
    public void adicionarMatriz(Matriz matriz) {
        this.matrizes.add(matriz);
        if (matriz.getCurso() != this) {
            matriz.setCurso(this);
        }
    }


    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", escola=" + escola +
                ", duracaoEmSemestre=" + duracaoEmSemestre +
                ", matrizes=" + matrizes +
                ", nome='" + nome + '\'' +
                '}';
    }

    public void removerMatriz(Matriz matriz) {
        this.matrizes.remove(matriz);


        if (matriz.getCurso() != null && matriz.getCurso().equals(this)) {
            matriz.setCurso(null);
        }

}
}