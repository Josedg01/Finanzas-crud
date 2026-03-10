package com.finanzas.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public abstract class GenericRepo<T> {

    @PersistenceContext(unitName = "FinanzasPU")
    protected EntityManager em;

    private final Class<T> type;

    protected GenericRepo(Class<T> type) {
        this.type = type;
    }

    public T find(Object id) {
        return em.find(type, id);
    }

    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type)
                .getResultList();
    }

    public void create(T entity) {
        em.persist(entity);
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public void delete(Object id) {
        T e = find(id);
        if (e != null) {
            em.remove(e);
        }
    }
}
