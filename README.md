**Sistema de Gestión de Biblioteca - API REST (APF1)**

Este proyecto es una aplicación Spring Boot desarrollada bajo los principios de la arquitectura RESTful y probada mediante la metodología TDD (Test-Driven Development).

**Arquitectura del Proyecto**

El sistema está construido siguiendo una separación limpia por capas:

Model (com.example.sistemabiblioteca.model): Define las entidades principales (Libro, Usuario, Prestamo, Resena). Son clases POJO encapsuladas con identificador único Long y constructores requeridos para el mapeo JSON.

Controller (com.example.sistemabiblioteca.controller): Expone la API REST (@RestController) mediante ResponseEntity, gestionando los verbos HTTP (GET, POST, PUT, DELETE) y mapeando datos con @PathVariable y @RequestBody.

Pruebas Unitarias TDD (src/test/java/...): Cobertura mediante 24 casos de prueba aislados en LibroControllerTest y PrestamoControllerTest.

**Endpoints de la API (8 Operaciones REST)**

**Gestión de Libros (/api/libros)**

POST /api/libros — Registrar un nuevo libro (201 Created / 400 Bad Request).

GET /api/libros — Listar todos los libros (200 OK).

GET /api/libros/{id} — Buscar un libro por ID (200 OK / 404 Not Found / 400 Bad Request).

POST /api/libros/{id}/reseñas — Agregar una reseña a un libro (201 Created / 404 Not Found / 400 Bad Request).

**Gestión de Préstamos (/api/prestamos)**

POST /api/prestamos — Registrar un préstamo (201 Created / 404 Not Found / 400 Bad Request).

GET /api/prestamos/usuario/{usuarioId} — Listar préstamos por usuario (200 OK / 404 Not Found).

PUT /api/prestamos/{id}/devolver — Devolver un libro prestado (200 OK / 404 Not Found / 400 Bad Request).

DELETE /api/prestamos/{id} — Cancelar/Eliminar un préstamo (204 No Content / 404 Not Found / 400 Bad Request).

**Ejecución del Proyecto**
Iniciar la aplicación:
./gradlew bootRun

**Ejecutar la suite completa de pruebas TDD:**
./gradlew test
