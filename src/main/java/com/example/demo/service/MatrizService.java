package com.example.demo.service;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.Matriz.dto.MatrizCreateRequest;
import com.example.demo.domain.Matriz.dto.MatrizResponse;
import com.example.demo.domain.Matriz.dto.MatrizUpdateRequest;
import com.example.demo.domain.curso.Curso;
import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.MatrizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class MatrizService {

    private final MatrizRepository matrizRepository;
    private final CursoRepository cursoRepository;

    @Transactional
    public MatrizResponse criar(MatrizCreateRequest request) {
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado: " + request.cursoId()));


        verificaAtivadaEscola(curso);
        verificaDuplicidade(curso, request.anoVigencia(), null);

        Matriz matriz = new Matriz(curso, request.anoVigencia());

        matrizRepository.save(matriz);

        return new MatrizResponse(matriz);
    }

    @Transactional
    public MatrizResponse atualizar(Long id, MatrizUpdateRequest request) {
        Matriz matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz não encontrada: " + id));

        Curso novoCurso = (request.cursoId() != null)
                ? cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado: " + request.cursoId()))
                : matriz.getCurso();

        Year novoAno = (request.anoVigencia() != null)
                ? request.anoVigencia()
                : matriz.getAnoVigencia();

        verificaAtivadaEscola(novoCurso);
        verificaDuplicidade(novoCurso, novoAno, matriz.getId());


        matriz.setCurso(novoCurso);
        matriz.setAnoVigencia(novoAno);
        matriz.setNome(Matriz.gerarNameMatriz(novoCurso.getNome(), novoAno));

        matrizRepository.save(matriz);
        return  new MatrizResponse(matriz);
    }

    @Transactional(readOnly = true)
    public MatrizResponse buscarPorId(Long id) {
        Matriz matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz não encontrada: " + id));

        if (!matriz.getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption("A matriz está ligada a uma escola inativa.");
        }

        return new MatrizResponse(matriz);
    }

    @Transactional(readOnly = true)
    public java.util.List<MatrizResponse> buscarTodas() {
        return matrizRepository.findByCurso_Escola_Ativo(true)
                .stream()
                .map(MatrizResponse::new)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        Matriz matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz não encontrada: " + id));

        if (!matriz.getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Não é possível deletar uma matriz ligada a uma escola inativa.");
        }

        matrizRepository.delete(matriz);
    }


    public void verificaDuplicidade(Curso curso, Year anoVigencia, Long idAtual) {
        System.out.println("dwsdwdwdw");
        matrizRepository.findByCursoAndAnoVigencia(curso, anoVigencia)
                .filter(m -> idAtual == null || !m.getId().equals(idAtual))
                .ifPresent(m -> {
                    throw new IllegalArgumentException(
                            "Já existe uma matriz cadastrada para o curso '%s' no ano de vigência %s"
                                    .formatted(curso.getNome(), anoVigencia.getValue())
                    );
                });

        System.out.println("dwdwd");
    }

    public void verificaAtivadaEscola(Curso curso){
        if (!curso.getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Não é possível cadastrar matriz para curso vinculado " +
                    "a uma escola inativa: escola "+ curso.getEscola().getCategoriaEscola() +" esta inativa");
        }
    }
}
