package com.ejemplo.cuentabancaria.controller;

import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import com.ejemplo.cuentabancaria.service.CuentaBancariaService;
import com.ejemplo.cuentabancaria.exception.CuentaNoEncontradaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cuentas Bancarias", description = "Endpoints para la gestión de cuentas bancarias")
public class CuentaBancariaController {
    // Logger para registrar eventos importantes de la clase.
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaController.class);

    // Inyectamos la capa de servicio para acceder a la lógica de negocio.
    @Autowired
    private CuentaBancariaService service;
    
    // Inyectamos RestTemplate para realizar llamadas a otros servicios.
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Endpoint para crear una nueva cuenta bancaria.
     * @param cuentaDto El objeto DTO con los datos de la nueva cuenta.
     * @return ResponseEntity con la cuenta creada y el estado HTTP 201 (CREATED).
     */
    @Operation(summary = "Crear una nueva cuenta bancaria",
               description = "Crea una nueva cuenta bancaria con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Número de cuenta duplicado")
    })
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
    @Operation(summary = "Obtener una cuenta por su número",
               description = "Busca y devuelve los detalles de una cuenta bancaria por su número.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> getCuentaByNumero(
        @Parameter(description = "Número de la cuenta a buscar") @PathVariable String numeroDeCuenta) {
      //@Parameter(description = "Número de la cuenta a actualizar") @PathVariable String numeroDeCuenta, 
        return service.getCuentaByNumero(numeroDeCuenta)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroDeCuenta));
    }

    /**
     * Endpoint para obtener una lista de todas las cuentas bancarias.
     * @return ResponseEntity con una lista de DTOs de cuentas y el estado HTTP 200 (OK).
     */
    @Operation(summary = "Obtener todas las cuentas bancarias",
               description = "Devuelve una lista de todas las cuentas bancarias registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cuentas devuelta exitosamente")
    })
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
    @Operation(summary = "Actualizar una cuenta existente", description = "Actualiza los datos de una cuenta bancaria existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> updateCuenta(
        @Parameter(description = "Número de la cuenta a actualizar") @PathVariable String numeroDeCuenta, 
        @Valid @RequestBody CuentaBancariaDTO cuentaDto) {
        CuentaBancariaDTO cuentaActualizada = service.updateCuenta(numeroDeCuenta, cuentaDto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    /**
     * Endpoint para eliminar una cuenta bancaria.
     * @param numeroDeCuenta El número de cuenta a eliminar.
     * @return ResponseEntity sin contenido y el estado HTTP 204 (NO CONTENT).
     * @throws CuentaNoEncontradaException si no se encuentra la cuenta.
     */
    @Operation(summary = "Eliminar una cuenta bancaria", description = "Elimina una cuenta bancaria del sistema por su número.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @DeleteMapping("/{numeroDeCuenta}")
    public ResponseEntity<Void> deleteCuenta(
        @Parameter(description = "Número de la cuenta a eliminar") @PathVariable String numeroDeCuenta) {
        service.deleteCuenta(numeroDeCuenta);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint que se consume a sí mismo para verificar la existencia de una cuenta.
     * Demuestra cómo un microservicio puede llamar a sus propios endpoints.
     * @param numeroDeCuenta El número de cuenta a verificar.
     * @return ResponseEntity con un mensaje de estado.
     */
    @Operation(summary = "Verificar existencia de una cuenta", description = "Endpoint de ejemplo que se llama a sí mismo para verificar si una cuenta existe.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La cuenta existe"),
        @ApiResponse(responseCode = "404", description = "La cuenta no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/verificar/{numeroDeCuenta}")
    public ResponseEntity<String> verificarExistencia(
        @Parameter(description = "Número de la cuenta a verificar") @PathVariable String numeroDeCuenta) {
        logger.info("Endpoint de verificacion llamado para el numero: {}", numeroDeCuenta);
        
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/cuentas/{numeroDeCuenta}")
                .buildAndExpand(numeroDeCuenta)
                .toUriString();
        
        try {
            restTemplate.getForEntity(url, CuentaBancariaDTO.class);
            return ResponseEntity.ok("La cuenta existe.");
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta no existe.");
        } catch (Exception e) {
            logger.error("Error inesperado al verificar la cuenta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}

