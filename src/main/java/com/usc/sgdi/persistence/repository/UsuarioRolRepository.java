package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.UsuarioRol;
import com.usc.sgdi.domain.model.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link UsuarioRol},
 * cuya clave primaria se representa mediante {@link UsuarioRolId}.
 */
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}
