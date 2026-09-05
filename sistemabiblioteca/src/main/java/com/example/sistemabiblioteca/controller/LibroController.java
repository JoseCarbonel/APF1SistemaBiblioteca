package com.example.sistemabiblioteca.controller;

import com.example.sistemabiblioteca.model.Libro;
import com.example.sistemabiblioteca.model.Resena;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final List<Libro> libros = new ArrayList<>();
    private final List<Resena> reseñas = new ArrayList<>();

    public LibroController() {
        libros.add(new Libro(1L, "Cien Años de Soledad", "978-1", "Ficción"));
        libros.add(new Libro(2L, "El Principito", "978-2", "Infantil"));
    }

    // 1. Crear Libro
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
            return ResponseEntity.badRequest().body("Título requerido");
        }
        libro.setId((long) (libros.size() + 1));
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }

    // 2. Listar Libros
    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(libros);
    }

    // 3. Buscar Libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            Long libroId = Long.parseLong(id);
            for (Libro l : libros) {
                if (l.getId().equals(libroId)) return ResponseEntity.ok(l);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID inválido");
        }
    }

    // 4. Agregar Reseña
    @PostMapping("/{id}/reseñas")
    public ResponseEntity<?> agregarResena(@PathVariable Long id, @RequestBody Resena resena) {
        if (id > libros.size() || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro inexistente");
        }
        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            return ResponseEntity.badRequest().body("Calificación inválida");
        }
        resena.setId((long) (reseñas.size() + 1));
        resena.setLibroId(id);
        reseñas.add(resena);
        return ResponseEntity.status(HttpStatus.CREATED).body(resena);
    }
}