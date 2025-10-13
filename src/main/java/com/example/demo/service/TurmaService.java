package com.example.demo.service;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.turma.Turma;
import com.example.demo.domain.turma.dto.TurmaCreateRequest;
import com.example.demo.domain.turma.dto.TurmaResponse;
import com.example.demo.domain.turma.dto.TurmaUpdateRequest;
import com.example.demo.infra.Exception.EscolaInativaExeption; // Exceção principal
import com.example.demo.infra.Exception.TurmaJaExisteException;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;

    // --- CRIAÇÃO (POST) ---
    @Transactional
    public TurmaResponse criar(TurmaCreateRequest request) {
        // Valida dependência: Turma só pode ser criada se a Escola estiver ativa
        Curso curso = buscarCursoEValidarAtividadeEscola(request.cursoId());

        if (turmaRepository.existsByCodigo(request.codigo())) {
            throw new TurmaJaExisteException("Já existe uma turma com o código: " + request.codigo());
        }

        Turma turma = new Turma(curso, request.codigo());
        turma = turmaRepository.save(turma);
        return new TurmaResponse(turma);
    }

    // --- ATUALIZAÇÃO (PATCH) ---
    @Transactional
    public TurmaResponse atualizar(Long id, TurmaUpdateRequest request) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));


        validarAtividadeEscola(turma.getCurso());

        // 1. Lógica de atualização de Curso
        if (request.cursoId() != null && !request.cursoId().equals(turma.getCurso().getId())) {

            Curso novoCurso = buscarCursoEValidarAtividadeEscola(request.cursoId());
            turma.setCurso(novoCurso);
        }


        if (request.codigo() != null && !request.codigo().equals(turma.getCodigo())) {
            if (turmaRepository.existsByCodigo(request.codigo())) {
                throw new TurmaJaExisteException("Já existe outra turma com o código: " + request.codigo());
            }
            turma.setCodigo(request.codigo());
        }

        turma = turmaRepository.save(turma);
        return new TurmaResponse(turma);
    }


    @Transactional(readOnly = true)
    public TurmaResponse buscarPorId(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));


        validarAtividadeEscola(turma.getCurso());

        return new TurmaResponse(turma);
    }


    @Transactional(readOnly = true)
    public List<TurmaResponse> buscarTodas() {
        // Busca todas e filtra aquelas cuja Escola está ativa
        return turmaRepository.findAll().stream()
                .filter(t -> t.getCurso().getEscola().isAtivo())
                .map(TurmaResponse::new)
                .collect(Collectors.toList());


    }


    @Transactional
    public void deletar(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));


        validarAtividadeEscola(turma.getCurso());

        turmaRepository.delete(turma);
    }



    private Curso buscarCursoEValidarAtividadeEscola(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));

        validarAtividadeEscola(curso);
        return curso;
    }


    private void validarAtividadeEscola(Curso curso) {
        if (!curso.getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Turma/Curso não pode ser manipulado, pois a Escola está inativa.");
        }
    }
}