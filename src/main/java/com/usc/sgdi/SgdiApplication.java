package com.usc.sgdi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot del SGDI.
 */
@SpringBootApplication
public class SgdiApplication {

	/**
	 * Inicia el contexto de Spring y la aplicación web.
	 *
	 * @param args argumentos recibidos al iniciar el proceso
	 */
	public static void main(String[] args) {
		SpringApplication.run(SgdiApplication.class, args);
	}

}
