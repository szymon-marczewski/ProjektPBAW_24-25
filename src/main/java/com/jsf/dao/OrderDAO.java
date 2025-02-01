package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Order;
import com.jsf.entities.User;
import jakarta.persistence.TypedQuery;

@Stateless
public class OrderDAO {
	private final static String UNIT_NAME = "my_persistence_unit";
	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
//	public Integer id = 3;
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

                Integer userId = (Integer) searchParams.get("idUser");

                String q = "SELECT o FROM Orders o WHERE o.idUser.idUser = :idUser AND o.status != 2 ORDER BY o.idOrder";
                Query query = em.createQuery(q);
                query.setParameter("idUser", userId); 
                try {
                    list = query.getResultList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return list;
        }
        
        public List<Order> getOrdersByUserId(Integer userId) {
            TypedQuery<Order> query = em.createNamedQuery("Order.findByUserId", Order.class);
            query.setParameter("idUser", userId); 
            return query.getResultList();
        }
        
//        public List<Object[]> getOrdersByUserId(Integer userId) {
//            TypedQuery<Object[]> query = em.createNamedQuery("SELECT o.idOrder, o.date, o.status, o.description, p.manufacturer, p.model " +
//                    "FROM Order o " +
//                    "WHERE o.idUser.idUser = :idUser" +
//                    "JOIN o.transactionsCollection t " +
//                    "JOIN t.idProduct p", Object[].class);
//            query.setParameter("idUser", userId); 
//            return query.getResultList();
//        }

}