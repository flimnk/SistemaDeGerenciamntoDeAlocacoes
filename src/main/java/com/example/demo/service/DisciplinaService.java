package com.example.demo.service;

import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.disciplina.DisciplinaCreateRequest;
import com.example.demo.domain.disciplina.DisciplinaResponse;
import com.example.demo.domain.disciplina.DisciplinaUpdateRequest;
import com.example.demo.infra.Exception.DisciplinaJaExisteException;
import com.example.demo.infra.Exception.DisciplinaNotFoundException;
import com.example.demo.repository.DisciplinaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    private final DisciplinaRepository repository;

    @Autowired
    public DisciplinaService(DisciplinaRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public DisciplinaResponse criar(DisciplinaCreateRequest disciplinaRequest){

        if(repository.existsByNome(disciplinaRequest.nome())) {
            throw new DisciplinaJaExisteException("nome", disciplinaRequest.nome());
        }
        if(repository.existsByDescricao(disciplinaRequest.descricao())) {
            throw new DisciplinaJaExisteException("descricao", disciplinaRequest.descricao());
        }
        Disciplina disciplina = new Disciplina(disciplinaRequest.nome(), disciplinaRequest.descricao());
        repository.save(disciplina);
        return new DisciplinaResponse(disciplina);
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> buscarTodas() {
        return repository.findAll().stream()
                .map(DisciplinaResponse::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<DisciplinaResponse> buscarTodasAtivas() {

        return repository.findByAtivoTrue().stream()
                .map(DisciplinaResponse::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(Long id){
        var disciplina = repository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException(id));

        return new DisciplinaResponse(disciplina);
    }



    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new DisciplinaNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaUpdateRequest disciplinaRequest) {

        Disciplina disciplina = repository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException(id));

        if (disciplinaRequest.nome() != null && !disciplinaRequest.nome().equals(disciplina.getNome())) {
            if (repository.existsByNome(disciplinaRequest.nome())) {
                throw new DisciplinaJaExisteException("nome", disciplinaRequest.nome());
            }
            disciplina.setNome(disciplinaRequest.nome());
        }
        if (disciplinaRequest.descricao() != null && !disciplinaRequest.descricao().equals(disciplina.getDescricao())) {
            if (repository.existsByDescricao(disciplinaRequest.descricao())) {
                throw new DisciplinaJaExisteException("descricao", disciplinaRequest.descricao());
            }
            disciplina.setDescricao(disciplinaRequest.descricao());
        }
        repository.save(disciplina);


        return new DisciplinaResponse(disciplina);
    }

}