package com.ejemplo.cuentabancaria.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Objeto de Transferencia de Datos (DTO) que representa una Cuenta Bancaria.
 * Esta clase se usa para la comunicación de datos a través de la API.
 * Su propósito es desacoplar la capa de la API de la entidad de la base de datos,
 * permitiendo un mayor control sobre qué datos se exponen y se validan.
 */
@Schema(description = "DTO para la transferencia de datos de la cuenta bancaria")
public class CuentaBancariaDTO {
    // El ID se incluye para operaciones como la actualización, pero no es necesario para la creación.
    private Long id;

    /**
     * Número de la cuenta bancaria.
     * La anotación @NotBlank valida que el campo no sea nulo, ni una cadena vacía, ni solo espacios en blanco.
     * El mensaje personalizado se mostrará al cliente si la validación falla.
     */
    @NotBlank(message = "El numero de cuenta es obligatorio.")
    private String numeroCuenta;

    /**
     * Nombre del titular de la cuenta.
     * @NotBlank asegura que este campo sea obligatorio y no esté vacío.
     */
    @NotBlank(message = "El nombre del titular es obligatorio.")
    private String nombreTitular;

    /**
     * Saldo de la cuenta.
     * @NotNull valida que el campo no sea nulo.
     * @Min(value = 0) valida que el valor sea igual o mayor a 0.
     * Se usa BigDecimal para manejar valores monetarios con precisión.
     */
    @NotNull(message = "El saldo es obligatorio.")
    @Min(value = 0, message = "El saldo no puede ser negativo.")
    private BigDecimal saldo;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}