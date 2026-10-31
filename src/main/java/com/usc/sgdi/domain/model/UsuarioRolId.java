package com.usc.sgdi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioRolId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_usuario", nullable = false)
	private Integer idUsuario;

	@Column(name = "id_rol", nullable = false)
	private Integer idRol;

	protected UsuarioRolId() {
	}

	public UsuarioRolId(Integer idUsuario, Integer idRol) {
		this.idUsuario = idUsuario;
		this.idRol = idRol;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdRol() {
		return idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UsuarioRolId that)) {
			return false;
		}
		return Objects.equals(idUsuario, that.idUsuario)
				&& Objects.equals(idRol, that.idRol);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUsuario, idRol);
	}
}
