package com.finanzas.service;

import com.finanzas.entity.Ingreso;
import com.finanzas.entity.TipoIngreso;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.IngresoRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class IngresoService extends GenericService<Ingreso> {

    @Inject
    private IngresoRepo r;

    @PersistenceContext(unitName = "FinanzasPU")
    private EntityManager em;

    @Override
    protected GenericRepo<Ingreso> repo() {
        return r;
    }


    public TipoIngreso refTipoIngreso(Long id) {
        return em.getReference(TipoIngreso.class, id);
    }
}
