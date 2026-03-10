package com.finanzas.web.bean;

import com.finanzas.entity.TipoPago;
import com.finanzas.service.TipoPagoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class TipoPagoBean extends CrudBeanBase<TipoPago> {

    @Inject
    private TipoPagoService service;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @Override
    protected List<TipoPago> listar() {
        return service.listar();
    }

    @Override
    protected void crear(TipoPago t) {
        service.crear(t);
    }

    @Override
    protected TipoPago actualizar(TipoPago t) {
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) {
        service.eliminar(id);
    }

    @Override
    protected TipoPago nuevoInstance() {
        TipoPago t = new TipoPago();
        t.setEstado(true);
        return t;
    }

    @Override
    protected Long getId(TipoPago t) {
        return t.getId();
    }
}
