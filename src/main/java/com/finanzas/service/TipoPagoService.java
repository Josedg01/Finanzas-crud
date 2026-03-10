package com.finanzas.service;

import com.finanzas.entity.TipoPago;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.TipoPagoRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TipoPagoService extends GenericService<TipoPago> {

    @Inject
    private TipoPagoRepo r;

    @Override
    protected GenericRepo<TipoPago> repo() {
        return r;
    }
}
