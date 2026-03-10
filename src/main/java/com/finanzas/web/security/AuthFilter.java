package com.finanzas.web.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Protege /pages/*: si no hay sesión autenticada, redirige a /login.xhtml
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/pages/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // JSF resources always allowed
        String uri = req.getRequestURI();
        if (uri.contains("javax.faces.resource")) {
            chain.doFilter(request, response);
            return;
        }

        // Verifica sesión (AuthBean está en SessionScoped; su presencia se refleja con sesión válida)
        // Aquí usamos un atributo simple para no acoplar el Filter a CDI.
        Object logged = req.getSession(false) != null ? req.getSession(false).getAttribute("FIN_AUTH") : null;
        if (logged == null) {
            String ctx = req.getContextPath();
            res.sendRedirect(ctx + "/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
