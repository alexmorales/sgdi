package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
