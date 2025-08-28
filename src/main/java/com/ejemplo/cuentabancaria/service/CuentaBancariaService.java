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

/**
 * Clase de servicio que contiene la lógica de negocio del microservicio.
 * Es responsable de gestionar las operaciones CRUD de las cuentas bancarias.
 * Las anotaciones @Service de Spring marcan esta clase como un componente de servicio,
 * haciendo que esté disponible para su inyección.
 */
@Service
public class CuentaBancariaService {
    // Logger para registrar información, advertencias y errores.
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaService.class);

    // Inyección de la capa de persistencia para acceder a los datos.
    @Autowired
    private CuentaBancariaRepository repository;
    
    // Inyección del mapeador para convertir entre entidades y DTOs.
    @Autowired
    private CuentaBancariaMapper mapper;

    /**
     * Crea una nueva cuenta bancaria en el sistema.
     * @param cuentaDto Objeto DTO con los datos de la nueva cuenta.
     * @return El DTO de la cuenta creada, con el ID asignado.
     * @throws CuentaDuplicadaException si ya existe una cuenta con el mismo número.
     */
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

    /**
     * Obtiene una cuenta bancaria por su número.
     * @param numeroCuenta El número de cuenta a buscar.
     * @return Un Optional con el DTO de la cuenta si se encuentra, o un Optional vacío.
     */
    public Optional<CuentaBancariaDTO> getCuentaByNumero(String numeroCuenta) {
        logger.info("Buscando cuenta con numero: {}", numeroCuenta);
        return repository.findByNumeroCuenta(numeroCuenta).map(mapper::toDto);
    }

    /**
     * Obtiene una lista de todas las cuentas bancarias.
     * @return Una lista de DTOs de cuentas bancarias.
     */
    public List<CuentaBancariaDTO> getAllCuentas() {
        logger.info("Obteniendo todas las cuentas.");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de una cuenta existente.
     * @param numeroCuenta El número de cuenta a actualizar.
     * @param cuentaActualizadaDto Objeto DTO con los datos nuevos de la cuenta.
     * @return El DTO de la cuenta actualizada.
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta a actualizar.
     */
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

    /**
     * Elimina una cuenta bancaria por su número.
     * @param numeroCuenta El número de cuenta a eliminar.
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta a eliminar.
     */
    public void deleteCuenta(String numeroCuenta) {
        logger.info("Intentando eliminar cuenta con numero: {}", numeroCuenta);
        CuentaBancaria cuenta = repository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroCuenta));
        repository.delete(cuenta);
        logger.info("Cuenta eliminada exitosamente.");
    }
}
