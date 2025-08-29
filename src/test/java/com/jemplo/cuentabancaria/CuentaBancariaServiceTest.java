package com.jemplo.cuentabancaria;

import com.ejemplo.cuentabancaria.exception.CuentaDuplicadaException;
import com.ejemplo.cuentabancaria.exception.CuentaNoEncontradaException;
import com.ejemplo.cuentabancaria.mapper.CuentaBancariaMapper;
import com.ejemplo.cuentabancaria.model.CuentaBancaria;
import com.ejemplo.cuentabancaria.model.CuentaBancariaDTO;
import com.ejemplo.cuentabancaria.repository.CuentaBancariaRepository;
import com.ejemplo.cuentabancaria.service.CuentaBancariaService;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class CuentaBancariaServiceTest {

    // Simula el repositorio para no depender de la base de datos real.
    @Mock
    private CuentaBancariaRepository repository;

    // Simula el mapper para no depender de la conversión de objetos real.
    @Mock
    private CuentaBancariaMapper mapper;

    // Inyecta los objetos mockeados en la clase de servicio que estamos probando.
    @InjectMocks
    private CuentaBancariaService service;

    // Se ejecuta antes de cada test para preparar el entorno.
    @BeforeEach
    void setUp() {
        // En este caso, @ExtendWith hace la mayor parte del trabajo, pero es una buena práctica
        // para inicializar el estado del test si es necesario.
    }

    @Test
    void testCrearCuentaExitosamente() {
        // Arrange
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setNumeroCuenta("123");
        CuentaBancaria entidad = new CuentaBancaria();
        entidad.setId(1L);
        entidad.setNumeroCuenta("123");

        // Configuración de Mocks:
        // Cuando se busca por numeroCuenta, simula que no existe (Optional.empty).
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.empty());
        // Simula la conversión del DTO a la entidad.
        when(mapper.toEntity(dto)).thenReturn(entidad);
        // Simula que el guardado de la entidad es exitoso y devuelve la misma entidad.
        when(repository.save(entidad)).thenReturn(entidad);
        // Simula la conversión de la entidad a DTO para la respuesta.
        when(mapper.toDto(entidad)).thenReturn(dto);

        // Act
        CuentaBancariaDTO resultado = service.crearCuenta(dto);

        // Assert
        // Verifica que el resultado no sea nulo.
        assertNotNull(resultado, "El resultado no debe ser nulo.");
        // Verifica que la cuenta devuelta sea la misma que se mockeó.
        assertEquals(dto, resultado, "El DTO devuelto debe ser el esperado.");
        // Verifica que el método 'save' del repositorio se llamó una vez con la entidad correcta.
        verify(repository, times(1)).save(entidad);
    }

    @Test
    void testCrearCuenta_DuplicateException() {
        // Arrange
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setNumeroCuenta("123");
        CuentaBancaria entidadExistente = new CuentaBancaria();

        // Configuración de Mocks:
        // Simula que la cuenta ya existe en la base de datos.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.of(entidadExistente));

        // Act & Assert
        // Verifica que se lance la excepción CuentaDuplicadaException.
        assertThrows(CuentaDuplicadaException.class, () -> service.crearCuenta(dto),
                "Debe lanzar una excepcion si la cuenta ya existe.");
    }

    @Test
    void testGetCuentaByNumero_Found() {
        // Arrange
        CuentaBancaria entidad = new CuentaBancaria();
        entidad.setNumeroCuenta("123");
        CuentaBancariaDTO dto = new CuentaBancariaDTO();
        dto.setNumeroCuenta("123");

        // Configuración de Mocks:
        // Simula que la cuenta es encontrada.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.of(entidad));
        when(mapper.toDto(entidad)).thenReturn(dto);

        // Act
        Optional<CuentaBancariaDTO> resultado = service.getCuentaByNumero("123");

        // Assert
        // Verifica que el Optional contenga un valor.
        assertTrue(resultado.isPresent(), "El resultado debe estar presente.");
        // Verifica que el DTO dentro del Optional sea el correcto.
        assertEquals(dto, resultado.get(), "El DTO devuelto debe ser el esperado.");
    }

    @Test
    void testGetCuentaByNumero_NotFound() {
        // Arrange
        // Simula que la cuenta no es encontrada.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.empty());

        // Act
        Optional<CuentaBancariaDTO> resultado = service.getCuentaByNumero("123");

        // Assert
        // Verifica que el Optional esté vacío.
        assertFalse(resultado.isPresent(), "El resultado no debe estar presente.");
    }

    @Test
    void testGetAllCuentas_ReturnsList() {
        // Arrange
        CuentaBancaria entidad1 = new CuentaBancaria();
        CuentaBancaria entidad2 = new CuentaBancaria();
        CuentaBancariaDTO dto1 = new CuentaBancariaDTO();
        CuentaBancariaDTO dto2 = new CuentaBancariaDTO();
        List<CuentaBancaria> entidades = Arrays.asList(entidad1, entidad2);
        List<CuentaBancariaDTO> dtos = Arrays.asList(dto1, dto2);

        // Configuración de Mocks:
        // Simula que el repositorio devuelve una lista de entidades.
        when(repository.findAll()).thenReturn(entidades);
        // Simula la conversión de cualquier entidad a su DTO correspondiente.
        when(mapper.toDto(any(CuentaBancaria.class))).thenReturn(dto1, dto2);

        // Act
        List<CuentaBancariaDTO> resultado = service.getAllCuentas();

        // Assert
        // Compara el tamaño y el contenido de las listas.
        assertEquals(2, resultado.size(), "La lista devuelta debe tener 2 elementos.");
        assertEquals(dtos, resultado, "Las listas de DTOs deben ser iguales.");
    }

    @Test
    void testUpdateCuenta_Success() {
        // Arrange
        CuentaBancaria entidadExistente = new CuentaBancaria();
        entidadExistente.setNumeroCuenta("123");
        entidadExistente.setNombreTitular("Antiguo Titular");
        entidadExistente.setSaldo(new BigDecimal("50.0"));

        CuentaBancariaDTO dtoActualizado = new CuentaBancariaDTO();
        dtoActualizado.setNumeroCuenta("123");
        dtoActualizado.setNombreTitular("Nuevo Titular");
        dtoActualizado.setSaldo(new BigDecimal("100.0"));

        // Configuración de Mocks:
        // Simula que la cuenta es encontrada y luego que el guardado es exitoso.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.of(entidadExistente));
        when(repository.save(any(CuentaBancaria.class))).thenReturn(entidadExistente);
        when(mapper.toDto(entidadExistente)).thenReturn(dtoActualizado);

        // Act
        CuentaBancariaDTO resultado = service.updateCuenta("123", dtoActualizado);

        // Assert
        // Verifica que los datos de la entidad se actualizaron correctamente.
        assertEquals("Nuevo Titular", resultado.getNombreTitular());
        assertEquals(new BigDecimal("100.0"), resultado.getSaldo());
    }

    @Test
    void testUpdateCuenta_NotFound() {
        // Arrange
        CuentaBancariaDTO dtoActualizado = new CuentaBancariaDTO();
        // Simula que la cuenta no es encontrada.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.empty());

        // Act & Assert
        // Verifica que se lance la excepción correcta si la cuenta no existe.
        assertThrows(CuentaNoEncontradaException.class, () -> service.updateCuenta("123", dtoActualizado),
                "Debe lanzar una excepcion si la cuenta no es encontrada.");
    }

    @Test
    void testDeleteCuenta_Success() {
        // Arrange
        CuentaBancaria entidad = new CuentaBancaria();
        // Simula que la cuenta es encontrada.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.of(entidad));
        // Mockea el método 'delete' para que no haga nada.
        doNothing().when(repository).delete(entidad);

        // Act
        service.deleteCuenta("123");

        // Assert
        // Verifica que el método 'delete' del repositorio se llamó una vez.
        verify(repository, times(1)).delete(entidad);
    }

    @Test
    void testDeleteCuenta_NotFound() {
        // Arrange
        // Simula que la cuenta no es encontrada.
        when(repository.findByNumeroCuenta("123")).thenReturn(Optional.empty());

        // Act & Assert
        // Verifica que se lance la excepción correcta si la cuenta no existe.
        assertThrows(CuentaNoEncontradaException.class, () -> service.deleteCuenta("123"),
                "Debe lanzar una excepcion si la cuenta no es encontrada.");
    }

@Test
void testCrearCuenta_LlamaLoggerYGuardaCuenta() {
    CuentaBancariaDTO dto = new CuentaBancariaDTO();
    dto.setNumeroCuenta("456");
    CuentaBancaria entidad = new CuentaBancaria();
    entidad.setId(2L);
    entidad.setNumeroCuenta("456");

    when(repository.findByNumeroCuenta("456")).thenReturn(Optional.empty());
    when(mapper.toEntity(dto)).thenReturn(entidad);
    when(repository.save(entidad)).thenReturn(entidad);
    when(mapper.toDto(entidad)).thenReturn(dto);

    CuentaBancariaDTO resultado = service.crearCuenta(dto);

    assertNotNull(resultado);
    assertEquals("456", resultado.getNumeroCuenta());
    verify(repository, times(1)).save(entidad);
}

@Test
void testGetCuentaByNumero_DevuelveOptionalDTO() {
    CuentaBancaria entidad = new CuentaBancaria();
    entidad.setNumeroCuenta("789");
    CuentaBancariaDTO dto = new CuentaBancariaDTO();
    dto.setNumeroCuenta("789");

    when(repository.findByNumeroCuenta("789")).thenReturn(Optional.of(entidad));
    when(mapper.toDto(entidad)).thenReturn(dto);

    Optional<CuentaBancariaDTO> resultado = service.getCuentaByNumero("789");

    assertTrue(resultado.isPresent());
    assertEquals("789", resultado.get().getNumeroCuenta());
}

@Test
void testGetAllCuentas_DevuelveListaDTOs() {
    CuentaBancaria entidad1 = new CuentaBancaria();
    CuentaBancaria entidad2 = new CuentaBancaria();
    CuentaBancariaDTO dto1 = new CuentaBancariaDTO();
    CuentaBancariaDTO dto2 = new CuentaBancariaDTO();
    List<CuentaBancaria> entidades = Arrays.asList(entidad1, entidad2);

    when(repository.findAll()).thenReturn(entidades);
    when(mapper.toDto(entidad1)).thenReturn(dto1);
    when(mapper.toDto(entidad2)).thenReturn(dto2);

    List<CuentaBancariaDTO> resultado = service.getAllCuentas();

    assertEquals(2, resultado.size());
}

@Test
void testUpdateCuenta_ActualizaNombreYSaldo() {
    CuentaBancaria entidad = new CuentaBancaria();
    entidad.setNumeroCuenta("321");
    entidad.setNombreTitular("Viejo");
    entidad.setSaldo(new BigDecimal("10.0"));

    CuentaBancariaDTO dto = new CuentaBancariaDTO();
    dto.setNumeroCuenta("321");
    dto.setNombreTitular("Nuevo");
    dto.setSaldo(new BigDecimal("20.0"));

    when(repository.findByNumeroCuenta("321")).thenReturn(Optional.of(entidad));
    when(repository.save(entidad)).thenReturn(entidad);
    when(mapper.toDto(entidad)).thenReturn(dto);

    CuentaBancariaDTO resultado = service.updateCuenta("321", dto);

    assertEquals("Nuevo", resultado.getNombreTitular());
    assertEquals(new BigDecimal("20.0"), resultado.getSaldo());
}

@Test
void testDeleteCuenta_EliminaCuenta() {
    CuentaBancaria entidad = new CuentaBancaria();
    when(repository.findByNumeroCuenta("654")).thenReturn(Optional.of(entidad));
    doNothing().when(repository).delete(entidad);

    service.deleteCuenta("654");

    verify(repository, times(1)).delete(entidad);
}}