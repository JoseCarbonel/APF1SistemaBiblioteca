package com.example.sistemabiblioteca.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PrestamoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // --- ENDPOINT 5: REGISTRAR PRÉSTAMO ---
    @Test
    void testRegistrarExito() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":1,\"libroId\":2}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testRegistrarUsuarioNoExiste() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":99,\"libroId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegistrarSinLibro() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuarioId\":1}"))
                .andExpect(status().isBadRequest());
    }

    // --- ENDPOINT 6: LISTAR POR USUARIO ---
    @Test
    void testListarPorUsuarioExito() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarPorUsuarioNoExiste() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarPorUsuarioDetalle() throws Exception {
        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(jsonPath("$[0].usuarioId").value(1));
    }

    // --- ENDPOINT 7: DEVOLVER LIBRO ---
    @Test
    void testDevolverExito() throws Exception {
        // Al usar @DirtiesContext, el préstamo inicial por defecto siempre tendrá ID 1
        mockMvc.perform(put("/api/prestamos/1/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("DEVUELTO"));
    }

    @Test
    void testDevolverNoEncontrado() throws Exception {
        mockMvc.perform(put("/api/prestamos/99/devolver"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDevolverYaDevuelto() throws Exception {
        // Primera devolución
        mockMvc.perform(put("/api/prestamos/1/devolver"));

        // Segunda devolución (falla con 400 Bad Request)
        mockMvc.perform(put("/api/prestamos/1/devolver"))
                .andExpect(status().isBadRequest());
    }

    // --- ENDPOINT 8: CANCELAR PRÉSTAMO ---
    @Test
    void testCancelarExito() throws Exception {
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCancelarNoEncontrado() throws Exception {
        mockMvc.perform(delete("/api/prestamos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCancelarFinalizadoError() throws Exception {
        mockMvc.perform(put("/api/prestamos/1/devolver"));
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isBadRequest());
    }
}