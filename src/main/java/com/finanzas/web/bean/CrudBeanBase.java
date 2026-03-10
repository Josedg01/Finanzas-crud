package com.finanzas.web.bean;

import java.io.Serializable;
import java.util.List;

public abstract class CrudBeanBase<T> implements Serializable {

    protected List<T> lista;
    protected T seleccionado;
    protected T nuevo;

    protected abstract List<T> listar();
    protected abstract void crear(T t);
    protected abstract T actualizar(T t);
    protected abstract void eliminarPorId(Long id);
    protected abstract T nuevoInstance();
    protected abstract Long getId(T t);

    public void init() {
        refrescar();
        nuevo = nuevoInstance();
    }

    public void refrescar() {
        lista = listar();
    }

    public void prepararNuevo() {
        nuevo = nuevoInstance();
    }

    public void guardarNuevo() {
        crear(nuevo);
        refrescar();
    }

    public void guardarEdicion() {
        if (seleccionado != null) {
            actualizar(seleccionado);
            refrescar();
        }
    }

    public void eliminar() {
        if (seleccionado != null) {
            Long id = getId(seleccionado);
            if (id != null) {
                eliminarPorId(id);
            }
            seleccionado = null;
            refrescar();
        }
    }

    public List<T> getLista() { return lista; }
    public T getSeleccionado() { return seleccionado; }
    public void setSeleccionado(T seleccionado) { this.seleccionado = seleccionado; }
    public T getNuevo() { return nuevo; }
}
