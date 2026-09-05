package com.example.sistemabiblioteca.model;

public class Resena {
    private Long id;
    private Long libroId;
    private Integer calificacion;
    private String comentario;

    public Resena() {}
    public Resena(Long id, Long libroId, Integer calificacion, String comentario) {
        this.id = id;
        this.libroId = libroId;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}