package com.example.sistemabiblioteca.model;

public class Prestamo {
    private Long id;
    private Long usuarioId;
    private Long libroId;
    private String estado;

    public Prestamo() {}
    public Prestamo(Long id, Long usuarioId, Long libroId, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}