package com.usc.sgdi.persistence.repository;

import com.usc.sgdi.domain.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
