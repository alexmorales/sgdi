package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link TipoDocumento}.
 */
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
}
