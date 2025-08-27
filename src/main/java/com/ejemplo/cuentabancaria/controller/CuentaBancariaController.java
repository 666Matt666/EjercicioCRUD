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

@RestController
@RequestMapping("/cuentas")
public class CuentaBancariaController {
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaController.class);

    @Autowired
    private CuentaBancariaService service;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<CuentaBancariaDTO> crearCuenta(@Valid @RequestBody CuentaBancariaDTO cuentaDto) {
        CuentaBancariaDTO nuevaCuenta = service.crearCuenta(cuentaDto);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @GetMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> getCuentaByNumero(@PathVariable String numeroDeCuenta) {
        return service.getCuentaByNumero(numeroDeCuenta)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con numero: " + numeroDeCuenta));
    }

    @GetMapping
    public ResponseEntity<List<CuentaBancariaDTO>> getAllCuentas() {
        return ResponseEntity.ok(service.getAllCuentas());
    }

    @PutMapping("/{numeroDeCuenta}")
    public ResponseEntity<CuentaBancariaDTO> updateCuenta(@PathVariable String numeroDeCuenta, @Valid @RequestBody CuentaBancariaDTO cuentaDto) {
        CuentaBancariaDTO cuentaActualizada = service.updateCuenta(numeroDeCuenta, cuentaDto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @DeleteMapping("/{numeroDeCuenta}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable String numeroDeCuenta) {
        service.deleteCuenta(numeroDeCuenta);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar/{numeroDeCuenta}")
    public ResponseEntity<String> verificarExistencia(@PathVariable String numeroDeCuenta) {
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