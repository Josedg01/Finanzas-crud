package com.finanzas.service;

import com.finanzas.entity.TipoIngreso;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.TipoIngresoRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TipoIngresoService extends GenericService<TipoIngreso> {

    @Inject
    private TipoIngresoRepo r;

    @Override
    protected GenericRepo<TipoIngreso> repo() {
        return r;
    }
}
