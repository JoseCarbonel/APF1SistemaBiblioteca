package com.example.sistemabiblioteca.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- OPERACIÓN 1: POST /api/libros (Crear Libro) ---

    @Test
    void testCrearLibroExitoso() throws Exception {
        String libroJson = """
            {
                "titulo": "Don Quijote de la Mancha",
                "isbn": "978-8424116415",
                "categoria": "Clásico",
                "disponible": true
            }
            """;

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(libroJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Don Quijote de la Mancha"));
    }

    @Test
    void testCrearLibroErrorSinTitulo() throws Exception {
        String libroJson = """
            {
                "titulo": "",
                "isbn": "978-1111111111",
                "categoria": "Ficción",
                "disponible": true
            }
            """;

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(libroJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearLibroIsbnDuplicado() throws Exception {
        String libroJson = """
            {
                "titulo": "Duplicado",
                "isbn": "978-0307474728",
                "categoria": "Ficción",
                "disponible": true
            }
            """;

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(libroJson))
                .andExpect(status().isConflict());
    }

    // --- OPERACIÓN 2: GET /api/libros (Listar / Filtrar) ---

    @Test
    void testObtenerTodosLosLibros() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testObtenerLibrosPorCategoriaExitoso() throws Exception {
        mockMvc.perform(get("/api/libros").param("categoria", "Ficción"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria").value("Ficción"));
    }

    @Test
    void testObtenerLibrosPorCategoriaSinResultados() throws Exception {
        mockMvc.perform(get("/api/libros").param("categoria", "Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- OPERACIÓN 3: GET /api/libros/{id} (Buscar por ID) ---

    @Test
    void testObtenerLibroPorIdExitoso() throws Exception {
        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Cien Años de Soledad"));
    }

    @Test
    void testObtenerLibroPorIdNoEncontrado() throws Exception {
        mockMvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerLibroPorIdFormatoInvalido() throws Exception {
        mockMvc.perform(get("/api/libros/abc"))
                .andExpect(status().isBadRequest());
    }

    // --- OPERACIÓN 4: POST /api/libros/{id}/reseñas (Agregar Reseña) ---

    @Test
    void testAgregarResenaExitoso() throws Exception {
        String resenaJson = """
            {
                "usuarioId": 1,
                "calificacion": 5,
                "comentario": "Excelente libro"
            }
            """;

        mockMvc.perform(post("/api/libros/1/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resenaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.calificacion").value(5));
    }

    @Test
    void testAgregarResenaLibroNoEncontrado() throws Exception {
        String resenaJson = """
            {
                "usuarioId": 1,
                "calificacion": 4,
                "comentario": "Muy bueno"
            }
            """;

        mockMvc.perform(post("/api/libros/999/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resenaJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAgregarResenaCalificacionInvalida() throws Exception {
        String resenaJson = """
            {
                "usuarioId": 1,
                "calificacion": 10,
                "comentario": "Fuera de rango"
            }
            """;

        mockMvc.perform(post("/api/libros/1/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resenaJson))
                .andExpect(status().isBadRequest());
    }
}