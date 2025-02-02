package com.jsf.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.jsf.entities.Transaction;

@Stateless
public class TransactionDAO {
    private final static String UNIT_NAME = "my_persistence_unit";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;

    public void create(Transaction transaction) {
        em.persist(transaction);
    }

    public Transaction merge(Transaction transaction) {
        return em.merge(transaction);
    }

    public void remove(Transaction transaction) {
        em.remove(em.merge(transaction));
    }

    public Transaction find(Object id) {
        return em.find(Transaction.class, id);
    }
}