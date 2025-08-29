package com.ejemplo.cuentabancaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entidad que representa una Cuenta Bancaria.
 * Esta clase es un "modelo de dominio" que se mapea directamente a una tabla
 * en la base de datos gracias al framework JPA (Java Persistence API).
 */
@Entity
public class CuentaBancaria {
    
    /**
     * Identificador único de la cuenta en la base de datos.
     * La anotación @Id marca este campo como la clave primaria.
     * La anotación @GeneratedValue define cómo se genera su valor (en este caso, automáticamente).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Número de la cuenta bancaria.
     * La anotación @NotBlank valida que el campo no esté vacío.
     * La anotación @Column(unique = true) asegura que no haya dos cuentas
     * con el mismo número en la base de datos.
     */
    @NotBlank
    @Column(unique = true)
    private String numeroCuenta;

    /**
     * Nombre del titular de la cuenta.
     * @NotBlank valida que este campo no sea nulo ni esté vacío.
     */
    @NotBlank
    private String nombreTitular;

    /**
     * Saldo de la cuenta.
     * @NotNull valida que el saldo no sea nulo.
     * @Min(0) asegura que el saldo no pueda ser un valor negativo.
     * Se utiliza BigDecimal para evitar problemas de precisión con valores decimales.
     */
    @NotNull
    @Min(0)
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
