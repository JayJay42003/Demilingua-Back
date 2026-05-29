package com.demilingua.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "objeto_ejercicio")
public class ObjetoEjercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejercicio_id", nullable = false)
    private Ejercicio ejercicio;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "respuesta_correcta", nullable = false, columnDefinition = "TEXT")
    private String respuestaCorrecta;

    @Column(columnDefinition = "LONGTEXT")
    private String opciones; // Guardado como String (JSON) para simplificar

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Ejercicio getEjercicio() { return ejercicio; }
    public void setEjercicio(Ejercicio ejercicio) { this.ejercicio = ejercicio; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
    public String getOpciones() { return opciones; }
    public void setOpciones(String opciones) { this.opciones = opciones; }
}
