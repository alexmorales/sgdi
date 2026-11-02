package com.usc.sgdi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Datos de un documento expuestos por la consulta REST y las vistas MVC.
 *
 * @param idDocumento identificador del documento
 * @param idCategoria identificador de la categoría asociada
 * @param idUsuario identificador del usuario asociado
 * @param idTipoDocumento identificador del tipo de documento
 * @param nombre nombre del documento
 * @param descripcion descripción del documento
 * @param path ruta almacenada para el documento
 * @param tamanio tamaño del documento en bytes
 * @param fechaCarga fecha y hora de carga del documento
 */
public record DocumentoResponse(
		@JsonProperty("id_documento") Integer idDocumento,
		@JsonProperty("id_categoria") Integer idCategoria,
		@JsonProperty("id_usuario") Integer idUsuario,
		@JsonProperty("id_tipo_documento") Integer idTipoDocumento,
		String nombre,
		String descripcion,
		String path,
		Long tamanio,
		@JsonProperty("fecha_carga") LocalDateTime fechaCarga) {
}
