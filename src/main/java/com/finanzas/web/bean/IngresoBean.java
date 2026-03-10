package com.finanzas.web.bean;

import com.finanzas.entity.Ingreso;
import com.finanzas.entity.TipoIngreso;
import com.finanzas.service.IngresoService;
import com.finanzas.service.TipoIngresoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class IngresoBean extends CrudBeanBase<Ingreso> {

    @Inject private IngresoService service;
    @Inject private TipoIngresoService tipoIngresoService;

    private List<TipoIngreso> tiposIngreso;


    private Long nuevoTipoIngresoId;
    private Long editTipoIngresoId;

    @PostConstruct
    public void postConstruct() {
        tiposIngreso = tipoIngresoService.listar();
        init();
    }


    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    private void markValidationFailed() {
        FacesContext.getCurrentInstance().validationFailed();
    }


    public List<TipoIngreso> getTiposIngreso() {
        return tiposIngreso;
    }


    public Long getNuevoTipoIngresoId() { return nuevoTipoIngresoId; }
    public void setNuevoTipoIngresoId(Long v) { nuevoTipoIngresoId = v; }

    public Long getEditTipoIngresoId() { return editTipoIngresoId; }
    public void setEditTipoIngresoId(Long v) { editTipoIngresoId = v; }


    @Override
    protected List<Ingreso> listar() { return service.listar(); }

    @Override
    protected void crear(Ingreso t) { service.crear(t); }

    @Override
    protected Ingreso actualizar(Ingreso t) { return service.actualizar(t); }

    @Override
    protected void eliminarPorId(Long id) { service.eliminar(id); }

    @Override
    protected Ingreso nuevoInstance() {
        Ingreso i = new Ingreso();
        i.setEstado(true);

        nuevoTipoIngresoId = (tiposIngreso != null && !tiposIngreso.isEmpty())
                ? tiposIngreso.get(0).getId()
                : null;

        return i;
    }

    @Override
    public void prepararNuevo() {
        super.prepararNuevo();
        if (nuevoTipoIngresoId == null && tiposIngreso != null && !tiposIngreso.isEmpty()) {
            nuevoTipoIngresoId = tiposIngreso.get(0).getId();
        }
    }

    public void prepararEdit() {
        if (seleccionado == null) return;
        editTipoIngresoId = (seleccionado.getTipoIngreso() != null)
                ? seleccionado.getTipoIngreso().getId()
                : null;
    }

    @Override
    public void guardarNuevo() {
        try {
            if (nuevo == null) return;

            if (nuevoTipoIngresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un Tipo de Ingreso.");
                markValidationFailed();
                return;
            }

            if (nuevo.getDescripcion() == null || nuevo.getDescripcion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite una descripción.");
                markValidationFailed();
                return;
            }

            nuevo.setTipoIngreso(service.refTipoIngreso(nuevoTipoIngresoId));

            crear(nuevo);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Ingreso guardado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override
    public void guardarEdicion() {
        try {
            if (seleccionado == null) return;

            if (editTipoIngresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un Tipo de Ingreso.");
                markValidationFailed();
                return;
            }

            if (seleccionado.getDescripcion() == null || seleccionado.getDescripcion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite una descripción.");
                markValidationFailed();
                return;
            }

            seleccionado.setTipoIngreso(service.refTipoIngreso(editTipoIngresoId));

            actualizar(seleccionado);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Ingreso actualizado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override
    protected Long getId(Ingreso t) { return t.getId(); }
}
