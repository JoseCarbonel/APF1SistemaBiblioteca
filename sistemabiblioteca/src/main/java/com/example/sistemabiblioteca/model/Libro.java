package com.example.sistemabiblioteca.model;

public class Libro {
    private Long id;
    private String titulo;
    private String isbn;
    private String categoria;
    private Boolean disponible;

    public Libro() {}

    public Libro(Long id, String titulo, String isbn, String categoria, Boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}
