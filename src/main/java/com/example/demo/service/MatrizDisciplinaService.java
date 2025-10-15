package com.example.demo.service;

import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaCreateRequest;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponse;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaUpdateRequest;
import com.example.demo.infra.Exception.DisciplinaInativaException;
import com.example.demo.infra.Exception.DisclinaJaEstaAlocadaNessaMatiz;
import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.repository.DisciplinaRepository;
import com.example.demo.repository.MatrizDisciplinaRepository;
import com.example.demo.repository.MatrizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatrizDisciplinaService {

    private final MatrizDisciplinaRepository mdRepository;
    private final MatrizRepository matrizRepository;
    private final DisciplinaRepository disciplinaRepository;


    @Transactional
    public MatrizDisciplinaResponse criar(MatrizDisciplinaCreateRequest request) {
        Matriz matriz =  buscarMatriz(request.matrizId());
        Disciplina disciplina = buscarDisciplina(request.disciplinaId());
        verificaDuplicidade(matriz,disciplina,null);


        MatrizDisciplina md = new MatrizDisciplina(
                matriz,
                disciplina,
                request.cargaHoraria(),
                request.semestre(),
                request.obrigatoria()
        );

        mdRepository.save(md);

        return new MatrizDisciplinaResponse(md);
    }

    @Transactional
    public MatrizDisciplinaResponse atualizar(Long id, MatrizDisciplinaUpdateRequest request) {
        MatrizDisciplina md = mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatrizDisciplina não encontrada: " + id));

        Matriz matriz = request.matrizId() != null ?
                 buscarMatriz(request.matrizId()) :
                    md.getMatriz();


        Disciplina disciplina = request.disciplinaId() != null ?
                buscarDisciplina(request.disciplinaId()) :
                    md.getDisciplina();


        verificaDuplicidade(matriz , disciplina,id);
        if (request.cargaHoraria() != null) md.setCargaHoraria(request.cargaHoraria());
        if (request.semestre() != null) md.setSemestre(request.semestre());
        if (request.obrigatoria() != null) md.setObrigatoria(request.obrigatoria());
        md.setDisciplina(disciplina);
        md.setMatriz(matriz);

        mdRepository.save(md);
        return new MatrizDisciplinaResponse(md);
    }

    @Transactional(readOnly = true)
    public MatrizDisciplinaResponse buscarPorId(Long id) {
        MatrizDisciplina md = mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatrizDisciplina não encontrada: " + id));




        verificaAtivadeEscola("A Matriz Disciplina está ligada a uma escola inativa.",md);


        return new MatrizDisciplinaResponse(md);
    }
    @Transactional(readOnly = true)
    public List<MatrizDisciplinaResponse> buscarTodos() {
        return mdRepository.findByMatriz_Curso_Escola_AtivoTrueAndDisciplina_AtivoTrue()
                .stream()
                .map(MatrizDisciplinaResponse::new)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        MatrizDisciplina md = mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MatrizDisciplina não encontrada: " + id));

        verificaAtivadeEscola("Não é possível deletar uma Matriz Disciplina ligada a uma escola inativa.",md);


        mdRepository.delete(md);
    }

    public void  verificaDuplicidade(Matriz matriz ,Disciplina disciplina ,Long id ){
        mdRepository.findByMatrizAndDisciplina(matriz,disciplina)
                .filter(existing -> existing.getId() == null || !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DisclinaJaEstaAlocadaNessaMatiz("Disciplina  já adicionada nessa matriz");
                });
    }

    public void verificaAtivadeEscola(String msg, MatrizDisciplina md){
        if (!md.getMatriz().getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption(msg);
        }

    }


    private Matriz buscarMatriz( Long matrizId) {
        Matriz matriz = matrizRepository.findById(matrizId)
                .orElseThrow(() -> new EntityNotFoundException("Matriz não encontrada com id: " + matrizId));


        if (!matriz.getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption("A Matriz (" + matriz.getNome() + ") está ligada a uma escola inativa.");
        }

        return matriz;
    }
    private Disciplina buscarDisciplina( Long disciplinaId) {
        var disciplina =  disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada com id: " + disciplinaId));
        if(!disciplina.isAtivo()) throw new DisciplinaInativaException("Disciplina inativa com id: " + disciplina.getId());
        return  disciplina;
    }

}
