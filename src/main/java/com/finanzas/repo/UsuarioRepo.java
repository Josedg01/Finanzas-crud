package com.finanzas.repo;

import com.finanzas.entity.Usuario;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class UsuarioRepo extends GenericRepo<Usuario> {
    public UsuarioRepo() {
        super(Usuario.class);
    }

    public Usuario findByLogin(String login) {
        if (login == null) return null;
        List<Usuario> res = em.createQuery(
                        "SELECT u FROM Usuario u WHERE LOWER(u.usuarioLogin) = LOWER(:login)",
                        Usuario.class)
                .setParameter("login", login.trim())
                .setMaxResults(1)
                .getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
