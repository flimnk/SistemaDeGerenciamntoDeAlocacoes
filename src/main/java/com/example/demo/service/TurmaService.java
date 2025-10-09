package com.example.demo.service;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.turma.Turma;
import com.example.demo.domain.turma.dto.TurmaCreateRequest;
import com.example.demo.domain.turma.dto.TurmaResponse;
import com.example.demo.domain.turma.dto.TurmaUpdateRequest;

import com.example.demo.infra.Exception.TurmaJaExisteException;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;

    public TurmaService(TurmaRepository turmaRepository, CursoRepository cursoRepository) {
        this.turmaRepository = turmaRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional(readOnly = true)
    public List<TurmaResponse> buscarTodas() {
        return turmaRepository.findAll().stream()
                .map(TurmaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TurmaResponse buscarPorId(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
        return new TurmaResponse(turma);
    }

    @Transactional
    public TurmaResponse criarTurma(TurmaCreateRequest request) {
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com id: " + request.cursoId()));

        if(turmaRepository.existsByCodigo(request.codigo())) throw new TurmaJaExisteException(request.toString());

        Turma turma = new Turma(curso, request.codigo());
        turmaRepository.save(turma);
        return new TurmaResponse(turma);
    }

    @Transactional
    public TurmaResponse atualizarTurma(Long id, TurmaUpdateRequest request) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));

        Curso curso = Optional.ofNullable(request.cursoId())
                .flatMap(cursoRepository::findById)
                .orElse(null);

        boolean codigoDuplicado = Optional.ofNullable(request.codigo())
                .filter(codigo -> !codigo.equals(turma.getCodigo()))
                .map(turmaRepository::existsByCodigo)
                .orElse(false);

        turma.atualizar(request.codigo(), curso, codigoDuplicado);

        return new TurmaResponse(turmaRepository.save(turma));
    }


    @Transactional
    public void deletarTurma(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
        turmaRepository.delete(turma);
    }



}
