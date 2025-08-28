package com.ejemplo.cuentabancaria.controller;

import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import com.ejemplo.cuentabancaria.service.CuentaBancariaService;
import com.ejemplo.cuentabancaria.exception.CuentaNoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar cuentas bancarias.
 * Expone los endpoints (puntos de acceso) para realizar operaciones CRUD.
 * La ruta base para todos los endpoints es "/cuentas".
 */
@RestController
@RequestMapping("/cuentas")
public class CuentaBancariaController {
    // Logger para registrar eventos importantes de la clase.
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaController.class);

    // Inyectamos la capa de servicio para acceder a la lógica de negocio.
    @Autowired
    private CuentaBancariaService service;
    
    // Inyectamos RestTemplate para realizar llamadas a otros servicios.
    // En este caso, lo usamos para hacer una llamada a un endpoint propio.
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Endpoint para crear una nueva cuenta bancaria.
     * @param cuentaDto El objeto DTO con los datos de la nueva cuenta.
     * @return ResponseEntity con la cuenta creada y el estado HTTP 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<CuentaBancariaDTO> crearCuenta(@Valid @RequestBody CuentaBancariaDTO cuentaDto) {
        CuentaBancariaDTO nuevaCuenta = service.crearCuenta(cuentaDto);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener una cuenta bancaria por su número.
     * @param numeroDeCuenta El número de cuenta a buscar, se extrae de la URL.
     * @return ResponseEntity con la cuenta encontrada y el estado HTTP 200 (OK).
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta.
     */
    @GetMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> getCuentaByNumero(@PathVariable String numeroDeCuenta) {
        return service.getCuentaByNumero(numeroDeCuenta)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroDeCuenta));
    }

    /**
     * Endpoint para obtener una lista de todas las cuentas bancarias.
     * @return ResponseEntity con una lista de DTOs de cuentas y el estado HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<CuentaBancariaDTO>> getAllCuentas() {
        return ResponseEntity.ok(service.getAllCuentas());
    }

    /**
     * Endpoint para actualizar una cuenta existente.
     * @param numeroDeCuenta El número de cuenta a actualizar.
     * @param cuentaDto El objeto DTO con los datos actualizados.
     * @return ResponseEntity con la cuenta actualizada y el estado HTTP 200 (OK).
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta.
     */
    @PutMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> updateCuenta(@PathVariable String numeroDeCuenta, @Valid @RequestBody CuentaBancariaDTO cuentaDto) {
        CuentaBancariaDTO cuentaActualizada = service.updateCuenta(numeroDeCuenta, cuentaDto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    /**
     * Endpoint para eliminar una cuenta bancaria.
     * @param numeroDeCuenta El número de cuenta a eliminar.
     * @return ResponseEntity sin contenido y el estado HTTP 204 (NO CONTENT).
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta.
     */
    @DeleteMapping("/{numeroDeCuenta}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable String numeroDeCuenta) {
        service.deleteCuenta(numeroDeCuenta);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint que se consume a sí mismo para verificar la existencia de una cuenta.
     * Demuestra cómo un microservicio puede llamar a sus propios endpoints.
     * @param numeroDeCuenta El número de cuenta a verificar.
     * @return ResponseEntity con un mensaje de estado.
     */
    @GetMapping("/verificar/{numeroDeCuenta}")
    public ResponseEntity<String> verificarExistencia(@PathVariable String numeroDeCuenta) {
        logger.info("Endpoint de verificacion llamado para el numero: {}", numeroDeCuenta);
        
        // Construimos la URL completa para el endpoint de consulta.
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/cuentas/{numeroDeCuenta}")
                .buildAndExpand(numeroDeCuenta)
                .toUriString();
        
        try {
            // Realizamos la llamada HTTP GET al endpoint de consulta.
            restTemplate.getForEntity(url, CuentaBancariaDTO.class);
            return ResponseEntity.ok("La cuenta existe.");
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            // Si el endpoint de consulta devuelve 404, significa que la cuenta no existe.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta no existe.");
        } catch (Exception e) {
            // Capturamos cualquier otro error inesperado y lo registramos.
            logger.error("Error inesperado al verificar la cuenta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}