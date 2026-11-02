package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link Rol}.
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {
}
