package com.demilingua.backend.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioTestId implements Serializable {

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "test_id")
    private Integer testId;

    public UsuarioTestId() {}

    public UsuarioTestId(Integer usuarioId, Integer testId) {
        this.usuarioId = usuarioId;
        this.testId = testId;
    }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public Integer getTestId() { return testId; }
    public void setTestId(Integer testId) { this.testId = testId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioTestId that = (UsuarioTestId) o;
        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(testId, that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, testId);
    }
}
