package com.finanzas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Ingreso", schema = "finanzas")
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IngresoId")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TipoIngresoId", nullable = false)
    private TipoIngreso tipoIngreso;

    @Column(name = "Descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "Institucion", length = 160)
    private String institucion;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoIngreso getTipoIngreso() { return tipoIngreso; }
    public void setTipoIngreso(TipoIngreso tipoIngreso) { this.tipoIngreso = tipoIngreso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @Override
    public String toString() { return descripcion; }


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ingreso that = (Ingreso) o;
    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return 31;
}
}
