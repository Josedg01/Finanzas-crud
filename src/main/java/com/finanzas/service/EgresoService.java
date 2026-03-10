package com.finanzas.service;

import com.finanzas.entity.Egreso;
import com.finanzas.entity.RenglonEgreso;
import com.finanzas.entity.TipoEgreso;
import com.finanzas.entity.TipoPago;
import com.finanzas.repo.EgresoRepo;
import com.finanzas.repo.GenericRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EgresoService extends GenericService<Egreso> {

    @Inject
    private EgresoRepo r;

    @PersistenceContext(unitName = "FinanzasPU")
    private EntityManager em;

    @Override
    protected GenericRepo<Egreso> repo() {
        return r;
    }

    public TipoEgreso refTipoEgreso(Long id) {
        return em.getReference(TipoEgreso.class, id);
    }

    public RenglonEgreso refRenglonEgreso(Long id) {
        return em.getReference(RenglonEgreso.class, id);
    }

    public TipoPago refTipoPago(Long id) {
        return em.getReference(TipoPago.class, id);
    }
}
