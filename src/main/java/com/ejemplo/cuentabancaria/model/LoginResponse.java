package com.ejemplo.cuentabancaria.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la respuesta del inicio de sesión.
 * Contiene el token JWT generado para el usuario.
 */
@Schema(description = "Respuesta del inicio de sesión con el token JWT")
public class LoginResponse {
    
    @Schema(description = "Token JWT para autenticar futuras peticiones")
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }
}
