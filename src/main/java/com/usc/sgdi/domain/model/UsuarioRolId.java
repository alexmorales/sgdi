package com.usc.sgdi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clave primaria compuesta de la asociación entre usuario y rol.
 */
@Embeddable
public class UsuarioRolId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_usuario", nullable = false)
	private Integer idUsuario;

	@Column(name = "id_rol", nullable = false)
	private Integer idRol;

	protected UsuarioRolId() {
	}

	/**
	 * Crea una clave compuesta para la pareja de identificadores indicada.
	 *
	 * @param idUsuario identificador del usuario
	 * @param idRol identificador del rol
	 */
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

	/**
	 * Compara esta clave con otra usando ambos identificadores.
	 *
	 * @param other objeto con el que se compara esta clave
	 * @return {@code true} si ambos identificadores coinciden; {@code false} en
	 *         caso contrario
	 */
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

	/**
	 * Calcula el código hash a partir de los identificadores de usuario y rol.
	 *
	 * @return código hash de esta clave compuesta
	 */
	@Override
	public int hashCode() {
		return Objects.hash(idUsuario, idRol);
	}
}
