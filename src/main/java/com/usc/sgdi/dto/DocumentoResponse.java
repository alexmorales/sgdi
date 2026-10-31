package com.usc.sgdi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

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
