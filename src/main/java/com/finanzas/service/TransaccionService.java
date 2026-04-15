package com.finanzas.service;

import com.finanzas.entity.Egreso;
import com.finanzas.entity.Ingreso;
import com.finanzas.entity.TipoPago;
import com.finanzas.entity.Transaccion;
import com.finanzas.entity.Usuario;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.TransaccionRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Stateless
public class TransaccionService extends GenericService<Transaccion> {

    @Inject
    private TransaccionRepo r;

    @PersistenceContext(unitName = "FinanzasPU")
    private EntityManager em;

    @Override
    protected GenericRepo<Transaccion> repo() {
        return r;
    }

    // ✅ CAMBIO: validar antes de crear
    @Override
    public void crear(Transaccion t) {
        validarLimiteEgreso(t, null);
        super.crear(t);
    }

    // ✅ CAMBIO: validar antes de actualizar (excluye la transacción actual del SUM)
    @Override
    public Transaccion actualizar(Transaccion t) {
        validarLimiteEgreso(t, t != null ? t.getId() : null);
        return super.actualizar(t);
    }

    /**
     * Valida límite de egresos por usuario.
     * Regla: si TipoTransaccion = EGRESO y Usuario.LimiteEgresos != null:
     *   SUM(egresos del usuario) + montoActual <= limite
     * Si excede -> IllegalArgumentException (bloquea guardado)
     */
    private void validarLimiteEgreso(Transaccion t, Long excludeTransaccionId) {
        if (t == null) return;

        // Solo aplica para EGRESO
        if (!"EGRESO".equalsIgnoreCase(t.getTipoTransaccion())) return;

        if (t.getUsuario() == null || t.getUsuario().getId() == null) return;
        if (t.getMonto() == null) return;

        Long usuarioId = t.getUsuario().getId();

        // Leer usuario REAL desde BD para obtener LimiteEgresos (no usar getReference aquí)
        Usuario u = em.find(Usuario.class, usuarioId);
        if (u == null) return;

        BigDecimal limite = u.getLimiteEgresos();
        if (limite == null) return; // sin límite => permite

        // SUMA de egresos existentes (opcionalmente solo estado=1 en el repo)
        BigDecimal sumaActual = r.sumEgresosByUsuario(usuarioId, excludeTransaccionId);
        if (sumaActual == null) sumaActual = BigDecimal.ZERO;

        BigDecimal total = sumaActual.add(t.getMonto());

        if (total.compareTo(limite) > 0) {
            throw new IllegalArgumentException(
                    "Límite de egresos excedido. " +
                    "Límite: " + limite +
                    ", egresos actuales: " + sumaActual +
                    ", intento: " + t.getMonto() +
                    ", total: " + total
            );
        }
    }

    // ====== refs (igual que antes) ======
    public Usuario refUsuario(Long id) {
        return em.getReference(Usuario.class, id);
    }

    public TipoPago refTipoPago(Long id) {
        return em.getReference(TipoPago.class, id);
    }

    public Egreso refEgreso(Long id) {
        return em.getReference(Egreso.class, id);
    }

    public Ingreso refIngreso(Long id) {
        return em.getReference(Ingreso.class, id);
    }

    //reporte (igual que antes)
    public List<Transaccion> buscarReporte(Long usuarioId, Date desde, Date hasta) {
        return r.buscarReporte(usuarioId, desde, hasta);
    }
}