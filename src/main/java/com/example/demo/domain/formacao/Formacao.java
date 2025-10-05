package com.example.demo.domain.formacao;

import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "formacao")
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_curso", nullable = false, length = 150)
    private String nomeCurso;

    @Column(name = "ano_conclusao", nullable = false)
    private LocalDate anoConclusao;

    @Column(name = "nome_instituicao", nullable = false, length = 150)
    private String nomeInstituicao;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoriaDaTitulacao categoria;



    public Formacao(String nomeCurso, LocalDate anoConclusao, String nomeInstituicao, CategoriaDaTitulacao categoria, Professor professor) {
        if (nomeCurso == null || nomeCurso.isBlank())
            throw new IllegalArgumentException("Nome do curso não pode ser vazio");
        if (anoConclusao == null)
            throw new IllegalArgumentException("Ano de conclusão é obrigatório");
        if (nomeInstituicao == null || nomeInstituicao.isBlank())
            throw new IllegalArgumentException("Nome da instituição não pode ser vazio");
        if (categoria == null)
            throw new IllegalArgumentException("Categoria da titulação é obrigatória");
        if (professor== null)
            throw new IllegalArgumentException("Formação deve estar vinculado a um professor");


        this.nomeCurso = nomeCurso;
        this.anoConclusao = anoConclusao;
        this.nomeInstituicao = nomeInstituicao;
        this.categoria = categoria;
        this.professor = professor;
    }



    public Long getId() {
        return id;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public LocalDate getAnoConclusao() {
        return anoConclusao;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public CategoriaDaTitulacao getCategoria() {
        return categoria;
    }
}
