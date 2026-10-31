package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
}
