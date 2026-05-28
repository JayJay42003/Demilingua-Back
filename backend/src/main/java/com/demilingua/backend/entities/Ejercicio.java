package com.demilingua.backend.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ejercicio")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(columnDefinition = "int default 0")
    private Integer puntuacion;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL)
    private List<ObjetoEjercicio> objetos;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Integer puntuacion) { this.puntuacion = puntuacion; }
    public List<ObjetoEjercicio> getObjetos() { return objetos; }
    public void setObjetos(List<ObjetoEjercicio> objetos) { this.objetos = objetos; }
}