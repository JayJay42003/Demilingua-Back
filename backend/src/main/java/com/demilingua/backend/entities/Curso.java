package com.demilingua.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idioma_id", nullable = false)
    private Idioma idioma;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 20)
    private String dificultad;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Idioma getIdioma() { return idioma; }
    public void setIdioma(Idioma idioma) { this.idioma = idioma; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
}