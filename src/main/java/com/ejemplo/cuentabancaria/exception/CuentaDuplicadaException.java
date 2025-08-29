package com.ejemplo.cuentabancaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar el error de una cuenta duplicada.
 * Esta clase extiende RuntimeException, lo que significa que no es necesario
 * declararla en la firma de los métodos.
 *
 * La anotación @ResponseStatus indica a Spring Boot que cuando esta excepción
 * sea lanzada, debe responder con el código de estado HTTP 409 Conflict.
 * Esto es una buena práctica en una API REST para comunicar que la petición
 * no se pudo completar debido a un conflicto con el estado actual del recurso,
 * en este caso, que el número de cuenta ya existe.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CuentaDuplicadaException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje detallado del error.
     */
    public CuentaDuplicadaException(String message) {
        super(message);
    }
}
