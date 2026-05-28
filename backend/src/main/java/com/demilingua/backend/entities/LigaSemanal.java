package com.demilingua.backend.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "liga_semanal")
public class LigaSemanal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "xp_semanal", columnDefinition = "int default 0")
    private Integer xpSemanal = 0;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio_semana")
    private Date fechaInicioSemana;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Integer getXpSemanal() { return xpSemanal; }
    public void setXpSemanal(Integer xpSemanal) { this.xpSemanal = xpSemanal; }
    public Date getFechaInicioSemana() { return fechaInicioSemana; }
    public void setFechaInicioSemana(Date fechaInicioSemana) { this.fechaInicioSemana = fechaInicioSemana; }
}