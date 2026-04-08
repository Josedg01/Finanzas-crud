package com.finanzas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Usuario", schema = "finanzas")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioId")
    private Long id;

    @Column(name = "Nombre", nullable = false, length = 140)
    private String nombre;

    // ✅ CAMBIO: Cedula ahora debe permitir NULL (porque JURIDICA no la usa)
    // También quité unique=true porque ahora se maneja con índice filtrado en SQL Server
    @Column(name = "Cedula", nullable = true, length = 20)
    private String cedula;

    // ✅ NUEVO: RNC (solo aplica a JURIDICA)
    @Column(name = "RNC", nullable = true, length = 9)
    private String rnc;

    /**
     * Credencial de acceso (usuario/login). Se guarda en BD.
     */
    @Column(name = "UsuarioLogin", nullable = false, length = 60, unique = true)
    private String usuarioLogin;

    /** BCrypt hash */
    @Column(name = "PasswordHash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "LimiteEgresos", precision = 14, scale = 2)
    private BigDecimal limiteEgresos;

    // ✅ NUEVO: Balance del periodo (lo mantiene un trigger en BD)
    @Column(name = "BalancePeriodo", nullable = false, precision = 14, scale = 2)
    private BigDecimal balancePeriodo = BigDecimal.ZERO;

    @Column(name = "TipoPersona", nullable = false, length = 10)
    private String tipoPersona; // FISICA / JURIDICA

    @Column(name = "FechaCorteDia", nullable = false)
    private Integer fechaCorteDia = 1;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    // ✅ NUEVO getters/setters RNC
    public String getRnc() { return rnc; }
    public void setRnc(String rnc) { this.rnc = rnc; }

    public String getUsuarioLogin() { return usuarioLogin; }
    public void setUsuarioLogin(String usuarioLogin) { this.usuarioLogin = usuarioLogin; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public BigDecimal getLimiteEgresos() { return limiteEgresos; }
    public void setLimiteEgresos(BigDecimal limiteEgresos) { this.limiteEgresos = limiteEgresos; }

    // ✅ NUEVO
    public BigDecimal getBalancePeriodo() { return balancePeriodo; }
    public void setBalancePeriodo(BigDecimal balancePeriodo) { this.balancePeriodo = balancePeriodo; }

    public String getTipoPersona() { return tipoPersona; }
    public void setTipoPersona(String tipoPersona) { this.tipoPersona = tipoPersona; }

    public Integer getFechaCorteDia() { return fechaCorteDia; }
    public void setFechaCorteDia(Integer fechaCorteDia) { this.fechaCorteDia = fechaCorteDia; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    // ✅ CAMBIO: toString ahora soporta Cedula/RNC null (no rompe)
    @Override
    public String toString() {
        String doc = (cedula != null && !cedula.isBlank())
                ? cedula
                : (rnc != null && !rnc.isBlank() ? rnc : "-");
        return nombre + " (" + doc + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario that = (Usuario) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}