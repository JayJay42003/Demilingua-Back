package com.demilingua.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "usuario_idioma")
public class UsuarioIdioma {

    @EmbeddedId
    private UsuarioIdiomaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idiomaId")
    @JoinColumn(name = "idioma_id")
    private Idioma idioma;

    @Column(nullable = false)
    private Integer puntos = 0;

    // Getters and Setters
    public UsuarioIdiomaId getId() { return id; }
    public void setId(UsuarioIdiomaId id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Idioma getIdioma() { return idioma; }
    public void setIdioma(Idioma idioma) { this.idioma = idioma; }
    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }
}
