package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.ClasificacionIA;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link ClasificacionIA}.
 */
public interface ClasificacionIARepository extends JpaRepository<ClasificacionIA, Integer> {
}
