package com.example.sistemabiblioteca.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class LibroControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // --- ENDPOINT 1: CREAR LIBRO ---
    @Test
    void testCrearExito() throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Rayuela\",\"isbn\":\"978-3\",\"categoria\":\"Ficcion\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCrearErrorSinTitulo() throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"\",\"isbn\":\"978-3\",\"categoria\":\"Ficcion\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearErrorBodyVacio() throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // --- ENDPOINT 2: LISTAR LIBROS ---
    @Test
    void testListarExito() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarTipoContenido() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListarNoVacio() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --- ENDPOINT 3: BUSCAR POR ID ---
    @Test
    void testBuscarPorIdExito() throws Exception {
        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testBuscarPorIdNoEncontrado() throws Exception {
        mockMvc.perform(get("/api/libros/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarPorIdInvalido() throws Exception {
        mockMvc.perform(get("/api/libros/abc"))
                .andExpect(status().isBadRequest());
    }

    // --- ENDPOINT 4: AGREGAR RESEÑA ---
    @Test
    void testResenaExito() throws Exception {
        mockMvc.perform(post("/api/libros/1/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"calificacion\":5,\"comentario\":\"Excelente lectura\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testResenaLibroInexistente() throws Exception {
        mockMvc.perform(post("/api/libros/99/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"calificacion\":5,\"comentario\":\"Muy bueno\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testResenaCalificacionInvalida() throws Exception {
        mockMvc.perform(post("/api/libros/1/reseñas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"calificacion\":10,\"comentario\":\"Invalido\"}"))
                .andExpect(status().isBadRequest());
    }
}