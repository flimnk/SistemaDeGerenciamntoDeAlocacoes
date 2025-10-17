package com.example.demo.domain.matrizDisciplina;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.infra.Exception.SemestreForaDoIntervaloExepition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(
        name = "matriz_disciplina",
        uniqueConstraints = @UniqueConstraint(columnNames = {"matriz_id", "disciplina_id"})
)
public class MatrizDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne( optional = false)
    @JoinColumn(name = "matriz_id", nullable = false)
    private Matriz matriz;

    @ManyToOne( optional = false)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "carga_horaria", nullable = false)
    private int cargaHoraria;

    @Column(name = "semestre", nullable = false)
    private int semestre;



    @OneToMany(mappedBy = "matrizDisciplina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alocacao> alocacoes = new HashSet<>();

    @OneToMany(mappedBy = "matrizDisciplina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Prioridade> prioridades = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "obrigatoria", nullable = false)
    private Obrigatoriedade obrigatoria;


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
        this.setSemestre(semestre);
        this.obrigatoria = obrigatoria;


    }

    public  void setSemestre(Integer semestre){
        if (semestre > this.getMatriz().getCurso().getDuracaoEmSemestre())
            throw  new SemestreForaDoIntervaloExepition("Semestre maior do que o o semestre final do curso");
        this.semestre = semestre;
    }

    public void adicionarPrioridade(Prioridade prioridade) {

        this.prioridades.add(prioridade);
        if (prioridade.getMatrizDisciplina() != this) {
            prioridade.setMatrizDisciplina(this);
        }
    }

    public void removerPrioridade(Prioridade prioridade) {
        this.prioridades.remove(prioridade);
        if (prioridade.getMatrizDisciplina() != null && prioridade.getMatrizDisciplina().equals(this)) {
            prioridade.setMatrizDisciplina(null);
        }
    }


    public void adicionarAlocacao(Alocacao alocacao) {
        this.alocacoes.add(alocacao);
        if (alocacao.getMatrizDisciplina() != this) {
            alocacao.setMatrizDisciplina(this);
        }
    }
    public void removerAlocacao(Alocacao alocacao) {
        this.alocacoes.remove(alocacao);

        if (alocacao.getMatrizDisciplina() != null && alocacao.getMatrizDisciplina().equals(this)) {
            alocacao.setMatrizDisciplina(null);
        }
    }




}
