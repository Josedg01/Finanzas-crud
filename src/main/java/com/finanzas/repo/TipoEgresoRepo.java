package com.finanzas.repo;

import com.finanzas.entity.TipoEgreso;
import jakarta.ejb.Stateless;

@Stateless
public class TipoEgresoRepo extends GenericRepo<TipoEgreso> {
    public TipoEgresoRepo() {
        super(TipoEgreso.class);
    }
}
