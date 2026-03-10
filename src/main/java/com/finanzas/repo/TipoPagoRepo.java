package com.finanzas.repo;

import com.finanzas.entity.TipoPago;
import jakarta.ejb.Stateless;

@Stateless
public class TipoPagoRepo extends GenericRepo<TipoPago> {
    public TipoPagoRepo() {
        super(TipoPago.class);
    }
}
