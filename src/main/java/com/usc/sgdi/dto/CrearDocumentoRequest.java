package com.usc.sgdi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para registrar los metadatos de un documento.
 *
 * <p>La fecha de carga y el identificador del documento son asignados por el
 * servidor.</p>
 */
public class CrearDocumentoRequest {

	@JsonProperty("id_categoria")
	@NotNull
	@Positive
	private Integer idCategoria;

	@JsonProperty("id_usuario")
	@NotNull
	@Positive
	private Integer idUsuario;

	@JsonProperty("id_tipo_documento")
	@NotNull
	@Positive
	private Integer idTipoDocumento;

	@NotBlank
	@Size(max = 50)
	private String nombre;

	@NotBlank
	@Size(max = 200)
	private String descripcion;

	@NotBlank
	@Size(max = 200)
	private String path;

	@NotNull
	@PositiveOrZero
	private Long tamanio;

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getTamanio() {
		return tamanio;
	}

	public void setTamanio(Long tamanio) {
		this.tamanio = tamanio;
	}
}
