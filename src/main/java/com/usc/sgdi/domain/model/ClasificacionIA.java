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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clasificacion_ia")
public class ClasificacionIA {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_clasificacion", nullable = false)
	private Integer idClasificacion;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_documento", nullable = false)
	private Documento documento;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_proveedor", nullable = false)
	private ProveedorIA proveedor;

	@Column(name = "categoria_sugerida", nullable = false, length = 200)
	private String categoriaSugerida;

	@Column(name = "confianza", nullable = false, precision = 5, scale = 2)
	private BigDecimal confianza;

	@Column(name = "observaciones", nullable = false, length = 200)
	private String observaciones;

	@Column(name = "fecha_clasificacion", nullable = false)
	private LocalDateTime fechaClasificacion;

	protected ClasificacionIA() {
	}

	public Integer getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(Integer idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public ProveedorIA getProveedor() {
		return proveedor;
	}

	public void setProveedor(ProveedorIA proveedor) {
		this.proveedor = proveedor;
	}

	public String getCategoriaSugerida() {
		return categoriaSugerida;
	}

	public void setCategoriaSugerida(String categoriaSugerida) {
		this.categoriaSugerida = categoriaSugerida;
	}

	public BigDecimal getConfianza() {
		return confianza;
	}

	public void setConfianza(BigDecimal confianza) {
		this.confianza = confianza;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public LocalDateTime getFechaClasificacion() {
		return fechaClasificacion;
	}

	public void setFechaClasificacion(LocalDateTime fechaClasificacion) {
		this.fechaClasificacion = fechaClasificacion;
	}
}
