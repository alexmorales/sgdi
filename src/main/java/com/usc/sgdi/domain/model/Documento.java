package com.usc.sgdi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa los metadatos de un documento y sus relaciones
 * con categoría, usuario y tipo documental.
 */
@Entity
@Table(name = "documentos")
public class Documento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_documento", nullable = false)
	private Integer idDocumento;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_categoria", nullable = false)
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@Column(name = "descripcion", nullable = false, length = 200)
	private String descripcion;

	@Column(name = "path", nullable = false, length = 200)
	private String path;

	@Column(name = "tamanio", nullable = false)
	private Long tamanio;

	@Column(name = "fecha_carga", nullable = false)
	private LocalDateTime fechaCarga;

	protected Documento() {
	}

	/**
	 * Crea una entidad documental con sus metadatos y relaciones obligatorias.
	 *
	 * @param categoria categoría asociada al documento
	 * @param usuario usuario asociado al documento
	 * @param tipoDocumento tipo documental asociado
	 * @param nombre nombre del documento
	 * @param descripcion descripción del documento
	 * @param path ruta registrada del documento
	 * @param tamanio tamaño del documento en bytes
	 * @param fechaCarga fecha y hora de carga
	 */
	public Documento(
			Categoria categoria,
			Usuario usuario,
			TipoDocumento tipoDocumento,
			String nombre,
			String descripcion,
			String path,
			Long tamanio,
			LocalDateTime fechaCarga) {
		this.categoria = categoria;
		this.usuario = usuario;
		this.tipoDocumento = tipoDocumento;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.path = path;
		this.tamanio = tamanio;
		this.fechaCarga = fechaCarga;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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

	public LocalDateTime getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(LocalDateTime fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
}
