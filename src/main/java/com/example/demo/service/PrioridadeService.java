package com.example.demo.service;

import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.dto.PrioridadeCreateRequest;
import com.example.demo.domain.prioridade.dto.PrioridadeResponse;
import com.example.demo.domain.prioridade.dto.PrioridadeUpdateRequest;
import com.example.demo.domain.professor.Professor;
import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.infra.Exception.PrioridadeJaExiste;
import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.repository.MatrizDisciplinaRepository;
import com.example.demo.repository.PrioridadeRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrioridadeService {

    private final PrioridadeRepository prioridadeRepository;
    private final MatrizDisciplinaRepository mdRepository;
    private final ProfessorRepository professorRepository;


    @Transactional
    public PrioridadeResponse criar(PrioridadeCreateRequest request) {
        MatrizDisciplina md = buscarMatrizDisciplina(request.matrizDisciplinaId());
        Professor professor = buscarProfessorEValidarAtividade(request.professorId());
        verificaAtividadeEscola(md);
        verificaDuplicidade(md,professor,null);



        Prioridade prioridade = new Prioridade(md, request.prioridade(), professor);
        prioridade = prioridadeRepository.save(prioridade);
        return new PrioridadeResponse(prioridade);
    }





    @Transactional
    public PrioridadeResponse atualizar(Long id, PrioridadeUpdateRequest request) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));

        MatrizDisciplina novaMd = prioridade.getMatrizDisciplina();
        Professor novoProfessor = prioridade.getProfessor(); // Começa com o professor atual

        boolean mudancaRelevante = false;

        if (request.matrizDisciplinaId() != null && !request.matrizDisciplinaId().equals(novaMd.getId())) {
            novaMd = buscarMatrizDisciplina(request.matrizDisciplinaId());
            verificaAtividadeEscola(novaMd);
            mudancaRelevante = true;
        }

        if (request.professorId() != null && !request.professorId().equals(novoProfessor.getId())) {
            novoProfessor = buscarProfessorEValidarAtividade(request.professorId());
            mudancaRelevante = true;
        }

        if (mudancaRelevante) {
            verificaDuplicidade(novaMd, novoProfessor, id);
        }

        // 4. Aplica as mudanças
        prioridade.setMatrizDisciplina(novaMd);
        prioridade.setProfessor(novoProfessor);
        Optional.ofNullable(request.prioridade()).ifPresent(prioridade::setPrioridade);

        prioridade = prioridadeRepository.save(prioridade);
        return new PrioridadeResponse(prioridade);
    }


    @Transactional(readOnly = true)
    public PrioridadeResponse buscarPorId(Long id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));


        verificaAtividadeEscola(prioridade.getMatrizDisciplina());

        return new PrioridadeResponse(prioridade);
    }


    @Transactional(readOnly = true)
    public List<PrioridadeResponse> buscarTodos() {
        // Validação 6: Filtra no Banco de Dados para garantir performance, retornando APENAS as ativas.
        return prioridadeRepository.findByMatrizDisciplina_Matriz_Curso_Escola_Ativo(true).stream()
                .map(PrioridadeResponse::new)
                .toList();
    }


    @Transactional
    public void deletar(Long id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));


        verificaAtividadeEscola(prioridade.getMatrizDisciplina());

        prioridadeRepository.delete(prioridade);
    }



    private MatrizDisciplina buscarMatrizDisciplina(Long id) {
        return mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz Disciplina não encontrada com ID: " + id));
    }

    private Professor buscarProfessorEValidarAtividade(Long id) {
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + id)) ;
           if(!professor.isAtivo() ) throw  new ProfessorInativoException("Professor inativo com ID: " + professor.getId());
        return  professor;
    }



    private void verificaAtividadeEscola(MatrizDisciplina md) {
        if (!md.getMatriz().getCurso().getEscola().isAtivo()) {
            CategoriaEscola escolaNome = md.getMatriz().getCurso().getEscola().getCategoriaEscola();
            String matrizNome = md.getMatriz().getNome();
            throw new EscolaInativaExeption(
                "Operação bloqueada: A prioridade está ligada à matriz '%s' de uma escola inativa: %s."
                .formatted(matrizNome, escolaNome)
            );
        }
    }
    private void verificaDuplicidade(MatrizDisciplina md , Professor pr,Long id){
        prioridadeRepository.findByMatrizDisciplinaAndProfessor(md,pr)
                .filter(p ->p.getId() == null ||  !p.getId().equals(id))
                .ifPresent(p -> {
                    // Trocar IllegalArgumentException pela exceção de negócio específica
                    throw new PrioridadeJaExiste("Já existe uma prioridade definida para esta nova combinação de Professor e Matriz Disciplina.");
                });
    }

}