package com.finanzas.repo;

import com.finanzas.entity.Egreso;
import jakarta.ejb.Stateless;

@Stateless
public class EgresoRepo extends GenericRepo<Egreso> {
    public EgresoRepo() {
        super(Egreso.class);
    }
}
