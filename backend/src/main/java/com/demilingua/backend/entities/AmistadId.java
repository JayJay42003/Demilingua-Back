package com.demilingua.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AmistadId implements Serializable {

    @Column(name = "usuario_id_1")
    private Integer usuarioId1;

    @Column(name = "usuario_id_2")
    private Integer usuarioId2;

    public AmistadId() {}

    public AmistadId(Integer usuarioId1, Integer usuarioId2) {
        this.usuarioId1 = usuarioId1;
        this.usuarioId2 = usuarioId2;
    }

    public Integer getUsuarioId1() { return usuarioId1; }
    public void setUsuarioId1(Integer usuarioId1) { this.usuarioId1 = usuarioId1; }
    public Integer getUsuarioId2() { return usuarioId2; }
    public void setUsuarioId2(Integer usuarioId2) { this.usuarioId2 = usuarioId2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmistadId that = (AmistadId) o;
        return Objects.equals(usuarioId1, that.usuarioId1) && Objects.equals(usuarioId2, that.usuarioId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId1, usuarioId2);
    }
}