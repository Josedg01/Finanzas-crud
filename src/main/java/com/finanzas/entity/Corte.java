package com.finanzas.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Corte", schema = "finanzas")
public class Corte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CorteId")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "UsuarioId", nullable = false)
    private Usuario usuario;

    @Column(name = "Anio", nullable = false)
    private Short anio;

    @Column(name = "Mes", nullable = false)
    private Short mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "FechaCorte", nullable = false)
    private Date fechaCorte;

    @Column(name = "BalanceInicial", nullable = false, precision = 14, scale = 2)
    private BigDecimal balanceInicial = BigDecimal.ZERO;

    @Column(name = "TotalIngresos", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalIngresos = BigDecimal.ZERO;

    @Column(name = "TotalEgresos", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalEgresos = BigDecimal.ZERO;

    @Column(name = "BalanceCorte", nullable = false, precision = 14, scale = 2)
    private BigDecimal balanceCorte = BigDecimal.ZERO;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Short getAnio() { return anio; }
    public void setAnio(Short anio) { this.anio = anio; }

    public Short getMes() { return mes; }
    public void setMes(Short mes) { this.mes = mes; }

    public Date getFechaCorte() { return fechaCorte; }
    public void setFechaCorte(Date fechaCorte) { this.fechaCorte = fechaCorte; }

    public BigDecimal getBalanceInicial() { return balanceInicial; }
    public void setBalanceInicial(BigDecimal balanceInicial) { this.balanceInicial = balanceInicial; }

    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    public BigDecimal getBalanceCorte() { return balanceCorte; }
    public void setBalanceCorte(BigDecimal balanceCorte) { this.balanceCorte = balanceCorte; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    @Override
    public String toString() {
        return anio + "-" + mes + " (" + (usuario == null ? "" : usuario.getNombre()) + ")";
    }


@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Corte that = (Corte) o;
    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return 31;
}
}
