package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para recuperar y persistir entidades {@link Documento}.
 *
 * <p>Hereda las operaciones estándar de {@link JpaRepository}; no declara
 * consultas específicas adicionales.</p>
 */
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
}
