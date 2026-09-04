package com.example.sistemabiblioteca.controller;

import com.example.sistemabiblioteca.model.Prestamo;
import com.example.sistemabiblioteca.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final AtomicLong prestamoIdCounter = new AtomicLong(1);

    public PrestamoController() {
        // Usuarios iniciales
        usuarios.add(new Usuario(1L, "Juan Perez", "juan@gmail.com", "987654321", "ESTUDIANTE"));
        usuarios.add(new Usuario(2L, "Maria Lopez", "maria@gmail.com", "912345678", "DOCENTE"));

        // Préstamo inicial
        prestamos.add(new Prestamo(prestamoIdCounter.getAndIncrement(), 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7), "PENDIENTE"));
    }

    // 5. Registrar Préstamo
    @PostMapping
    public ResponseEntity<?> registrarPrestamo(@RequestBody Prestamo prestamo) {
        boolean usuarioExiste = usuarios.stream().anyMatch(u -> u.getId().equals(prestamo.getUsuarioId()));
        if (!usuarioExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario ingresado no existe");
        }

        boolean libroPrestado = prestamos.stream()
                .anyMatch(p -> p.getLibroId().equals(prestamo.getLibroId()) && "PENDIENTE".equalsIgnoreCase(p.getEstado()));
        if (libroPrestado) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El libro ya se encuentra prestado actualmente");
        }

        prestamo.setId(prestamoIdCounter.getAndIncrement());
        if (prestamo.getFechaPrestamo() == null) {
            prestamo.setFechaPrestamo(LocalDate.now());
        }
        if (prestamo.getFechaDevolucion() == null) {
            prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
        }
        prestamo.setEstado("PENDIENTE");

        prestamos.add(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    // 6. Consultar Préstamos por Usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        boolean usuarioExiste = usuarios.stream().anyMatch(u -> u.getId().equals(usuarioId));
        if (!usuarioExiste) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        List<Prestamo> prestamosUsuario = prestamos.stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(prestamosUsuario);
    }

    // 7. Devolver Libro
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {
        Optional<Prestamo> prestamoOpt = prestamos.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (prestamoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado");
        }

        Prestamo prestamo = prestamoOpt.get();
        if ("DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El préstamo ya fue devuelto con anterioridad");
        }

        prestamo.setEstado("DEVUELTO");
        return ResponseEntity.ok(prestamo);
    }

    // 8. Cancelar Préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPrestamo(@PathVariable Long id) {
        Optional<Prestamo> prestamoOpt = prestamos.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (prestamoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado para eliminación");
        }

        Prestamo prestamo = prestamoOpt.get();
        if ("DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede eliminar un préstamo finalizado");
        }

        prestamos.remove(prestamo);
        return ResponseEntity.noContent().build();
    }
}