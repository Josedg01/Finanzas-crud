package com.finanzas.web.bean;

import com.finanzas.entity.Usuario;
import com.finanzas.service.UsuarioService;
import com.finanzas.web.util.CedulaUtil;
import com.finanzas.web.util.RncValidatorUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UsuarioBean extends CrudBeanBase<Usuario> {

    @Inject
    private UsuarioService service;

    private final List<String> tiposPersona = Arrays.asList("FISICA", "JURIDICA");

    // Password handling (solo para crear / opcional en editar)
    private String nuevoPass1;
    private String nuevoPass2;
    private String editPass;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    public List<String> getTiposPersona() { return tiposPersona; }

    public String getNuevoPass1() { return nuevoPass1; }
    public void setNuevoPass1(String nuevoPass1) { this.nuevoPass1 = nuevoPass1; }
    public String getNuevoPass2() { return nuevoPass2; }
    public void setNuevoPass2(String nuevoPass2) { this.nuevoPass2 = nuevoPass2; }
    public String getEditPass() { return editPass; }
    public void setEditPass(String editPass) { this.editPass = editPass; }

    @Override
    protected List<Usuario> listar() { return service.listar(); }

    
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    
    private void validarCedulaRncPorTipo(Usuario t) {
        String tipo = t.getTipoPersona();

        
        if (t.getCedula() != null) t.setCedula(t.getCedula().trim());
        if (t.getRnc() != null) t.setRnc(t.getRnc().trim());

        if ("FISICA".equals(tipo)) {
            if (isBlank(t.getCedula())) {
                throw new IllegalArgumentException("Digite la cédula (obligatoria para persona física).");
            }
            if (!CedulaUtil.validarCedula(t.getCedula())) {
                throw new IllegalArgumentException("La cédula ingresada no es válida.");
            }
            // limpiar RNC porque no aplica
            t.setRnc(null);
        }
        else if ("JURIDICA".equals(tipo)) {
            if (isBlank(t.getRnc())) {
                throw new IllegalArgumentException("Digite el RNC (obligatorio para persona jurídica).");
            }

            
            if (!RncValidatorUtil.validarRNC(t.getRnc())) {
                throw new IllegalArgumentException("El RNC ingresado no es válido.");
            }

            // limpiar cédula porque no aplica
            t.setCedula(null);
        }
        else {
            throw new IllegalArgumentException("TipoPersona inválido. Use FISICA o JURIDICA.");
        }
    }

    @Override
    protected void crear(Usuario t) {

        if (t.getUsuarioLogin() == null || t.getUsuarioLogin().isBlank()) {
            throw new IllegalArgumentException("Digite el Usuario/Login");
        }

        // Cedula/RNC depende del tipo
        validarCedulaRncPorTipo(t);

        // ✅ NUEVO: no permitir limite egresos negativo
        if (t.getLimiteEgresos() != null && t.getLimiteEgresos().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El límite de egresos no puede ser negativo.");
        }

        if (nuevoPass1 == null || nuevoPass1.isBlank()) {
            throw new IllegalArgumentException("Digite una contraseña");
        }
        if (!nuevoPass1.equals(nuevoPass2)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        service.crearConPassword(t, nuevoPass1);
        nuevoPass1 = null;
        nuevoPass2 = null;
    }

    @Override
    protected Usuario actualizar(Usuario t) {

        // Cedula/RNC depende del tipo
        validarCedulaRncPorTipo(t);

        // ✅ NUEVO: no permitir limite egresos negativo
        if (t.getLimiteEgresos() != null && t.getLimiteEgresos().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El límite de egresos no puede ser negativo.");
        }

        // Si se digitó una nueva contraseña, actualizar hash
        if (editPass != null && !editPass.isBlank()) {
            t.setPasswordHash(org.mindrot.jbcrypt.BCrypt.hashpw(editPass, org.mindrot.jbcrypt.BCrypt.gensalt(12)));
            editPass = null;
        }
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) { service.eliminar(id); }

    @Override
    protected Usuario nuevoInstance() {
        Usuario u = new Usuario();
        u.setEstado(true);
        u.setFechaCorteDia(1);
        u.setTipoPersona("FISICA");
        u.setUsuarioLogin("");

        // inicializar campos condicionales limpios
        u.setCedula(null);
        u.setRnc(null);

        return u;
    }

    @Override
    protected Long getId(Usuario t) {
        return t.getId();
    }
}