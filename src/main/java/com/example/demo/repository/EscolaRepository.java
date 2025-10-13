package com.example.demo.repository;

import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.escola.Escola;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    boolean existsByCategoriaEscola(CategoriaEscola categoriaEscola);
    List<Escola> findAllByAtivoTrue();


}