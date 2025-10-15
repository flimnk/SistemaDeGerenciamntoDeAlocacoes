package com.example.demo.service;

import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.escola.dto.EscolaCreateRequest;
import com.example.demo.domain.escola.dto.EscolaResponse;
import com.example.demo.domain.escola.dto.EscolaUpdateRequest;

import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.infra.Exception.EscolaJaExisteExcption;
import com.example.demo.repository.EscolaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EscolaService {

    private final EscolaRepository escolaRepository;

    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    @Transactional
    public EscolaResponse criar(EscolaCreateRequest request) {
        if(escolaRepository.existsByCategoriaEscola(request.categoriaEscola())) throw  new EscolaJaExisteExcption("Escola com a categoria ja existe: " + request.categoriaEscola());
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
    public EscolaResponse atualizar(Long id, EscolaUpdateRequest request) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));

        if(!escola.isAtivo()) throw  new EscolaInativaExeption("Escola inativa com ID: " + id);


        if (request.categoriaEscola() != null && !escolaRepository.existsByCategoriaEscola( request.categoriaEscola())) {
            escola.setCategoriaEscola(request.categoriaEscola());
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
    // 🔹 Buscar todas as ativas (lista simples)
    @Transactional(readOnly = true)
    public List<EscolaResponse> buscarAtivas() {
        return escolaRepository.findAllByAtivoTrue().stream()
                .map(EscolaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void desativar(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada"));

        escola.desativar();



        escolaRepository.save(escola);
    }




    // 🔹 Ativar
    @Transactional
    public void ativar(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));
        escola.ativar();
        escolaRepository.save(escola);
    }
}