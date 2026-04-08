package com.finanzas.web.bean;

import com.finanzas.entity.Corte;
import com.finanzas.entity.Usuario;
import com.finanzas.service.CorteService;
import com.finanzas.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class CorteBean extends CrudBeanBase<Corte> {

    @Inject private CorteService service;
    @Inject private UsuarioService usuarioService;

    private List<Usuario> usuarios;

    // ====== IDs para selects (evita converter) ======
    private Long nuevoUsuarioId;
    private Long editUsuarioId;

    @PostConstruct
    public void postConstruct() {
        usuarios = usuarioService.listar();
        init();
    }

    public List<Usuario> getUsuarios() { return usuarios; }

    public Long getNuevoUsuarioId() { return nuevoUsuarioId; }
    public void setNuevoUsuarioId(Long nuevoUsuarioId) { this.nuevoUsuarioId = nuevoUsuarioId; }

    public Long getEditUsuarioId() { return editUsuarioId; }
    public void setEditUsuarioId(Long editUsuarioId) { this.editUsuarioId = editUsuarioId; }

    // ===================== helpers =====================
    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    private void markValidationFailed() {
        FacesContext.getCurrentInstance().validationFailed();
    }

    // ✅ NUEVO: no permitir montos negativos
    private boolean isNeg(BigDecimal v) {
        return v != null && v.compareTo(BigDecimal.ZERO) < 0;
    }

    // ===================== CRUD Base =====================
    @Override protected List<Corte> listar() { return service.listar(); }
    @Override protected void crear(Corte t) { service.crear(t); }
    @Override protected Corte actualizar(Corte t) { return service.actualizar(t); }
    @Override protected void eliminarPorId(Long id) { service.eliminar(id); }

    @Override
    protected Corte nuevoInstance() {
        Corte c = new Corte();
        c.setEstado(true);

        nuevoUsuarioId = (usuarios != null && !usuarios.isEmpty()) ? usuarios.get(0).getId() : null;

        Calendar cal = Calendar.getInstance();
        c.setAnio((short) cal.get(Calendar.YEAR));
        c.setMes((short) (cal.get(Calendar.MONTH) + 1));
        c.setFechaCorte(new Date());

        c.setBalanceInicial(BigDecimal.ZERO);
        c.setTotalIngresos(BigDecimal.ZERO);
        c.setTotalEgresos(BigDecimal.ZERO);
        c.setBalanceCorte(BigDecimal.ZERO);

        return c;
    }

    @Override
    public void prepararNuevo() {
        super.prepararNuevo();
        if (nuevoUsuarioId == null && usuarios != null && !usuarios.isEmpty()) {
            nuevoUsuarioId = usuarios.get(0).getId();
        }
    }

    public void prepararEdit() {
        if (seleccionado == null) return;
        editUsuarioId = (seleccionado.getUsuario() != null) ? seleccionado.getUsuario().getId() : null;
    }

    @Override
    public void guardarNuevo() {
        try {
            if (nuevo == null) return;

            if (nuevoUsuarioId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un usuario.");
                markValidationFailed();
                return;
            }

            nuevo.setUsuario(service.refUsuario(nuevoUsuarioId));

            // ✅ NUEVO: validar montos no negativos
            if (isNeg(nuevo.getBalanceInicial()) || isNeg(nuevo.getTotalIngresos())
                    || isNeg(nuevo.getTotalEgresos()) || isNeg(nuevo.getBalanceCorte())) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación",
                        "No se permiten montos negativos en los campos del corte.");
                markValidationFailed();
                return;
            }

            crear(nuevo);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Corte guardado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override
    public void guardarEdicion() {
        try {
            if (seleccionado == null) return;

            if (editUsuarioId == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un usuario.");
                markValidationFailed();
                return;
            }

            seleccionado.setUsuario(service.refUsuario(editUsuarioId));

            // ✅ NUEVO: validar montos no negativos
            if (isNeg(seleccionado.getBalanceInicial()) || isNeg(seleccionado.getTotalIngresos())
                    || isNeg(seleccionado.getTotalEgresos()) || isNeg(seleccionado.getBalanceCorte())) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación",
                        "No se permiten montos negativos en los campos del corte.");
                markValidationFailed();
                return;
            }

            actualizar(seleccionado);
            refrescar();
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Corte actualizado.");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            markValidationFailed();
        }
    }

    @Override protected Long getId(Corte t) { return t.getId(); }
}
