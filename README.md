# EjercicioCRUD

Enunciado ejercicio práctico 1:
Desarrollar un microservicio, arquitectura REST, que exponga un CRUD completo sobre entidades bancarias, es decir, el CRUD debe permitir Altas, bajas, modificaciones, y consultas. Los campos de la entidad son a libre a interpretación al igual que sus relaciones (puede tener o no).
Asimismo, se espera que el microservicio exponga un nuevo End Point (dentro del mismo microservicio) que consuma el end point de consulta, es decir, que realice una llamada así mismo.
Observaciones:
-	Se puede usar una base de datos en memoria (h2).
-	Se valora el diseño del microservicio: tipo de arquitectura, si aplica algún patrón de diseño, etc.
-	Se valoran aspectos tales como manejo de duplicidad en la creación/post y en el manejo de excepciones.
-	Se valora si el ejemplo incorpora algún test.

Los fuentes fueron subidos en https://github.com/666Matt666/EjercicioCRUD
El desarrollo fue diseñado en capas
Se adjunta el archivo "REST API basics- CRUD, test & variable.postman_collection.json" para importar en Postman y probar las funcionalidades.

    "REST API basics- CRUD, test & variable.postman_collection.json" se corre con RUN adjuntando el archivo "cuentasBancarias.json"

    Lo primero que se debe realizar para la prueba es ejecutar el POST Obtener TOKEN (este tiene una duracion de una hora)
    El resto de los metodos, tienen agregada la variable token ({{Access_token}}) en el Authorization

Microservicio de Gestión de Cuentas Bancarias

Este proyecto es un microservicio RESTful desarrollado con Spring Boot para gestionar un CRUD completo sobre una entidad bancaria (CuentaBancaria). La aplicación incluye autenticación basada en tokens JWT, validación de datos, y documentación de la API con Swagger.


### 📖 Documentación de la API (Swagger UI)

La documentación de la API se genera automáticamente usando **Swagger UI (OpenAPI 3.0)**. En el código, se utilizan anotaciones (`@Tag`) para categorizar los endpoints por su función (`Cuentas Bancarias`).

Esto permite que la documentación sea fácil de navegar y que otros desarrolladores puedan entender rápidamente la estructura del microservicio.

Puedes acceder a ella en:
`http://localhost:8080/swagger-ui.html`


🚀 Diseño y Arquitectura
El microservicio está diseñado siguiendo una arquitectura de capas que separa claramente las responsabilidades, lo que facilita la escalabilidad y el mantenimiento:

controller: La capa de presentación. Expone los endpoints REST para las peticiones HTTP.

service: La capa de lógica de negocio. Contiene las reglas del negocio, como la validación de duplicidad y el manejo de excepciones.

repository: La capa de persistencia. Se encarga de la comunicación con la base de datos (H2 en memoria).

Se aplican los siguientes patrones de diseño y buenas prácticas:

Arquitectura RESTful: Se utilizan los verbos HTTP estándar (GET, POST, PUT, DELETE) y códigos de estado para una comunicación eficiente.

DTOs: Se usan DTOs (Data Transfer Objects) para transferir datos de forma segura entre la capa de presentación y la lógica de negocio, desacoplando la entidad de la base de datos de la API.

Manejo de Excepciones Global: Un manejador de excepciones centralizado (GlobalExceptionHandler) captura los errores y los convierte en respuestas HTTP consistentes.

Inyección de Dependencias: El contenedor de Spring se encarga de gestionar la creación y conexión de los objetos (@Autowired), reduciendo el acoplamiento.

⚙️ Requisitos y Tecnologías
Para ejecutar este proyecto, necesitas:

Java 17 (JDK) o superior.

Maven para la gestión de dependencias.

▶️ Instrucciones de Uso
1. Clona el repositorio
Bash

git clone <https://github.com/666Matt666/EjercicioCRUD/tree/main>
cd cuenta-bancaria
2. Ejecuta la aplicación
Usa el siguiente comando para iniciar el microservicio. La base de datos H2 se levantará automáticamente en memoria.

Bash

./mvnw spring-boot:run
3. Accede a las herramientas
Una vez que la aplicación esté corriendo, podrás acceder a las siguientes herramientas:

Documentación de la API (Swagger UI):
http://localhost:8080/swagger-ui.html

Consola de la Base de Datos H2:
http://localhost:8080/h2-console
(Conéctate usando la URL JDBC: jdbc:h2:mem:bancodb y usuario: sa)

🔐 Autenticación y Tokens JWT
Para acceder a los endpoints protegidos, primero debes obtener un token JWT.

1. Crear usuario (una sola vez)
POST a: http://localhost:8080/api/register

Body (JSON):

JSON

{
  "email": "user@ejemplo.com",
  "password": "mi-password"
}
2. Obtener Token JWT
POST a: http://localhost:8080/api/login

Body (JSON):

JSON

{
  "email": "user@ejemplo.com",
  "password": "mi-password"
}
los metodos, tienen agregada la variable token ({{Access_token}}) en el "Authorization" o te dará un token, el cual debes copiar manualmente

3. Usar el Token
Para cualquier petición a los endpoints de /cuentas, debes agregar el token en la cabecera Authorization de la siguiente manera:

Key: Authorization

Value: Bearer Token <TU_TOKEN_AQUÍ>

✅ Endpoints de la API
POST /cuentas - Crea una nueva cuenta.

GET /cuentas/{numeroDeCuenta} - Consulta una cuenta específica.

GET /cuentas - Obtiene todas las cuentas.

PUT /cuentas/{numeroDeCuenta} - Actualiza una cuenta.

DELETE /cuentas/{numeroDeCuenta} - Elimina una cuenta.