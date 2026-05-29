package com.demilingua.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "usuario_test")
public class UsuarioTest {

    @EmbeddedId
    private UsuarioTestId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testId")
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(columnDefinition = "int default 0")
    private Integer puntuacion = 0;

    // Getters and Setters
    public UsuarioTestId getId() { return id; }
    public void setId(UsuarioTestId id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
    public Integer getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Integer puntuacion) { this.puntuacion = puntuacion; }
}
