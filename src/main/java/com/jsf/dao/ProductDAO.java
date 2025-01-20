package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Product;

@Stateless
public class ProductDAO {
	private final static String UNIT_NAME = "my_persistence_unit";
	public int nr = 0;
	public int end_nr = 5; 
	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Product product) {
		em.persist(product);
	}

	public Product merge(Product product) {
		return em.merge(product);
	}

	public void remove(Product product) {
		em.remove(em.merge(product));
	}

	public Product find(Object id) {
		return em.find(Product.class, id);
	}

	public void less()
	{
		int nr = 0;
		getList(null);
	}
	
	public List<Product> more()
	{
		int nr = 10;
		int end_nr = 20;
		return getFullList(nr, end_nr);
	}

	public List<Product> getFullList(int nr, int end_nr) {
		List<Product> list = null;

		Query query = em.createQuery("SELECT p FROM Product p").setFirstResult(0).setMaxResults(5);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public List<Product> getList(Map<String, Object> searchParams) {
		List<Product> list = null;

		// 1. Build query string with parameters
		String select = "select p ";
		String from = "from Product p ";
		String where = "";
		String orderby = "order by p.type asc, p.manufacturer asc";

		String type = (String) searchParams.get("type");
		if (type != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.type like :type ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);

		if (type != null) {
			query.setParameter("type", type+"%");
		}
		//tu działa XD
		query.setFirstResult(0).setMaxResults(15);
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}