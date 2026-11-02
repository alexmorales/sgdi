package com.usc.sgdi.controller.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.application.service.DocumentoReferenciaNoEncontradaException;
import com.usc.sgdi.dto.CrearDocumentoRequest;
import com.usc.sgdi.dto.DocumentoResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class DocumentoMvcControllerTest {

	@Mock
	private ConsultarDocumentoService consultarDocumentoService;

	private MockMvc mockMvc;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders.standaloneSetup(
				new DocumentoMvcController(consultarDocumentoService))
				.setValidator(validator)
				.build();
	}

	@AfterEach
	void tearDown() {
		validator.close();
	}

	@Test
	void muestraFormularioDeRegistro() throws Exception {
		mockMvc.perform(get("/documentos/nuevo"))
				.andExpect(status().isOk())
				.andExpect(view().name("documentos/registro"));
	}

	@Test
	void registroValidoRedirigeAlDetalleConMensaje() throws Exception {
		when(consultarDocumentoService.registrar(any(CrearDocumentoRequest.class)))
				.thenReturn(new DocumentoResponse(7, 2, 3, 4, "Acta", "Acta de reunión",
						"/uploads/acta.pdf", 1024L, LocalDateTime.of(2026, 10, 29, 12, 0)));

		mockMvc.perform(post("/documentos")
						.param("idCategoria", "2")
						.param("idUsuario", "3")
						.param("idTipoDocumento", "4")
						.param("nombre", "Acta")
						.param("descripcion", "Acta de reunión")
						.param("path", "/uploads/acta.pdf")
						.param("tamanio", "1024"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/documentos/7"))
				.andExpect(flash().attribute("mensajeExito", "Documento registrado correctamente."));
	}

	@Test
	void datosInvalidosVuelvenAlFormularioSinPersistir() throws Exception {
		mockMvc.perform(post("/documentos")
						.param("idCategoria", "0")
						.param("idUsuario", "3")
						.param("idTipoDocumento", "4")
						.param("nombre", "")
						.param("descripcion", "Acta")
						.param("path", "/uploads/acta.pdf")
						.param("tamanio", "-1"))
				.andExpect(status().isOk())
				.andExpect(view().name("documentos/registro"));

		verify(consultarDocumentoService, never()).registrar(any(CrearDocumentoRequest.class));
	}

	@Test
	void referenciaInexistenteVuelveAlFormularioConError() throws Exception {
		when(consultarDocumentoService.registrar(any(CrearDocumentoRequest.class)))
				.thenThrow(new DocumentoReferenciaNoEncontradaException("idCategoria", 99));

		mockMvc.perform(post("/documentos")
						.param("idCategoria", "99")
						.param("idUsuario", "3")
						.param("idTipoDocumento", "4")
						.param("nombre", "Acta")
						.param("descripcion", "Acta de reunión")
						.param("path", "/uploads/acta.pdf")
						.param("tamanio", "1024"))
				.andExpect(status().isOk())
				.andExpect(view().name("documentos/registro"));
	}
}
