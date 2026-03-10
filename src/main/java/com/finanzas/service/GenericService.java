package com.finanzas.service;

import com.finanzas.repo.GenericRepo;

import java.util.List;

public abstract class GenericService<T> {

    protected abstract GenericRepo<T> repo();

    public List<T> listar() {
        return repo().findAll();
    }

    public T buscar(Object id) {
        return repo().find(id);
    }

    public void crear(T t) {
        repo().create(t);
    }

    public T actualizar(T t) {
        return repo().update(t);
    }

    public void eliminar(Object id) {
        repo().delete(id);
    }
}
