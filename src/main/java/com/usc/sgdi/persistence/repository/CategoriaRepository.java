package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
