package com.usc.sgdi.application.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usc.sgdi.domain.model.Categoria;
import com.usc.sgdi.domain.model.Documento;
import com.usc.sgdi.domain.model.TipoDocumento;
import com.usc.sgdi.domain.model.Usuario;
import com.usc.sgdi.dto.CrearDocumentoRequest;
import com.usc.sgdi.dto.DocumentoResponse;
import com.usc.sgdi.persistence.repository.CategoriaRepository;
import com.usc.sgdi.persistence.repository.DocumentoRepository;
import com.usc.sgdi.persistence.repository.TipoDocumentoRepository;
import com.usc.sgdi.persistence.repository.UsuarioRepository;

/**
 * Proporciona operaciones de consulta de documentos y transforma entidades
 * persistentes al DTO utilizado por las interfaces REST y MVC.
 */
@Service
public class ConsultarDocumentoService {

	private final DocumentoRepository documentoRepository;
	private final CategoriaRepository categoriaRepository;
	private final UsuarioRepository usuarioRepository;
	private final TipoDocumentoRepository tipoDocumentoRepository;

	/**
	 * Crea el servicio con el repositorio de documentos.
	 *
	 * @param documentoRepository repositorio de documentos
	 * @param categoriaRepository repositorio de categorías asociadas
	 * @param usuarioRepository repositorio de usuarios asociados
	 * @param tipoDocumentoRepository repositorio de tipos documentales asociados
	 */
	public ConsultarDocumentoService(
			DocumentoRepository documentoRepository,
			CategoriaRepository categoriaRepository,
			UsuarioRepository usuarioRepository,
			TipoDocumentoRepository tipoDocumentoRepository) {
		this.documentoRepository = documentoRepository;
		this.categoriaRepository = categoriaRepository;
		this.usuarioRepository = usuarioRepository;
		this.tipoDocumentoRepository = tipoDocumentoRepository;
	}

	/**
	 * Busca un documento por identificador y convierte el resultado a respuesta.
	 *
	 * @param idDocumento identificador del documento que se desea consultar
	 * @return respuesta con los datos del documento, o {@link Optional#empty()}
	 *         cuando no existe
	 */
	@Transactional(readOnly = true)
	public Optional<DocumentoResponse> consultarPorId(Integer idDocumento) {
		return documentoRepository.findById(idDocumento).map(this::toResponse);
	}

	/**
	 * Registra los metadatos de un documento y sus relaciones existentes.
	 *
	 * @param request datos validados para el documento
	 * @return los datos del documento persistido, incluido su identificador generado
	 * @throws DocumentoReferenciaNoEncontradaException si una categoría, usuario o
	 *         tipo documental referenciado no existe
	 */
	@Transactional
	public DocumentoResponse registrar(CrearDocumentoRequest request) {
		Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
				.orElseThrow(() -> new DocumentoReferenciaNoEncontradaException(
						"idCategoria", request.getIdCategoria()));
		Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
				.orElseThrow(() -> new DocumentoReferenciaNoEncontradaException(
						"idUsuario", request.getIdUsuario()));
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.getIdTipoDocumento())
				.orElseThrow(() -> new DocumentoReferenciaNoEncontradaException(
						"idTipoDocumento", request.getIdTipoDocumento()));

		Documento documento = new Documento(
				categoria,
				usuario,
				tipoDocumento,
				request.getNombre(),
				request.getDescripcion(),
				request.getPath(),
				request.getTamanio(),
				LocalDateTime.now(ZoneOffset.UTC));

		return toResponse(documentoRepository.save(documento));
	}

	/**
	 * Recupera todos los documentos y convierte cada entidad a respuesta.
	 *
	 * @return lista de documentos; vacía si el repositorio no contiene registros
	 */
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
