package com.demilingua.backend.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contrasena;

    @Column(columnDefinition = "int default 5")
    private Integer vidas = 5;

    @Column(name = "racha_actual", columnDefinition = "int default 0")
    private Integer rachaActual = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id")
    private Division division;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ultima_recarga", columnDefinition = "datetime default current_timestamp")
    private Date ultimaRecarga = new Date();

    @Column(name = "fcm_token", length = 255)
    private String fcmToken; // Para notificaciones Push en la Fase 2/4

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Integer getVidas() { return vidas; }
    public void setVidas(Integer vidas) { this.vidas = vidas; }
    public Integer getRachaActual() { return rachaActual; }
    public void setRachaActual(Integer rachaActual) { this.rachaActual = rachaActual; }
    public Division getDivision() { return division; }
    public void setDivision(Division division) { this.division = division; }
    public Date getUltimaRecarga() { return ultimaRecarga; }
    public void setUltimaRecarga(Date ultimaRecarga) { this.ultimaRecarga = ultimaRecarga; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}