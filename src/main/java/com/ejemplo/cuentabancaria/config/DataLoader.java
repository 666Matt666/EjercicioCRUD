package com.ejemplo.cuentabancaria.config;

import com.ejemplo.cuentabancaria.model.Usuario;
import com.ejemplo.cuentabancaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Clase para inicializar datos de prueba en la base de datos H2.
 * Esta clase se ejecuta automáticamente al iniciar la aplicación.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica si el usuario ya existe para evitar duplicados
        if (usuarioRepository.findByEmail("user@ejemplo.com").isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setEmail("user@ejemplo.com");
            // La contraseña se encripta antes de guardarla en la base de datos
            usuario.setPassword(passwordEncoder.encode("mi-password"));
            usuarioRepository.save(usuario);
            System.out.println("Usuario de prueba 'user@ejemplo.com' creado con éxito.");
        }
    }
}
