package com.ejemplo.cuentabancaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar el error cuando una cuenta bancaria
 * no es encontrada en la base de datos.
 *
 * Esta clase extiende RuntimeException, lo que significa que no es necesario
 * declararla en la firma de los métodos, simplificando el código.
 *
 * La anotación @ResponseStatus le indica a Spring Boot que cuando esta excepción
 * sea lanzada, debe responder con el código de estado HTTP 404 (NOT_FOUND).
 * Este es el estándar REST para indicar que el recurso solicitado no existe,
 * lo que proporciona una respuesta clara y semántica a los clientes de la API.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CuentaNoEncontradaException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje detallado del error.
     */
    public CuentaNoEncontradaException(String message) {
        super(message);
    }
}