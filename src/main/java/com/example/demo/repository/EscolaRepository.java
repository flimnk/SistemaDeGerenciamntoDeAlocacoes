package com.example.demo.repository;

import com.example.demo.domain.escola.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    boolean existsByCategoriaEscola();
}