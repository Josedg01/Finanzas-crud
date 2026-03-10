package com.finanzas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TipoIngreso", schema = "finanzas")
public class TipoIngreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoIngresoId")
    private Long id;

    @Column(name = "Descripcion", nullable = false, length = 120, unique = true)
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @Override
    public String toString() { return descripcion; }


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TipoIngreso that = (TipoIngreso) o;
    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return 31;
}
}
