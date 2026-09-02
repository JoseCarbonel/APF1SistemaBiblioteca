package com.example.sistemabiblioteca.model;

import java.time.LocalDate;

public class Resena {
    private Long id;
    private Long libroId;
    private String comentario;
    private Integer calificacion; // 1 a 5
    private LocalDate fechaResena;

    public Resena() {}

    public Resena(Long id, Long libroId, String comentario, Integer calificacion, LocalDate fechaResena) {
        this.id = id;
        this.libroId = libroId;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fechaResena = fechaResena;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public LocalDate getFechaResena() { return fechaResena; }
    public void setFechaResena(LocalDate fechaResena) { this.fechaResena = fechaResena; }
}
