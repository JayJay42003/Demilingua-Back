package com.demilingua.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioIdiomaId implements Serializable {

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "idioma_id")
    private Integer idiomaId;

    public UsuarioIdiomaId() {}

    public UsuarioIdiomaId(Integer usuarioId, Integer idiomaId) {
        this.usuarioId = usuarioId;
        this.idiomaId = idiomaId;
    }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public Integer getIdiomaId() { return idiomaId; }
    public void setIdiomaId(Integer idiomaId) { this.idiomaId = idiomaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioIdiomaId that = (UsuarioIdiomaId) o;
        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(idiomaId, that.idiomaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, idiomaId);
    }
}