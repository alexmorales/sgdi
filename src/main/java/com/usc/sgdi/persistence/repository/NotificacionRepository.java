package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data para operaciones estándar sobre {@link Notificacion}.
 */
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
