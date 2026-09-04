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
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- OPERACIÓN 5: POST /api/prestamos (Registrar Préstamo) ---

    @Test
    void testRegistrarPrestamoExitoso() throws Exception {
        String prestamoJson = """
            {
                "usuarioId": 2,
                "libroId": 2
            }
            """;

        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(prestamoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testRegistrarPrestamoUsuarioNoExiste() throws Exception {
        String prestamoJson = """
            {
                "usuarioId": 999,
                "libroId": 2
            }
            """;

        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(prestamoJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegistrarPrestamoLibroYaPrestado() throws Exception {
        String prestamoJson = """
            {
                "usuarioId": 2,
                "libroId": 1
            }
            """;

        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(prestamoJson))
                .andExpect(status().isBadRequest());
    }

    // --- OPERACIÓN 6: GET /api/prestamos/usuario/{usuarioId} (Consultar Préstamos por Usuario) ---

    @Test
    void testObtenerPrestamosPorUsuarioExitoso() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(1));
    }

    @Test
    void testObtenerPrestamosPorUsuarioNoEncontrado() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPrestamosPorUsuarioSinPrestamosActivos() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- OPERACIÓN 7: PUT /api/prestamos/{id}/devolver (Devolver Libro) ---

    @Test
    void testDevolverLibroExitoso() throws Exception {
        mockMvc.perform(put("/api/prestamos/1/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("DEVUELTO"));
    }

    @Test
    void testDevolverLibroNoEncontrado() throws Exception {
        mockMvc.perform(put("/api/prestamos/999/devolver"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDevolverLibroYaDevuelto() throws Exception {
        // Primera devolución
        mockMvc.perform(put("/api/prestamos/1/devolver"));

        // Intentar devolver por segunda vez
        mockMvc.perform(put("/api/prestamos/1/devolver"))
                .andExpect(status().isBadRequest());
    }

    // --- OPERACIÓN 8: DELETE /api/prestamos/{id} (Cancelar Préstamo) ---

    @Test
    void testCancelarPrestamoExitoso() throws Exception {
        // Creamos un préstamo dinámico para luego eliminarlo
        String prestamoJson = """
            {
                "usuarioId": 1,
                "libroId": 2
            }
            """;

        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(prestamoJson));

        mockMvc.perform(delete("/api/prestamos/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCancelarPrestamoNoEncontrado() throws Exception {
        mockMvc.perform(delete("/api/prestamos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCancelarPrestamoYaDevueltoError() throws Exception {
        // Devolvemos el préstamo 1 primero
        mockMvc.perform(put("/api/prestamos/1/devolver"));

        // Intentamos cancelar un préstamo ya finalizado/devuelto
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isBadRequest());
    }
}