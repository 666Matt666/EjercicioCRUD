package com.ejemplo.cuentabancaria.service;

import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import com.ejemplo.cuentabancaria.repository.CuentaBancariaRepository;
import com.ejemplo.cuentabancaria.mapper.CuentaBancariaMapper;
import com.ejemplo.cuentabancaria.exception.CuentaDuplicadaException;
import com.ejemplo.cuentabancaria.exception.CuentaNoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuentaBancariaService {
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaService.class);

    @Autowired
    private CuentaBancariaRepository repository;
    @Autowired
    private CuentaBancariaMapper mapper;

    public CuentaBancariaDTO crearCuenta(CuentaBancariaDTO cuentaDto) {
        logger.info("Intentando crear cuenta con numero: {}", cuentaDto.getNumeroCuenta());
        if (repository.findByNumeroCuenta(cuentaDto.getNumeroCuenta()).isPresent()) {
            logger.warn("Fallo al crear la cuenta. Numero de cuenta duplicado: {}", cuentaDto.getNumeroCuenta());
            throw new CuentaDuplicadaException("Ya existe una cuenta con el numero: " + cuentaDto.getNumeroCuenta());
        }
        CuentaBancaria nuevaCuenta = repository.save(mapper.toEntity(cuentaDto));
        logger.info("Cuenta creada exitosamente con ID: {}", nuevaCuenta.getId());
        return mapper.toDto(nuevaCuenta);
    }

    public Optional<CuentaBancariaDTO> getCuentaByNumero(String numeroCuenta) {
        logger.info("Buscando cuenta con numero: {}", numeroCuenta);
        return repository.findByNumeroCuenta(numeroCuenta).map(mapper::toDto);
    }

    public List<CuentaBancariaDTO> getAllCuentas() {
        logger.info("Obteniendo todas las cuentas.");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public CuentaBancariaDTO updateCuenta(String numeroCuenta, CuentaBancariaDTO cuentaActualizadaDto) {
        logger.info("Intentando actualizar cuenta con numero: {}", numeroCuenta);
        CuentaBancaria cuentaExistente = repository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroCuenta));
        
        cuentaExistente.setNombreTitular(cuentaActualizadaDto.getNombreTitular());
        cuentaExistente.setSaldo(cuentaActualizadaDto.getSaldo());

        CuentaBancaria cuentaGuardada = repository.save(cuentaExistente);
        logger.info("Cuenta actualizada exitosamente con ID: {}", cuentaGuardada.getId());
        return mapper.toDto(cuentaGuardada);
    }

    public void deleteCuenta(String numeroCuenta) {
        logger.info("Intentando eliminar cuenta con numero: {}", numeroCuenta);
        CuentaBancaria cuenta = repository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroCuenta));
        repository.delete(cuenta);
        logger.info("Cuenta eliminada exitosamente.");
    }
}