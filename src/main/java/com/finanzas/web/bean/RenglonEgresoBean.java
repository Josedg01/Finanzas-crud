package com.finanzas.web.bean;

import com.finanzas.entity.RenglonEgreso;
import com.finanzas.service.RenglonEgresoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class RenglonEgresoBean extends CrudBeanBase<RenglonEgreso> {

    @Inject
    private RenglonEgresoService service;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @Override
    protected List<RenglonEgreso> listar() {
        return service.listar();
    }

    @Override
    protected void crear(RenglonEgreso t) {
        service.crear(t);
    }

    @Override
    protected RenglonEgreso actualizar(RenglonEgreso t) {
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) {
        service.eliminar(id);
    }

    @Override
    protected RenglonEgreso nuevoInstance() {
        RenglonEgreso t = new RenglonEgreso();
        t.setEstado(true);
        return t;
    }

    @Override
    protected Long getId(RenglonEgreso t) {
        return t.getId();
    }
}
