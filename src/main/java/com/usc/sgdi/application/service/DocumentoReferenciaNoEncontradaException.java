package com.usc.sgdi.application.service;

/**
 * Indica que una referencia obligatoria del documento no existe.
 */
public class DocumentoReferenciaNoEncontradaException extends RuntimeException {

	private final String fieldName;

	/**
	 * Crea la excepción para una referencia documental que no existe.
	 *
	 * @param fieldName nombre de la propiedad de entrada asociada a la referencia
	 * @param id identificador de la referencia inexistente
	 */
	public DocumentoReferenciaNoEncontradaException(String fieldName, Integer id) {
		super("No existe la referencia " + fieldName + " con identificador " + id + ".");
		this.fieldName = fieldName;
	}

	/**
	 * Devuelve el nombre de la propiedad de entrada que contiene la referencia.
	 *
	 * @return nombre de la propiedad
	 */
	public String getFieldName() {
		return fieldName;
	}
}
