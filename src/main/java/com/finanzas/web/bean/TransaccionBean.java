package com.finanzas.web.bean;

import com.finanzas.entity.*;
import com.finanzas.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class TransaccionBean extends CrudBeanBase<Transaccion> {

    @Inject
    private TransaccionService service;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private TipoPagoService tipoPagoService;
    @Inject
    private EgresoService egresoService;
    @Inject
    private IngresoService ingresoService;

    private List<Usuario> usuarios;
    private List<TipoPago> tiposPago;
    private List<Egreso> egresos;
    private List<Ingreso> ingresos;
    private final List<String> tiposTransaccion = Arrays.asList("INGRESO", "EGRESO");

    // ====== IDs para selects (evita converters) ======
    private Long nuevoUsuarioId;
    private Long nuevoTipoPagoId;
    private Long nuevoEgresoId;
    private Long nuevoIngresoId;

    private Long editUsuarioId;
    private Long editTipoPagoId;
    private Long editEgresoId;
    private Long editIngresoId;

    @PostConstruct
    public void postConstruct() {
        recargarCombos();
        init();
    }

    private void recargarCombos() {
        usuarios = usuarioService.listar();
        tiposPago = tipoPagoService.listar();
        egresos = egresoService.listar();
        ingresos = ingresoService.listar();
    }

    // ===================== Getters de listas =====================
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<TipoPago> getTiposPago() { return tiposPago; }
    public List<Egreso> getEgresos() { return egresos; }
    public List<Ingreso> getIngresos() { return ingresos; }
    public List<String> getTiposTransaccion() { return tiposTransaccion; }

    // ===================== Getters/Setters IDs =====================
    public Long getNuevoUsuarioId() { return nuevoUsuarioId; }
    public void setNuevoUsuarioId(Long nuevoUsuarioId) { this.nuevoUsuarioId = nuevoUsuarioId; }

    public Long getNuevoTipoPagoId() { return nuevoTipoPagoId; }
    public void setNuevoTipoPagoId(Long nuevoTipoPagoId) { this.nuevoTipoPagoId = nuevoTipoPagoId; }

    public Long getNuevoEgresoId() { return nuevoEgresoId; }
    public void setNuevoEgresoId(Long nuevoEgresoId) { this.nuevoEgresoId = nuevoEgresoId; }

    public Long getNuevoIngresoId() { return nuevoIngresoId; }
    public void setNuevoIngresoId(Long nuevoIngresoId) { this.nuevoIngresoId = nuevoIngresoId; }

    public Long getEditUsuarioId() { return editUsuarioId; }
    public void setEditUsuarioId(Long editUsuarioId) { this.editUsuarioId = editUsuarioId; }

    public Long getEditTipoPagoId() { return editTipoPagoId; }
    public void setEditTipoPagoId(Long editTipoPagoId) { this.editTipoPagoId = editTipoPagoId; }

    public Long getEditEgresoId() { return editEgresoId; }
    public void setEditEgresoId(Long editEgresoId) { this.editEgresoId = editEgresoId; }

    public Long getEditIngresoId() { return editIngresoId; }
    public void setEditIngresoId(Long editIngresoId) { this.editIngresoId = editIngresoId; }

    // ===================== Helpers =====================
    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    private void markValidationFailed() {
        FacesContext.getCurrentInstance().validationFailed();
    }

    private String getRootMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null) root = root.getCause();
        return root.getMessage() != null ? root.getMessage() : root.toString();
    }

    @Override
    protected List<Transaccion> listar() { return service.listar(); }

    @Override
    protected void crear(Transaccion t) { service.crear(t); }

    @Override
    protected Transaccion actualizar(Transaccion t) { return service.actualizar(t); }

    @Override
    protected void eliminarPorId(Long id) { service.eliminar(id); }

    @Override
    protected Transaccion nuevoInstance() {
        Transaccion t = new Transaccion();
        t.setEstado(true);
        t.setFechaRegistro(new Date());
        t.setFechaTransaccion(new Date());
        t.setTipoTransaccion("EGRESO");
        t.setMonto(null);

        // Defaults (solo IDs, NO objetos)
        nuevoUsuarioId = (usuarios != null && !usuarios.isEmpty()) ? usuarios.get(0).getId() : null;
        nuevoTipoPagoId = (tiposPago != null && !tiposPago.isEmpty()) ? tiposPago.get(0).getId() : null;
        nuevoEgresoId = (egresos != null && !egresos.isEmpty()) ? egresos.get(0).getId() : null;
        nuevoIngresoId = null;

        return t;
    }

    @Override
    public void prepararNuevo() {
        super.prepararNuevo();
        if (nuevo != null && "EGRESO".equals(nuevo.getTipoTransaccion())) {
            nuevoIngresoId = null;
        }
    }

    public void prepararEdit() {
        if (seleccionado == null) return;

        editUsuarioId = (seleccionado.getUsuario() != null) ? seleccionado.getUsuario().getId() : null;
        editTipoPagoId = (seleccionado.getTipoPago() != null) ? seleccionado.getTipoPago().getId() : null;
        editEgresoId = (seleccionado.getEgreso() != null) ? seleccionado.getEgreso().getId() : null;
        editIngresoId = (seleccionado.getIngreso() != null) ? seleccionado.getIngreso().getId() : null;
    }

    public void onTipoTransaccionChange() {
        Transaccion t = (nuevo != null ? nuevo : seleccionado);
        if (t == null) return;

        if ("INGRESO".equals(t.getTipoTransaccion())) {
            if (t == nuevo) nuevoEgresoId = null;
            else editEgresoId = null;

        } else if ("EGRESO".equals(t.getTipoTransaccion())) {
            if (t == nuevo) nuevoIngresoId = null;
            else editIngresoId = null;
        }
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
            if (nuevo.getTipoTransaccion() == null || nuevo.getTipoTransaccion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione el tipo de transacción.");
                markValidationFailed();
                return;
            }
            if (nuevo.getFechaTransaccion() == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione la fecha de transacción.");
                markValidationFailed();
                return;
            }
            if (nuevo.getMonto() == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite el monto.");
                markValidationFailed();
                return;
            }

            // no permitir montos negativos
            if (nuevo.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "El monto no puede ser negativo.");
                markValidationFailed();
                return;
            }

            nuevo.setUsuario(service.refUsuario(nuevoUsuarioId));

            if (nuevoTipoPagoId != null) nuevo.setTipoPago(service.refTipoPago(nuevoTipoPagoId));
            else nuevo.setTipoPago(null);

            if ("EGRESO".equals(nuevo.getTipoTransaccion())) {
                nuevo.setIngreso(null);
                if (nuevoEgresoId == null) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un egreso.");
                    markValidationFailed();
                    return;
                }
                nuevo.setEgreso(service.refEgreso(nuevoEgresoId));

            } else if ("INGRESO".equals(nuevo.getTipoTransaccion())) {
                nuevo.setEgreso(null);
                if (nuevoIngresoId == null) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un ingreso.");
                    markValidationFailed();
                    return;
                }
                nuevo.setIngreso(service.refIngreso(nuevoIngresoId));
            }

            crear(nuevo);
            refrescar();

            recargarCombos();

            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Transacción guardada.");
        } catch (Exception ex) {
            // ✅ CAMBIO: mostrar alerta especial y bloquear guardado
            String msg = getRootMessage(ex);

            if (ex instanceof IllegalArgumentException || (msg != null && msg.toLowerCase().contains("límite"))) {
                addMsg(FacesMessage.SEVERITY_WARN, "Límite de egresos", msg);
            } else {
                addMsg(FacesMessage.SEVERITY_ERROR, "Error", msg);
            }

            // ✅ Bloquea el cierre del dialog y el guardado
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
            if (seleccionado.getTipoTransaccion() == null || seleccionado.getTipoTransaccion().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione el tipo de transacción.");
                markValidationFailed();
                return;
            }
            if (seleccionado.getFechaTransaccion() == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione la fecha de transacción.");
                markValidationFailed();
                return;
            }
            if (seleccionado.getMonto() == null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite el monto.");
                markValidationFailed();
                return;
            }

            // no permitir montos negativos
            if (seleccionado.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "El monto no puede ser negativo.");
                markValidationFailed();
                return;
            }

            seleccionado.setUsuario(service.refUsuario(editUsuarioId));

            if (editTipoPagoId != null) seleccionado.setTipoPago(service.refTipoPago(editTipoPagoId));
            else seleccionado.setTipoPago(null);

            if ("EGRESO".equals(seleccionado.getTipoTransaccion())) {
                seleccionado.setIngreso(null);
                if (editEgresoId == null) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un egreso.");
                    markValidationFailed();
                    return;
                }
                seleccionado.setEgreso(service.refEgreso(editEgresoId));

            } else if ("INGRESO".equals(seleccionado.getTipoTransaccion())) {
                seleccionado.setEgreso(null);
                if (editIngresoId == null) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Seleccione un ingreso.");
                    markValidationFailed();
                    return;
                }
                seleccionado.setIngreso(service.refIngreso(editIngresoId));
            }

            actualizar(seleccionado);
            refrescar();

            recargarCombos();

            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Transacción actualizada.");
        } catch (Exception ex) {
            // ✅ CAMBIO: mostrar alerta especial y bloquear guardado
            String msg = getRootMessage(ex);

            if (ex instanceof IllegalArgumentException || (msg != null && msg.toLowerCase().contains("límite"))) {
                addMsg(FacesMessage.SEVERITY_WARN, "Límite de egresos", msg);
            } else {
                addMsg(FacesMessage.SEVERITY_ERROR, "Error", msg);
            }

            markValidationFailed();
        }
    }

    @Override
    protected Long getId(Transaccion t) {
        return t.getId();
    }
}