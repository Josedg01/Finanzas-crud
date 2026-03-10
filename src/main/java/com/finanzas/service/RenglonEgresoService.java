package com.finanzas.service;

import com.finanzas.entity.RenglonEgreso;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.RenglonEgresoRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class RenglonEgresoService extends GenericService<RenglonEgreso> {

    @Inject
    private RenglonEgresoRepo r;

    @Override
    protected GenericRepo<RenglonEgreso> repo() {
        return r;
    }
}
