package com.ejemplo.cuentabancaria.repository;

import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);
}