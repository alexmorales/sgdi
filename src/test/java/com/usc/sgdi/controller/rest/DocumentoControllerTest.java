package com.usc.sgdi.controller.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.dto.DocumentoResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DocumentoControllerTest {

	@Mock
	private ConsultarDocumentoService consultarDocumentoService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(
				new DocumentoController(consultarDocumentoService)).build();
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
}
