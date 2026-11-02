package com.usc.sgdi.controller.mvc;

import com.usc.sgdi.application.service.ConsultarDocumentoService;
import com.usc.sgdi.application.service.DocumentoReferenciaNoEncontradaException;
import com.usc.sgdi.dto.CrearDocumentoRequest;
import com.usc.sgdi.dto.DocumentoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Atiende las rutas MVC para registrar, listar y consultar documentos en
 * vistas Thymeleaf.
 */
@Controller
@RequestMapping("/documentos")
public class DocumentoMvcController {

	private final ConsultarDocumentoService consultarDocumentoService;

	/**
	 * Crea el controlador MVC con el servicio de consulta documental.
	 *
	 * @param consultarDocumentoService servicio que registra y obtiene documentos
	 */
	public DocumentoMvcController(ConsultarDocumentoService consultarDocumentoService) {
		this.consultarDocumentoService = consultarDocumentoService;
	}

	/**
	 * Obtiene todos los documentos y los expone a la vista de listado.
	 *
	 * @param model modelo MVC al que se agrega el atributo {@code documentos}
	 * @return nombre lógico de la vista de listado
	 */
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("documentos", consultarDocumentoService.listarTodos());
		return "documentos/listado";
	}

	/**
	 * Muestra el formulario para registrar metadatos de un documento.
	 *
	 * @param model modelo al que se agrega el formulario vacío
	 * @return nombre lógico de la vista de registro
	 */
	@GetMapping("/nuevo")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("nuevoDocumento", new CrearDocumentoRequest());
		return "documentos/registro";
	}

	/**
	 * Valida y registra metadatos desde el formulario MVC.
	 *
	 * @param request datos enviados por el formulario
	 * @param bindingResult errores de conversión o validación
	 * @param redirectAttributes atributos flash para el resultado del registro
	 * @return formulario cuando hay errores, o redirección al detalle al completar
	 *         el registro
	 */
	@PostMapping
	public String registrar(
			@Valid @ModelAttribute("nuevoDocumento") CrearDocumentoRequest request,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "documentos/registro";
		}

		DocumentoResponse documento;
		try {
			documento = consultarDocumentoService.registrar(request);
		} catch (DocumentoReferenciaNoEncontradaException exception) {
			bindingResult.rejectValue(exception.getFieldName(), "referencia.noEncontrada",
					exception.getMessage());
			return "documentos/registro";
		}

		redirectAttributes.addFlashAttribute("mensajeExito",
				"Documento registrado correctamente.");
		return "redirect:/documentos/" + documento.idDocumento();
	}

	/**
	 * Busca un documento y selecciona la vista correspondiente a su resultado.
	 *
	 * @param idDocumento identificador del documento solicitado
	 * @return vista de detalle con el atributo {@code documento}, o vista de no
	 *         encontrado con estado HTTP {@code 404}
	 */
	@GetMapping("/{id_documento}")
	public ModelAndView consultarPorId(@PathVariable("id_documento") Integer idDocumento) {
		return consultarDocumentoService.consultarPorId(idDocumento)
				.map(documento -> new ModelAndView("documentos/detalle")
						.addObject("documento", documento))
				.orElseGet(() -> new ModelAndView("documentos/no-encontrado", HttpStatus.NOT_FOUND));
	}
}
