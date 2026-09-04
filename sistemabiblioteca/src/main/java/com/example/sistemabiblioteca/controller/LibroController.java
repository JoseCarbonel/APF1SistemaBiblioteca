package com.example.sistemabiblioteca.controller;

import com.example.sistemabiblioteca.model.Libro;
import com.example.sistemabiblioteca.model.Resena;
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
@RequestMapping("/api/libros")
public class LibroController {

    private final List<Libro> libros = new ArrayList<>();
    private final List<Resena> reseñas = new ArrayList<>();
    private final AtomicLong libroIdCounter = new AtomicLong(1);
    private final AtomicLong resenaIdCounter = new AtomicLong(1);

    public LibroController() {
        // Datos iniciales para pruebas
        libros.add(new Libro(libroIdCounter.getAndIncrement(), "Cien Años de Soledad", "978-0307474728", "Ficción", true));
        libros.add(new Libro(libroIdCounter.getAndIncrement(), "El Principito", "978-0156013987", "Infantil", true));
    }

    // 1. Crear Libro
    @PostMapping
    public ResponseEntity<?> crearLibro(@RequestBody Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty() ||
            libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El título y el ISBN son obligatorios");
        }

        boolean isbnExiste = libros.stream().anyMatch(l -> l.getIsbn().equalsIgnoreCase(libro.getIsbn()));
        if (isbnExiste) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El ISBN ya se encuentra registrado");
        }

        libro.setId(libroIdCounter.getAndIncrement());
        if (libro.getDisponible() == null) {
            libro.setDisponible(true);
        }
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    // 2. Listar Libros (con opción a filtro por categoría)
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerLibros(@RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.trim().isEmpty()) {
            List<Libro> filtrados = libros.stream()
                    .filter(l -> l.getCategoria() != null && l.getCategoria().equalsIgnoreCase(categoria))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filtrados);
        }
        return ResponseEntity.ok(libros);
    }

    // 3. Obtener Libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerLibroPorId(@PathVariable String id) {
        Long libroId;
        try {
            libroId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del libro debe ser numérico");
        }

        Optional<Libro> libroOpt = libros.stream().filter(l -> l.getId().equals(libroId)).findFirst();
        if (libroOpt.isPresent()) {
            return ResponseEntity.ok(libroOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
    }

    // 4. Agregar Reseña a un Libro
    @PostMapping("/{id}/reseñas")
    public ResponseEntity<?> agregarResena(@PathVariable Long id, @RequestBody Resena resena) {
        Optional<Libro> libroOpt = libros.stream().filter(l -> l.getId().equals(id)).findFirst();
        if (libroOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede reseñar un libro inexistente");
        }

        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La calificación debe estar entre 1 y 5");
        }

        resena.setId(resenaIdCounter.getAndIncrement());
        resena.setLibroId(id);
        if (resena.getFechaResena() == null) {
            resena.setFechaResena(LocalDate.now());
        }
        reseñas.add(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body(resena);
    }
}