package com.ejemplo.cuentabancaria.repository;

import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de datos para la entidad CuentaBancaria.
 * Esta interfaz es la capa de persistencia (acceso a datos) de la aplicación.
 *
 * Extiende JpaRepository para heredar automáticamente un conjunto de métodos
 * CRUD (Create, Read, Update, Delete) predefinidos por Spring Data JPA.
 *
 * @param <CuentaBancaria> La entidad con la que trabaja el repositorio.
 * @param <Long> El tipo de dato del identificador (clave primaria) de la entidad.
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {

    /**
     * Método para buscar una cuenta bancaria por su número de cuenta.
     * Spring Data JPA infiere automáticamente la consulta SQL a partir del nombre
     * del método, lo que evita tener que escribir la query manualmente.
     *
     * @param numeroCuenta El número de cuenta a buscar.
     * @return Un objeto Optional que contiene la CuentaBancaria si se encuentra,
     * o un Optional vacío si no existe.
     */
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);
}