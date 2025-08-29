package com.ejemplo.cuentabancaria.repository;

import com.ejemplo.cuentabancaria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de datos para la entidad Usuario.
 *
 * Extiende JpaRepository para heredar automáticamente los métodos CRUD
 * (Create, Read, Update, Delete) predefinidos por Spring Data JPA.
 *
 * @param <Usuario> La entidad con la que trabaja el repositorio.
 * @param <Long> El tipo de dato del identificador (clave primaria) de la entidad.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Método para buscar un usuario por su dirección de correo electrónico.
     * Spring Data JPA genera la consulta automáticamente a partir del nombre del método.
     *
     * @param email La dirección de correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el objeto Usuario si se encuentra,
     * o un Optional vacío si no existe.
     */
    Optional<Usuario> findByEmail(String email);
}