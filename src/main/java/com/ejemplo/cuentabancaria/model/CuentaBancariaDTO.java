package com.ejemplo.cuentabancaria.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CuentaBancariaDTO {
    private Long id;

    @NotBlank(message = "El numero de cuenta es obligatorio.")
    private String numeroCuenta;

    @NotBlank(message = "El nombre del titular es obligatorio.")
    private String nombreTitular;

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
