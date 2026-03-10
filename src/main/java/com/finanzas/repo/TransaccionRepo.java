package com.finanzas.repo;

import com.finanzas.entity.Transaccion;
import jakarta.ejb.Stateless;

@Stateless
public class TransaccionRepo extends GenericRepo<Transaccion> {
    public TransaccionRepo() {
        super(Transaccion.class);
    }
}
