package com.example.demo.service;

import com.example.demo.domain.curso.Curso;
import com.example.demo.domain.curso.dto.CursoCreateRequest;
import com.example.demo.domain.curso.dto.CursoResponse;
import com.example.demo.domain.curso.dto.CursoUpdateRequest;
import com.example.demo.domain.escola.Escola;

import com.example.demo.infra.Exception.CursoJaExisteException;
import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.infra.Exception.TurmaJaExisteException;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.EscolaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class CursoService {

    private final CursoRepository cursoRepository;
    private final EscolaRepository escolaRepository;

    public CursoService(CursoRepository cursoRepository, EscolaRepository escolaRepository) {
        this.cursoRepository = cursoRepository;
        this.escolaRepository = escolaRepository;
    }

    @Transactional
    public CursoResponse criar(CursoCreateRequest request) {
        Escola escola = escolaRepository.findById(request.escolaId())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + request.escolaId()));

        if(!escola.isAtivo()) throw new EscolaInativaExeption("A escola "+ escola.getCategoriaEscola() +" esta inativa");
        if(cursoRepository.existsByNome(request.nome())) throw new CursoJaExisteException("Ja existe uma disciplina com esse nome" + request.nome());

        Curso curso = new Curso(escola, request.nome(), request.duracaoEmSemestre());
        escola.adicionarCurso(curso);
        curso = cursoRepository.save(curso);

        return new CursoResponse(curso);
    }
    @Transactional(readOnly = true)
    public Page<CursoResponse> buscarTodos(Pageable pageable) {
        // Busca apenas cursos cuja escola está ativa
        return cursoRepository.findByEscola_Ativo(true, pageable)
                .map(CursoResponse::new);
    }

    @Transactional(readOnly = true)
    public CursoResponse buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));


        if (!curso.getEscola().isAtivo()) {
            throw new EscolaInativaExeption("O curso está ligado a uma escola inativa.");

        }

        return new CursoResponse(curso);
    }

    @Transactional
    public CursoResponse atualizar(Long id, CursoUpdateRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));


        if (request.nome() != null && !request.nome().isBlank()) {
            if (!request.nome().equalsIgnoreCase(curso.getNome()) && cursoRepository.existsByNome(request.nome())) {
                throw new CursoJaExisteException("Já existe um curso com o nome: " + request.nome());
            }
            curso.setNome(request.nome());
        }
        if (request.duracaoEmSemestre() != null) {
            curso.setDuracaoEmSemestre(request.duracaoEmSemestre());
        }

        if (request.escolaId() != null && !request.escolaId().equals(curso.getEscola().getId())) {
            Escola novaEscola = escolaRepository.findById(request.escolaId())
                    .orElseThrow(() -> new EntityNotFoundException("Nova Escola não encontrada com ID: " + request.escolaId()));

            if(!novaEscola.isAtivo()) throw new EscolaInativaExeption("A escola "+ novaEscola.getCategoriaEscola() +" esta inativa");

            curso.getEscola().removerCurso(curso);
            novaEscola.adicionarCurso(curso);


        }

        curso = cursoRepository.save(curso);
        return new CursoResponse(curso);
    }
    @Transactional
    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));

        if (!curso.getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Não é possível deletar um curso ligado a uma escola inativa.");
        }

        cursoRepository.delete(curso);
    }
}