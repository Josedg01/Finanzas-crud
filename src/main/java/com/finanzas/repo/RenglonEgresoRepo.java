package com.finanzas.repo;

import com.finanzas.entity.RenglonEgreso;
import jakarta.ejb.Stateless;

@Stateless
public class RenglonEgresoRepo extends GenericRepo<RenglonEgreso> {
    public RenglonEgresoRepo() {
        super(RenglonEgreso.class);
    }
}
