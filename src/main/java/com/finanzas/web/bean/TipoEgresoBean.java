package com.finanzas.web.bean;

import com.finanzas.entity.TipoEgreso;
import com.finanzas.service.TipoEgresoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class TipoEgresoBean extends CrudBeanBase<TipoEgreso> {

    @Inject
    private TipoEgresoService service;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    @Override
    protected List<TipoEgreso> listar() {
        return service.listar();
    }

    @Override
    protected void crear(TipoEgreso t) {
        service.crear(t);
    }

    @Override
    protected TipoEgreso actualizar(TipoEgreso t) {
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) {
        service.eliminar(id);
    }

    @Override
    protected TipoEgreso nuevoInstance() {
        TipoEgreso t = new TipoEgreso();
        t.setEstado(true);
        return t;
    }

    @Override
    protected Long getId(TipoEgreso t) {
        return t.getId();
    }
}
