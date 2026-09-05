package com.example.sistemabiblioteca.model;

public class Usuario {
    private Long id;
    private String nombre;
    private String correo;
    private String tipo;

    public Usuario() {}
    public Usuario(Long id, String nombre, String correo, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}