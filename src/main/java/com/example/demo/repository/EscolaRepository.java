package com.example.demo.repository;

import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.escola.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    boolean existsByCategoriaEscola(CategoriaEscola categoriaEscola);
    List<Escola> findAllByAtivoTrue();

}