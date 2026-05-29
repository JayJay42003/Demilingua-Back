package com.demilingua.backend.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "amistad")
public class Amistad {

    @EmbeddedId
    private AmistadId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId1")
    @JoinColumn(name = "usuario_id_1")
    private Usuario usuario1;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId2")
    @JoinColumn(name = "usuario_id_2")
    private Usuario usuario2;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_solicitud", columnDefinition = "datetime default current_timestamp")
    private Date fechaSolicitud = new Date();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDIENTE', 'ACEPTADO')")
    private EstadoAmistad estado = EstadoAmistad.PENDIENTE;

    public enum EstadoAmistad {
        PENDIENTE, ACEPTADO
    }

    // Getters and Setters
    public AmistadId getId() { return id; }
    public void setId(AmistadId id) { this.id = id; }
    public Usuario getUsuario1() { return usuario1; }
    public void setUsuario1(Usuario usuario1) { this.usuario1 = usuario1; }
    public Usuario getUsuario2() { return usuario2; }
    public void setUsuario2(Usuario usuario2) { this.usuario2 = usuario2; }
    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public EstadoAmistad getEstado() { return estado; }
    public void setEstado(EstadoAmistad estado) { this.estado = estado; }
}
