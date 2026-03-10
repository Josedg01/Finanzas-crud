package com.finanzas.web.bean;

import com.finanzas.entity.TipoIngreso;
import com.finanzas.service.TipoIngresoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class TipoIngresoBean extends CrudBeanBase<TipoIngreso> {

    @Inject
    private TipoIngresoService service;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @Override
    protected List<TipoIngreso> listar() {
        return service.listar();
    }

    @Override
    protected void crear(TipoIngreso t) {
        service.crear(t);
    }

    @Override
    protected TipoIngreso actualizar(TipoIngreso t) {
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) {
        service.eliminar(id);
    }

    @Override
    protected TipoIngreso nuevoInstance() {
        TipoIngreso t = new TipoIngreso();
        t.setEstado(true);
        return t;
    }

    @Override
    protected Long getId(TipoIngreso t) {
        return t.getId();
    }
}
