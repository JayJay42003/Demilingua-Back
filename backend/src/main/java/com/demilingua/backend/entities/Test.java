package com.demilingua.backend.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false, length = 100)
    private String titulo;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Ejercicio> ejercicios;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<Ejercicio> getEjercicios() { return ejercicios; }
    public void setEjercicios(List<Ejercicio> ejercicios) { this.ejercicios = ejercicios; }
}
