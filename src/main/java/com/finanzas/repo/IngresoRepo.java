package com.finanzas.repo;

import com.finanzas.entity.Ingreso;
import jakarta.ejb.Stateless;

@Stateless
public class IngresoRepo extends GenericRepo<Ingreso> {
    public IngresoRepo() {
        super(Ingreso.class);
    }
}
