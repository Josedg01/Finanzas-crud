package com.finanzas.repo;

import com.finanzas.entity.Transaccion;
import jakarta.ejb.Stateless;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

@Stateless
public class TransaccionRepo extends GenericRepo<Transaccion> {

    public TransaccionRepo() {
        super(Transaccion.class);
    }

    // reporte con filtros opcionales
    public List<Transaccion> buscarReporte(Long usuarioId, Date desde, Date hasta) {
        String jpql = "SELECT t FROM Transaccion t "
                + "WHERE (:uid IS NULL OR t.usuario.id = :uid) "
                + "AND (:d IS NULL OR t.fechaTransaccion >= :d) "
                + "AND (:h IS NULL OR t.fechaTransaccion <= :h) "
                + "ORDER BY t.fechaTransaccion DESC";

        return em.createQuery(jpql, Transaccion.class)
                .setParameter("uid", usuarioId)
                .setParameter("d", desde)
                .setParameter("h", hasta)
                .getResultList();
    }

    // TransaccionRepo.java
    public BigDecimal sumEgresosByUsuario(Long usuarioId, Long excludeTransaccionId) {
        if (usuarioId == null) {
            return BigDecimal.ZERO;
        }

        String jpql
                = "SELECT COALESCE(SUM(t.monto), 0) "
                + "FROM Transaccion t "
                + "WHERE t.usuario.id = :uid "
                + "  AND t.tipoTransaccion = 'EGRESO' "
                + "  AND t.estado = TRUE "
                + (excludeTransaccionId != null ? " AND t.id <> :ex " : "");

        var q = em.createQuery(jpql, BigDecimal.class)
                .setParameter("uid", usuarioId);

        if (excludeTransaccionId != null) {
            q.setParameter("ex", excludeTransaccionId);
        }

        return q.getSingleResult();
    }
}
