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

    // ✅ NUEVO: reporte
    public List<Transaccion> buscarReporte(Long usuarioId, Date desde, Date hasta) {
        return r.buscarReporte(usuarioId, desde, hasta);
    }
}
