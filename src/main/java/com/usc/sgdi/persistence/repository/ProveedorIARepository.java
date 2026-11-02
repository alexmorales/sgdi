package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.ProveedorIA;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link ProveedorIA}.
 */
public interface ProveedorIARepository extends JpaRepository<ProveedorIA, Integer> {
}
