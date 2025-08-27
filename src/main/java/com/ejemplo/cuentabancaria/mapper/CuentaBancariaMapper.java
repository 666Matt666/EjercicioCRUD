package com.ejemplo.cuentabancaria.mapper;

import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import org.springframework.stereotype.Component;

@Component
public class CuentaBancariaMapper {

    public CuentaBancaria toEntity(CuentaBancariaDTO dto) {
        CuentaBancaria entidad = new CuentaBancaria();
        entidad.setId(dto.getId());
        entidad.setNumeroCuenta(dto.getNumeroCuenta());
        entidad.setNombreTitular(dto.getNombreTitular());
        entidad.setSaldo(dto.getSaldo());
        return entidad;
    }

    public CuentaBancariaDTO toDto(CuentaBancaria entidad) {
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setId(entidad.getId());
        dto.setNumeroCuenta(entidad.getNumeroCuenta());
        dto.setNombreTitular(entidad.getNombreTitular());
        dto.setSaldo(entidad.getSaldo());
        return dto;
    }
}
