package com.finanzas.repo;

import com.finanzas.entity.TipoIngreso;
import jakarta.ejb.Stateless;

@Stateless
public class TipoIngresoRepo extends GenericRepo<TipoIngreso> {
    public TipoIngresoRepo() {
        super(TipoIngreso.class);
    }
}
