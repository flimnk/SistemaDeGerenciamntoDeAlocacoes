package com.example.demo.domain.disciplina;



public record DisciplinaResponse(Long id,String nome,String descricao, boolean ativo) {

    public DisciplinaResponse(Disciplina disciplina) {
        this(disciplina.getId(), disciplina.getNome(), disciplina.getDescricao(), disciplina.isAtivo());
    }
    public DisciplinaResponse(Long id, String nome, String descricao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
    }

}
