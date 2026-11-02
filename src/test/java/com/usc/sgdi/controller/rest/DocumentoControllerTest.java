package com.usc.sgdi.controller.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.application.service.DocumentoReferenciaNoEncontradaException;
import com.usc.sgdi.dto.DocumentoResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class DocumentoControllerTest {

	@Mock
	private ConsultarDocumentoService consultarDocumentoService;

	private MockMvc mockMvc;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders.standaloneSetup(
				new DocumentoController(consultarDocumentoService))
				.setValidator(validator)
				.build();
	}

	@AfterEach
	void tearDown() {
		validator.close();
	}

	@Test
	void consultaDocumentoExistenteDevuelve200YDto() throws Exception {
		when(consultarDocumentoService.consultarPorId(7)).thenReturn(Optional.of(
				new DocumentoResponse(7, 2, 3, 4, "Acta", "Acta de reunión",
						"/uploads/acta.pdf", 1024L, LocalDateTime.of(2026, 10, 29, 12, 0))));

		mockMvc.perform(get("/api/documentos/7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id_documento").value(7))
				.andExpect(jsonPath("$.id_categoria").value(2))
				.andExpect(jsonPath("$.path").value("/uploads/acta.pdf"));
	}

	@Test
	void consultaDocumentoInexistenteDevuelve404() throws Exception {
		when(consultarDocumentoService.consultarPorId(99)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/documentos/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void identificadorNoPositivoDevuelve400() throws Exception {
		mockMvc.perform(get("/api/documentos/0"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void identificadorNoNumericoDevuelve400() throws Exception {
		mockMvc.perform(get("/api/documentos/no-es-numero"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void registroValidoDevuelve201LocationYDocumento() throws Exception {
		when(consultarDocumentoService.registrar(any())).thenReturn(
				new DocumentoResponse(7, 2, 3, 4, "Acta", "Acta de reunión",
						"/uploads/acta.pdf", 1024L, LocalDateTime.of(2026, 10, 29, 12, 0)));

		mockMvc.perform(post("/api/documentos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id_categoria": 2,
								  "id_usuario": 3,
								  "id_tipo_documento": 4,
								  "nombre": "Acta",
								  "descripcion": "Acta de reunión",
								  "path": "/uploads/acta.pdf",
								  "tamanio": 1024
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/api/documentos/7"))
				.andExpect(jsonPath("$.id_documento").value(7))
				.andExpect(jsonPath("$.id_categoria").value(2));
	}

	@Test
	void registroInvalidoDevuelve400SinInvocarServicio() throws Exception {
		mockMvc.perform(post("/api/documentos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id_categoria": 0,
								  "id_usuario": 3,
								  "id_tipo_documento": 4,
								  "nombre": "",
								  "descripcion": "Acta",
								  "path": "/uploads/acta.pdf",
								  "tamanio": -1
								}
								"""))
				.andExpect(status().isBadRequest());

		verify(consultarDocumentoService, never()).registrar(any());
	}

	@Test
	void referenciaInexistenteDevuelve404SinCuerpo() throws Exception {
		when(consultarDocumentoService.registrar(any()))
				.thenThrow(new DocumentoReferenciaNoEncontradaException("idCategoria", 99));

		mockMvc.perform(post("/api/documentos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id_categoria": 99,
								  "id_usuario": 3,
								  "id_tipo_documento": 4,
								  "nombre": "Acta",
								  "descripcion": "Acta de reunión",
								  "path": "/uploads/acta.pdf",
								  "tamanio": 1024
								}
								"""))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}
}
