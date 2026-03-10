package com.finanzas.web.bean;

import com.finanzas.entity.*;
import com.finanzas.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class EgresoBean extends CrudBeanBase<Egreso> {

    @Inject private EgresoService service;
    @Inject private TipoEgresoService tipoEgresoService;
    @Inject private RenglonEgresoService renglonEgresoService;
    @Inject private TipoPagoService tipoPagoService;

    private List<TipoEgreso> tiposEgreso;
    private List<RenglonEgreso> renglonesEgreso;
    private List<TipoPago> tiposPago;

    // ====== IDs para selects (evita converters) ======
    private Long nuevoTipoEgresoId;
    private Long nuevoRenglonEgresoId;
    private Long nuevoTipoPagoDefectoId; // opcional

    private Long editTipoEgresoId;
    private Long editRenglonEgresoId;
    private Long editTipoPagoDefectoId; // opcional

    @PostConstruct
    public void postConstruct() {
        tiposEgreso = tipoEgresoService.listar();
        renglonesEgreso = renglonEgresoService.listar();
        tiposPago = tipoPagoService.listar();
        init();
    }

    // ===================== Helpers =====================
    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    private void markValidationFailed() {
        FacesContext.getCurrentInstance().validationFailed();
    }

    // ===================== Listas para combos =====================
    public List<TipoEgreso> getTiposEgreso() { return tiposEgreso; }
    public List<RenglonEgreso> getRenglonesEgreso() { return renglonesEgreso; }
    public List<TipoPago> getTiposPago() { return tiposPago; }

    // ===================== Getters/Setters IDs =====================
    public Long getNuevoTipoEgresoId() { return nuevoTipoEgresoId; }
    public void setNuevoTipoEgresoId(Long v) { nuevoTipoEgresoId = v; }

    public Long getNuevoRenglonEgresoId() { return nuevoRenglonEgresoId; }
    public void setNuevoRenglonEgresoId(Long v) { nuevoRenglonEgresoId = v; }

    public Long getNuevoTipoPagoDefectoId() { return nuevoTipoPagoDefectoId; }
    public void setNuevoTipoPagoDefectoId(Long v) { nuevoTipoPagoDefectoId = v; }

    public Long getEditTipoEgresoId() { return editTipoEgresoId; }
    public void setEditTipoEgresoId(Long v) { editTipoEgresoId = v; }

    public Long getEditRenglonEgresoId() { return editRenglonEgresoId; }
    public void setEditRenglonEgresoId(Long v) { editRenglonEgresoId = v; }

    public Long getEditTipoPagoDefectoId() { return editTipoPagoDefectoId; }
    public void setEditTipoPagoDefectoId(Long v) { editTipoPagoDefectoId = v; }

    @Override protected List<Egreso> listar() { return service.listar(); }
    @Override protected void crear(Egreso t) { service.crear(t); }
    @Override protected Egreso actualizar(Egreso t) { return service.actualizar(t); }
    @Override protected void eliminarPorId(Long id) { service.eliminar(id); }

    @Override
    protected Egreso nuevoInstance() {
        Egreso e = new Egreso();
        e.setEstado(true);


        nuevoTipoEgresoId = (tiposEgreso != null && !tiposEgreso.isEmpty()) ? tiposEgreso.get(0).getId() : null;
        nuevoRenglonEgresoId = (renglonesEgreso != null && !renglonesEgreso.isEmpty()) ? renglonesEgreso.get(0).getId() : null;
        nuevoTipoPagoDefectoId = null; // opcional

        return e;
    }

    @Override
    public void prepararNuevo() {
        super.prepararNuevo();

        if (nuevoTipoEgresoId == null && tiposEgreso != null && !tiposEgreso.isEmpty()) {
            nuevoTipoEgresoId = tiposEgreso.get(0).getId();
        }
        if (nuevoRenglonEgresoId == null && renglonesEgreso != null && !renglonesEgreso.isEmpty()) {
            nuevoRenglonEgresoId = renglonesEgreso.get(0).getId();
        }
        nuevoTipoPagoDefectoId = null;
    }

    public void prepararEdit() {
        if (seleccionado == null) return;

        editTipoEgresoId = (seleccionado.getTipoEgreso() != null) ? seleccionado.getTipoEgreso().getId() : null;
        editRenglonEgresoId = (seleccionado.getRenglonEgreso() != null) ? seleccionado.getRenglonEgreso().getId() : null;
        editTipoPagoDefectoId = (seleccionado.getTipoPagoDefecto() != null) ? seleccionado.getTipoPagoDefecto().getId() : null;
    }

    @Override
    public void guardarNuevo() {
        try {
            if (nuevo == null) return;

            if (nuevoTipoEgresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un tipo de egreso.");
                markValidationFailed();
                return;
            }
            if (nuevoRenglonEgresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un renglón de egreso.");
                markValidationFailed();
                return;
            }
            if (nuevo.getDescripcion() == null || nuevo.getDescripcion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite una descripción.");
                markValidationFailed();
                return;
            }


            nuevo.setTipoEgreso(service.refTipoEgreso(nuevoTipoEgresoId));
            nuevo.setRenglonEgreso(service.refRenglonEgreso(nuevoRenglonEgresoId));

            if (nuevoTipoPagoDefectoId != null) {
                nuevo.setTipoPagoDefecto(service.refTipoPago(nuevoTipoPagoDefectoId));
            } else {
                nuevo.setTipoPagoDefecto(null);
            }

            crear(nuevo);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Egreso guardado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override
    public void guardarEdicion() {
        try {
            if (seleccionado == null) return;

            if (editTipoEgresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un tipo de egreso.");
                markValidationFailed();
                return;
            }
            if (editRenglonEgresoId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un renglón de egreso.");
                markValidationFailed();
                return;
            }
            if (seleccionado.getDescripcion() == null || seleccionado.getDescripcion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite una descripción.");
                markValidationFailed();
                return;
            }

            seleccionado.setTipoEgreso(service.refTipoEgreso(editTipoEgresoId));
            seleccionado.setRenglonEgreso(service.refRenglonEgreso(editRenglonEgresoId));

            if (editTipoPagoDefectoId != null) {
                seleccionado.setTipoPagoDefecto(service.refTipoPago(editTipoPagoDefectoId));
            } else {
                seleccionado.setTipoPagoDefecto(null);
            }

            actualizar(seleccionado);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Egreso actualizado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override
    protected Long getId(Egreso t) { return t.getId(); }
}
