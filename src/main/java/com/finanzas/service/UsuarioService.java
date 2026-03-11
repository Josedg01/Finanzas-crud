package com.finanzas.service;

import com.finanzas.entity.Usuario;
import com.finanzas.repo.GenericRepo;
import com.finanzas.repo.UsuarioRepo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UsuarioService extends GenericService<Usuario> {

    @Inject
    private UsuarioRepo r;

    @Override
    protected GenericRepo<Usuario> repo() {
        return r;
    }

    public Usuario buscarPorLogin(String login) {
        // ✅ CAMBIO: trim para evitar " user " y problemas de espacios
        if (login == null) return null;
        return r.findByLogin(login.trim());
    }

    /** Crea un usuario asignando hash BCrypt al password. */
    public void crearConPassword(Usuario u, String passwordPlano) {
        if (u == null) throw new IllegalArgumentException("Usuario requerido");
        if (passwordPlano == null || passwordPlano.isBlank()) {
            throw new IllegalArgumentException("Password requerido");
        }

        // ✅ CAMBIO: normalizar campos (trim) antes de guardar
        if (u.getUsuarioLogin() != null) u.setUsuarioLogin(u.getUsuarioLogin().trim());
        if (u.getCedula() != null) u.setCedula(u.getCedula().trim());
        if (u.getRnc() != null) u.setRnc(u.getRnc().trim());

        u.setPasswordHash(BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12)));
        crear(u);
    }

    public boolean verificarPassword(Usuario u, String passwordPlano) {
        if (u == null) return false;
        if (passwordPlano == null) return false;

        String hash = u.getPasswordHash();
        return hash != null && BCrypt.checkpw(passwordPlano, hash);
    }
}