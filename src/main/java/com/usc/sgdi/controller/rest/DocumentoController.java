package com.usc.sgdi.controller.rest;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.application.service.DocumentoReferenciaNoEncontradaException;
import com.usc.sgdi.dto.CrearDocumentoRequest;
import com.usc.sgdi.dto.DocumentoResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone operaciones REST para registrar y consultar documentos.
 */
@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

	private final ConsultarDocumentoService consultarDocumentoService;

	/**
	 * Crea el controlador con el servicio de documentos.
	 *
	 * @param consultarDocumentoService servicio que obtiene los datos del documento
	 */
	public DocumentoController(ConsultarDocumentoService consultarDocumentoService) {
		this.consultarDocumentoService = consultarDocumentoService;
	}

	/**
	 * Consulta un documento por su identificador.
	 *
	 * @param idDocumento identificador del documento solicitado
	 * @return respuesta con el documento si existe, o sin cuerpo con estado
	 *         {@code 400} para identificadores no positivos y {@code 404} si no existe
	 */
	@GetMapping("/{id_documento}")
	public ResponseEntity<DocumentoResponse> consultarPorId(
			@PathVariable("id_documento") Integer idDocumento) {
		if (idDocumento <= 0) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.of(consultarDocumentoService.consultarPorId(idDocumento));
	}

	/**
	 * Registra los metadatos de un documento.
	 *
	 * @param request datos de entrada validados
	 * @return respuesta {@code 201 Created}, con el documento creado y su URI de
	 *         consulta en la cabecera {@code Location}
	 */
	@PostMapping
	public ResponseEntity<DocumentoResponse> registrar(
			@Valid @RequestBody CrearDocumentoRequest request) {
		DocumentoResponse documento = consultarDocumentoService.registrar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id_documento}")
				.buildAndExpand(documento.idDocumento())
				.toUri();
		return ResponseEntity.created(location).body(documento);
	}

	/**
	 * Convierte una referencia asociada inexistente en una respuesta sin cuerpo.
	 *
	 * @return respuesta HTTP {@code 404 Not Found}
	 */
	@ExceptionHandler(DocumentoReferenciaNoEncontradaException.class)
	public ResponseEntity<Void> referenciaNoEncontrada() {
		return ResponseEntity.notFound().build();
	}
}
