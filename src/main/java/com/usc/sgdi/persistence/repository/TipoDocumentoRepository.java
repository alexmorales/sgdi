package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
}
