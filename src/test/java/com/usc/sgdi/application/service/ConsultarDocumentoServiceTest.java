package com.usc.sgdi.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsultarDocumentoServiceTest {

	@Mock
	private DocumentoRepository documentoRepository;

	@Mock
	private CategoriaRepository categoriaRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private TipoDocumentoRepository tipoDocumentoRepository;

	private ConsultarDocumentoService service;

	@BeforeEach
	void setUp() {
		service = new ConsultarDocumentoService(
				documentoRepository, categoriaRepository, usuarioRepository, tipoDocumentoRepository);
	}

	@Test
	void registraDocumentoConRelacionesYFechaGenerada() {
		Categoria categoria = mock(Categoria.class);
		when(categoria.getIdCategoria()).thenReturn(2);
		Usuario usuario = mock(Usuario.class);
		when(usuario.getIdUsuario()).thenReturn(3);
		TipoDocumento tipoDocumento = mock(TipoDocumento.class);
		when(tipoDocumento.getIdTipoDocumento()).thenReturn(4);
		when(categoriaRepository.findById(2)).thenReturn(Optional.of(categoria));
		when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario));
		when(tipoDocumentoRepository.findById(4)).thenReturn(Optional.of(tipoDocumento));
		when(documentoRepository.save(any(Documento.class))).thenAnswer(invocation -> {
			Documento documento = invocation.getArgument(0);
			documento.setIdDocumento(7);
			return documento;
		});

		DocumentoResponse response = service.registrar(documentoRequest());

		assertEquals(7, response.idDocumento());
		assertEquals(2, response.idCategoria());
		assertEquals(3, response.idUsuario());
		assertEquals(4, response.idTipoDocumento());
		assertNotNull(response.fechaCarga());

		ArgumentCaptor<Documento> documentoCaptor = ArgumentCaptor.forClass(Documento.class);
		verify(documentoRepository).save(documentoCaptor.capture());
		assertEquals("Acta", documentoCaptor.getValue().getNombre());
		assertEquals("/uploads/acta.pdf", documentoCaptor.getValue().getPath());
		assertEquals(1024L, documentoCaptor.getValue().getTamanio());
	}

	@Test
	void noPersisteCuandoLaCategoriaNoExiste() {
		when(categoriaRepository.findById(99)).thenReturn(Optional.empty());
		CrearDocumentoRequest request = documentoRequest();
		request.setIdCategoria(99);

		DocumentoReferenciaNoEncontradaException exception = assertThrows(
				DocumentoReferenciaNoEncontradaException.class, () -> service.registrar(request));

		assertEquals("idCategoria", exception.getFieldName());
		verify(documentoRepository, never()).save(any(Documento.class));
	}

	private CrearDocumentoRequest documentoRequest() {
		CrearDocumentoRequest request = new CrearDocumentoRequest();
		request.setIdCategoria(2);
		request.setIdUsuario(3);
		request.setIdTipoDocumento(4);
		request.setNombre("Acta");
		request.setDescripcion("Acta de reunión");
		request.setPath("/uploads/acta.pdf");
		request.setTamanio(1024L);
		return request;
	}
}
