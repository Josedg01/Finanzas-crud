package com.finanzas.repo;

import com.finanzas.entity.Transaccion;
import jakarta.ejb.Stateless;

import java.util.Date;
import java.util.List;

@Stateless
public class TransaccionRepo extends GenericRepo<Transaccion> {
    public TransaccionRepo() {
        super(Transaccion.class);
    }

    // ✅ NUEVO: reporte con filtros opcionales
    public List<Transaccion> buscarReporte(Long usuarioId, Date desde, Date hasta) {
        String jpql = "SELECT t FROM Transaccion t " +
                "WHERE (:uid IS NULL OR t.usuario.id = :uid) " +
                "AND (:d IS NULL OR t.fechaTransaccion >= :d) " +
                "AND (:h IS NULL OR t.fechaTransaccion <= :h) " +
                "ORDER BY t.fechaTransaccion DESC";

        return em.createQuery(jpql, Transaccion.class)
                .setParameter("uid", usuarioId)
                .setParameter("d", desde)
                .setParameter("h", hasta)
                .getResultList();
    }
}
