package com.usc.sgdi.controller.rest;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.dto.DocumentoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

	private final ConsultarDocumentoService consultarDocumentoService;

	public DocumentoController(ConsultarDocumentoService consultarDocumentoService) {
		this.consultarDocumentoService = consultarDocumentoService;
	}

	@GetMapping("/{id_documento}")
	public ResponseEntity<DocumentoResponse> consultarPorId(
			@PathVariable("id_documento") Integer idDocumento) {
		if (idDocumento <= 0) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.of(consultarDocumentoService.consultarPorId(idDocumento));
	}
}
