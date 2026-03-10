package com.finanzas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Egreso", schema = "finanzas")
public class Egreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EgresoId")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TipoEgresoId", nullable = false)
    private TipoEgreso tipoEgreso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RenglonEgresoId", nullable = false)
    private RenglonEgreso renglonEgreso;

    @ManyToOne
    @JoinColumn(name = "TipoPagoDefectoId")
    private TipoPago tipoPagoDefecto;

    @Column(name = "Descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoEgreso getTipoEgreso() { return tipoEgreso; }
    public void setTipoEgreso(TipoEgreso tipoEgreso) { this.tipoEgreso = tipoEgreso; }

    public RenglonEgreso getRenglonEgreso() { return renglonEgreso; }
    public void setRenglonEgreso(RenglonEgreso renglonEgreso) { this.renglonEgreso = renglonEgreso; }

    public TipoPago getTipoPagoDefecto() { return tipoPagoDefecto; }
    public void setTipoPagoDefecto(TipoPago tipoPagoDefecto) { this.tipoPagoDefecto = tipoPagoDefecto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return descripcion;
    }


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Egreso that = (Egreso) o;
    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return 31;
}
}
