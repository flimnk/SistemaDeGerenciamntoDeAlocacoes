package com.example.demo.service;

import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.escola.dto.EscolaCreateRequest;
import com.example.demo.domain.escola.dto.EscolaResponse;
import com.example.demo.domain.escola.dto.EscolaUpdateRequest;

import com.example.demo.infra.Exception.EscolaJaExisteExcption;
import com.example.demo.repository.EscolaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EscolaService {

    private final EscolaRepository escolaRepository;

    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    @Transactional
    public EscolaResponse criar(EscolaCreateRequest request) {
        if(escolaRepository.existsByCategoriaEscola()) throw  new EscolaJaExisteExcption("Escola com a categoria ja existe: " + request.categoriaEscola());
        Escola escola = new Escola(request.categoriaEscola());
        escola = escolaRepository.save(escola);
        return new EscolaResponse(escola);
    }

    @Transactional(readOnly = true)
    public Page<EscolaResponse> buscarTodos(Pageable pageable) {
        return escolaRepository.findAll(pageable)
                .map(EscolaResponse::new);
    }

    @Transactional(readOnly = true)
    public EscolaResponse buscarPorId(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));
        return new EscolaResponse(escola);
    }

    @Transactional
    public EscolaResponse atualizar(Long id, EscolaUpdateRequest dto) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));


        if (dto.categoriaEscola() != null && !escolaRepository.existsByCategoriaEscola()) {
            escola.setCategoriaEscola(dto.categoriaEscola());
        }



        escola = escolaRepository.save(escola);
        return new EscolaResponse(escola);
    }

    @Transactional
    public void deletar(Long id) {

        if (!escolaRepository.existsById(id)) {
            throw new EntityNotFoundException("Escola não encontrada com ID: " + id);
        }
        escolaRepository.deleteById(id);
    }
}