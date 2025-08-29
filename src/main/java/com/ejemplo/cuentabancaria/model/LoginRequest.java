package com.ejemplo.cuentabancaria.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la petición de inicio de sesión.
 * Contiene las credenciales del usuario (email y password).
 */
@Schema(description = "Petición para el inicio de sesión del usuario")
public class LoginRequest {
    
    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
    private String email;
    
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}