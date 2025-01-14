package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Order;
import com.jsf.entities.User;

@Stateless
public class OrderDAO {
	private final static String UNIT_NAME = "my_persistence_unit";
	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	public Integer id = 3;
	public void create(Order order) {
		em.persist(order);
	}

	public Order merge(Order order) {
		return em.merge(order);
	}

	public void remove(Order order) {
		em.remove(em.merge(order));
	}

	public Order find(Object id) {
		return em.find(Order.class, id);
	}
//
	public void create(User user) {
		em.persist(user);
//		id = user.getIdUser();
	}
//
	public List<Order> getFullList() {
		List<Order> list = null;

		Query query = em.createQuery("SELECT p FROM Order p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public List<Order> getList(Map<String, Object> searchParams) {
		List<Order> list = null;

		//test
//		String q = "select p from Order p, User u where u.idUser = p.idUser and p.idUser like 7 order by p.idOrder";
		String q = "select p from Order p, User u where u.idUser = p.idUser and u.active like 1 order by p.idOrder";

		Query query = em.createQuery(q);
		list = query.getResultList();
		return list;
	}

}