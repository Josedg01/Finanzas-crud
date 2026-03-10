package com.finanzas.repo;

import com.finanzas.entity.Corte;
import jakarta.ejb.Stateless;

@Stateless
public class CorteRepo extends GenericRepo<Corte> {
    public CorteRepo() {
        super(Corte.class);
    }
}
