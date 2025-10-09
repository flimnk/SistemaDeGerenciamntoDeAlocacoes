package com.example.demo.service;

import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.disciplina.DisciplinaCreateRequest;
import com.example.demo.domain.disciplina.DisciplinaResponse;
import com.example.demo.domain.disciplina.DisciplinaUpdateRequest;
import com.example.demo.infra.Exception.DisciplinaJaExisteException;
import com.example.demo.repository.DisciplinaRepository;

import jakarta.persistence.EntityNotFoundException;
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
    public DisciplinaResponse criar(DisciplinaCreateRequest request){

        if(repository.existsByNome(request.nome())) {
            throw new DisciplinaJaExisteException("nome", request.nome());
        }
        if(repository.existsByDescricao(request.descricao())) {
            throw new DisciplinaJaExisteException("descricao", request.descricao());
        }

        Disciplina disciplina = new Disciplina(request.nome(), request.descricao());
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
                .orElseThrow(() -> new EntityNotFoundException(("Disciplina com ID " + id + " não foi encontrada.")));

        return new DisciplinaResponse(disciplina);
    }



    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(("Disciplina com ID " + id + " não foi encontrada."));
        }
        repository.deleteById(id);
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaUpdateRequest request) {

        Disciplina disciplina = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(("Disciplina com ID " + id + " não foi encontrada.")));

        if (request.nome() != null && !request.nome().equals(disciplina.getNome())) {
            if (repository.existsByNome(request.nome())) {
                throw new DisciplinaJaExisteException("nome", request.nome());
            }
            disciplina.setNome(request.nome());
        }
        if (request.descricao() != null && !request.descricao().equals(disciplina.getDescricao())) {
            if (repository.existsByDescricao(request.descricao())) {
                throw new DisciplinaJaExisteException("descricao", request.descricao());
            }
            disciplina.setDescricao(request.descricao());
        }
        repository.save(disciplina);


        return new DisciplinaResponse(disciplina);
    }

}