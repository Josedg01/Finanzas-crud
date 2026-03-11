package com.finanzas.web.bean;

import com.finanzas.entity.Usuario;
import com.finanzas.service.UsuarioService;
import com.finanzas.web.util.CedulaUtil;
import com.finanzas.web.util.RncValidatorUtil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    // Login
    private String login;
    private String password;

    // Registro
    private Usuario nuevo = new Usuario();
    private String password1;
    private String password2;

    // Sesión
    private Usuario usuarioActual;

    public boolean isLogueado() { return usuarioActual != null; }
    public boolean isLoggedIn() { return usuarioActual != null; }

    public String getNombreUsuario() {
        if (usuarioActual == null) return "Invitado";
        if (usuarioActual.getNombre() != null && !usuarioActual.getNombre().isBlank()) {
            return usuarioActual.getNombre();
        }
        if (usuarioActual.getUsuarioLogin() != null && !usuarioActual.getUsuarioLogin().isBlank()) {
            return usuarioActual.getUsuarioLogin();
        }
        return "Usuario";
    }

    public void logout() { doLogout(); }

    public Usuario getUsuarioActual() { return usuarioActual; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Usuario getNuevo() { return nuevo; }
    public void setNuevo(Usuario nuevo) { this.nuevo = nuevo; }

    public String getPassword1() { return password1; }
    public void setPassword1(String password1) { this.password1 = password1; }

    public String getPassword2() { return password2; }
    public void setPassword2(String password2) { this.password2 = password2; }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public void prepararRegistro() {
        nuevo = new Usuario();
        nuevo.setEstado(true);
        nuevo.setFechaCorteDia(1);
        nuevo.setTipoPersona("FISICA");

        nuevo.setCedula(null);
        nuevo.setRnc(null);

        password1 = null;
        password2 = null;
    }

    public void doLogin() {
        try {
            if (login == null || login.isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite el usuario.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }
            if (password == null || password.isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite la contraseña.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            Usuario u = usuarioService.buscarPorLogin(login);
            if (u == null || !usuarioService.verificarPassword(u, password)) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Login", "Credenciales incorrectas.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }
            if (Boolean.FALSE.equals(u.getEstado())) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Login", "Usuario inactivo.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            usuarioActual = u;
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("FIN_AUTH", u.getId());

            password = null;

            redirect("/pages/transaccion.xhtml");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public void doLogout() {
        try {
            usuarioActual = null;
            login = null;
            password = null;

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().remove("FIN_AUTH");

            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            redirect("/login.xhtml");
        } catch (Exception ignored) {
        }
    }

    public void doRegister() {
        try {
            if (nuevo.getNombre() == null || nuevo.getNombre().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite el nombre.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            if (nuevo.getUsuarioLogin() == null || nuevo.getUsuarioLogin().isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite el usuario/login.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            if (password1 == null || password1.isBlank()) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Digite la contraseña.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }
            if (!password1.equals(password2)) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Las contraseñas no coinciden.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            // Validación condicional Cedula/RNC según TipoPersona
            String tipo = nuevo.getTipoPersona();
            if ("FISICA".equals(tipo)) {
                if (isBlank(nuevo.getCedula())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "La cédula es obligatoria para persona física.");
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
                nuevo.setCedula(nuevo.getCedula().trim());
                if (!CedulaUtil.validarCedula(nuevo.getCedula())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "La cédula ingresada no es válida.");
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
                // limpiar RNC
                nuevo.setRnc(null);

            } else if ("JURIDICA".equals(tipo)) {
                if (isBlank(nuevo.getRnc())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "El RNC es obligatorio para persona jurídica.");
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
                nuevo.setRnc(nuevo.getRnc().trim());
                if (!RncValidatorUtil.validarRNC(nuevo.getRnc())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "El RNC ingresado no es válido.");
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
                // limpiar Cédula
                nuevo.setCedula(null);

            } else {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Tipo de persona inválido.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            if (usuarioService.buscarPorLogin(nuevo.getUsuarioLogin()) != null) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Validación", "Ese usuario/login ya existe.");
                FacesContext.getCurrentInstance().validationFailed();
                return;
            }

            usuarioService.crearConPassword(nuevo, password1);
            addMsg(FacesMessage.SEVERITY_INFO, "OK", "Usuario registrado. Inicie sesión.");

            prepararRegistro();
            redirect("/login.xhtml");
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    private void redirect(String path) throws IOException {
        String ctx = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(ctx + path);
    }
}