package com.example.sistemabiblioteca.controller;

import com.example.sistemabiblioteca.model.Prestamo;
import com.example.sistemabiblioteca.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

    public PrestamoController() {
        usuarios.add(new Usuario(1L, "Juan", "juan@mail.com", "ALUMNO"));
        prestamos.add(new Prestamo(1L, 1L, 1L, "PENDIENTE"));
    }

    // 5. Registrar Préstamo
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Prestamo prestamo) {
        if (prestamo.getUsuarioId() == null || prestamo.getUsuarioId() > usuarios.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario inexistente");
        }
        if (prestamo.getLibroId() == null) {
            return ResponseEntity.badRequest().body("Libro requerido");
        }
        prestamo.setId((long) (prestamos.size() + 1));
        prestamo.setEstado("PENDIENTE");
        prestamos.add(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    // 6. Listar por Usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        if (usuarioId > usuarios.size() || usuarioId <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario inexistente");
        }
        List<Prestamo> res = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.getUsuarioId().equals(usuarioId)) res.add(p);
        }
        return ResponseEntity.ok(res);
    }

    // 7. Devolver Libro
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        for (Prestamo p : prestamos) {
            if (p.getId().equals(id)) {
                if ("DEVUELTO".equals(p.getEstado())) {
                    return ResponseEntity.badRequest().body("Ya devuelto");
                }
                p.setEstado("DEVUELTO");
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado");
    }

    // 8. Cancelar Préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        for (Prestamo p : prestamos) {
            if (p.getId().equals(id)) {
                if ("DEVUELTO".equals(p.getEstado())) {
                    return ResponseEntity.badRequest().body("No se puede eliminar finalizado");
                }
                prestamos.remove(p);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado");
    }
}