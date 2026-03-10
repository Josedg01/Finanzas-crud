package com.finanzas.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Transaccion", schema = "finanzas")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransaccionId")
    private Long id;

    @Column(name = "TipoTransaccion", nullable = false, length = 10)
    private String tipoTransaccion; // INGRESO / EGRESO

    @ManyToOne(optional = false)
    @JoinColumn(name = "UsuarioId", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "EgresoId")
    private Egreso egreso;

    @ManyToOne
    @JoinColumn(name = "IngresoId")
    private Ingreso ingreso;

    @ManyToOne
    @JoinColumn(name = "TipoPagoId")
    private TipoPago tipoPago;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FechaTransaccion", nullable = false)
    private Date fechaTransaccion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FechaRegistro", nullable = false)
    private Date fechaRegistro = new Date();

    @Column(name = "Monto", nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    @Column(name = "TarjetaCR", length = 40)
    private String tarjetaCR;

    @Column(name = "Comentario", length = 300)
    private String comentario;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Egreso getEgreso() { return egreso; }
    public void setEgreso(Egreso egreso) { this.egreso = egreso; }

    public Ingreso getIngreso() { return ingreso; }
    public void setIngreso(Ingreso ingreso) { this.ingreso = ingreso; }

    public TipoPago getTipoPago() { return tipoPago; }
    public void setTipoPago(TipoPago tipoPago) { this.tipoPago = tipoPago; }

    public Date getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(Date fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getTarjetaCR() { return tarjetaCR; }
    public void setTarjetaCR(String tarjetaCR) { this.tarjetaCR = tarjetaCR; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return (tipoTransaccion == null ? "" : tipoTransaccion) + " - " + (monto == null ? "" : monto);
    }


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaccion that = (Transaccion) o;
    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return 31;
}
}
