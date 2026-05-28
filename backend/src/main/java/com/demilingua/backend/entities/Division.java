package com.demilingua.backend.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "division")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "xp_minimo")
    private Integer xpMinimo;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getXpMinimo() { return xpMinimo; }
    public void setXpMinimo(Integer xpMinimo) { this.xpMinimo = xpMinimo; }
}