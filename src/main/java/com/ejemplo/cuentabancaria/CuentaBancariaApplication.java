package com.ejemplo.cuentabancaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Clase principal que inicia la aplicación de Spring Boot.
 * La anotación @SpringBootApplication es una anotación de conveniencia
 * que combina @Configuration, @EnableAutoConfiguration y @ComponentScan.
 * Esto le indica a Spring que esta es la clase de configuración principal
 * y que debe escanear el paquete actual y sus subpaquetes en busca de componentes.
 */
@SpringBootApplication
public class CuentaBancariaApplication {

    /**
     * El método main es el punto de entrada de la aplicación.
     * Al ejecutar esta clase, Spring Boot se encarga de configurar y
     * arrancar el servidor web embebido (ej. Tomcat).
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(CuentaBancariaApplication.class, args);
    }

    /**
     * Bean para crear una instancia de RestTemplate.
     * Un "Bean" es un objeto que Spring instancia, ensambla y gestiona.
     * RestTemplate es una clase de Spring para hacer llamadas HTTP a
     * otros servicios REST. La creamos aquí para poder inyectarla
     * en el controlador y usarla para el endpoint de auto-consumo.
     * @return Una nueva instancia de RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}