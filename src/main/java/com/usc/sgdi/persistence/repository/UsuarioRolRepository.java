package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.UsuarioRol;
import com.usc.sgdi.domain.model.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}
