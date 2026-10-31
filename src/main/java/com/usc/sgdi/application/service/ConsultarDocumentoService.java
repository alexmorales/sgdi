package com.usc.sgdi.application.service;

import com.usc.sgdi.domain.model.Documento;
import com.usc.sgdi.dto.DocumentoResponse;
import com.usc.sgdi.persistence.repository.DocumentoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultarDocumentoService {

	private final DocumentoRepository documentoRepository;

	public ConsultarDocumentoService(DocumentoRepository documentoRepository) {
		this.documentoRepository = documentoRepository;
	}

	@Transactional(readOnly = true)
	public Optional<DocumentoResponse> consultarPorId(Integer idDocumento) {
		return documentoRepository.findById(idDocumento).map(this::toResponse);
	}

	@Transactional(readOnly = true)
	public List<DocumentoResponse> listarTodos() {
		return documentoRepository.findAll().stream().map(this::toResponse).toList();
	}

	private DocumentoResponse toResponse(Documento documento) {
		return new DocumentoResponse(
				documento.getIdDocumento(),
				documento.getCategoria().getIdCategoria(),
				documento.getUsuario().getIdUsuario(),
				documento.getTipoDocumento().getIdTipoDocumento(),
				documento.getNombre(),
				documento.getDescripcion(),
				documento.getPath(),
				documento.getTamanio(),
				documento.getFechaCarga());
	}
}
