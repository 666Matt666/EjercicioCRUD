package com.ejemplo.cuentabancaria.mapper;

import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import org.springframework.stereotype.Component;

/**
 * Clase que actúa como un "mapeador" o convertidor de objetos.
 * Se encarga de transformar los objetos de la Entidad (CuentaBancaria),
 * que se usan para la persistencia en la base de datos,
 * a los objetos DTO (CuentaBancariaDTO), que se usan para la comunicación
 * con la API REST. Esto mantiene el desacoplamiento entre las capas.
 */
@Component
public class CuentaBancariaMapper {

    /**
     * Convierte un objeto DTO a una entidad de la base de datos.
     * @param dto Objeto de transferencia de datos de entrada.
     * @return La entidad CuentaBancaria con los datos del DTO.
     */
    public CuentaBancaria toEntity(CuentaBancariaDTO dto) {
        CuentaBancaria entidad = new CuentaBancaria();
        entidad.setId(dto.getId());
        entidad.setNumeroCuenta(dto.getNumeroCuenta());
        entidad.setNombreTitular(dto.getNombreTitular());
        entidad.setSaldo(dto.getSaldo());
        return entidad;
    }

    /**
     * Convierte una entidad de la base de datos a un objeto DTO.
     * @param entidad La entidad CuentaBancaria de la base de datos.
     * @return El objeto DTO con los datos de la entidad.
     */
    public CuentaBancariaDTO toDto(CuentaBancaria entidad) {
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setId(entidad.getId());
        dto.setNumeroCuenta(entidad.getNumeroCuenta());
        dto.setNombreTitular(entidad.getNombreTitular());
        dto.setSaldo(entidad.getSaldo());
        return dto;
    }
}
