package com.finanzas.service;

import com.finanzas.entity.TipoEgreso;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.TipoEgresoRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TipoEgresoService extends GenericService<TipoEgreso> {

    @Inject
    private TipoEgresoRepo r;

    @Override
    protected GenericRepo<TipoEgreso> repo() {
        return r;
    }
}
