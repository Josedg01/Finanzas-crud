package com.finanzas.web.bean;

import com.finanzas.entity.Usuario;
import com.finanzas.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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

    public List<String> getTiposPersona() {return tiposPersona;}

    public String getNuevoPass1() { return nuevoPass1; }
    public void setNuevoPass1(String nuevoPass1) { this.nuevoPass1 = nuevoPass1; }
    public String getNuevoPass2() { return nuevoPass2; }
    public void setNuevoPass2(String nuevoPass2) { this.nuevoPass2 = nuevoPass2; }
    public String getEditPass() { return editPass; }
    public void setEditPass(String editPass) { this.editPass = editPass; }

    @Override
    protected List<Usuario> listar() {return service.listar();}
 
    @Override
    protected void crear(Usuario t) {
        // Para login, usuarioLogin y password son obligatorios
        if (t.getUsuarioLogin() == null || t.getUsuarioLogin().isBlank()) {
            throw new IllegalArgumentException("Digite el Usuario/Login");
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
        // Si se digitó una nueva contraseña, actualizar hash
        if (editPass != null && !editPass.isBlank()) {
            t.setPasswordHash(org.mindrot.jbcrypt.BCrypt.hashpw(editPass, org.mindrot.jbcrypt.BCrypt.gensalt(12)));
            editPass = null;
        }
        return service.actualizar(t);
    }

    @Override
    protected void eliminarPorId(Long id) { service.eliminar(id);}
    
    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    @Override
    protected Usuario nuevoInstance() {
        Usuario u = new Usuario();
        u.setEstado(true);
        u.setFechaCorteDia(1);
        u.setTipoPersona("FISICA");
        u.setUsuarioLogin("");
        return u;
    }

    @Override
    protected Long getId(Usuario t) {
        return t.getId();
    }
}
