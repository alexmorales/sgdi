package com.usc.sgdi.controller.mvc;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/documentos")
public class DocumentoMvcController {

	private final ConsultarDocumentoService consultarDocumentoService;

	public DocumentoMvcController(ConsultarDocumentoService consultarDocumentoService) {
		this.consultarDocumentoService = consultarDocumentoService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("documentos", consultarDocumentoService.listarTodos());
		return "documentos/listado";
	}

	@GetMapping("/{id_documento}")
	public ModelAndView consultarPorId(@PathVariable("id_documento") Integer idDocumento) {
		return consultarDocumentoService.consultarPorId(idDocumento)
				.map(documento -> new ModelAndView("documentos/detalle")
						.addObject("documento", documento))
				.orElseGet(() -> new ModelAndView("documentos/no-encontrado", HttpStatus.NOT_FOUND));
	}
}
