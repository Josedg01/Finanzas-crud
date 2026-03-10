package com.finanzas.service;

import com.finanzas.entity.Corte;
import com.finanzas.entity.Usuario;
import com.finanzas.repo.CorteRepo;
import com.finanzas.repo.GenericRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CorteService extends GenericService<Corte> {

    @Inject
    private CorteRepo r;

    @PersistenceContext(unitName = "FinanzasPU")
    private EntityManager em;

    @Override
    protected GenericRepo<Corte> repo() {
        return r;
    }

    /** Referencia (proxy) sin SELECT completo */
    public Usuario refUsuario(Long id) {
        return em.getReference(Usuario.class, id);
    }
}
