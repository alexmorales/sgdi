package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link Categoria}.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
