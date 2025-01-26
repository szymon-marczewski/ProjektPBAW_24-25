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
//	public int nr = 0;
//	public int end_nr = 5; 
	// Dependency injection (no setter method is needed)
//        private List<Product> list;
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

	public List<Product> getFullList(String sortField, boolean ascending) {
            List<Product> list = null;

            String select = "SELECT p ";
            String from = "FROM Product p ";
            String orderBy = "ORDER BY p." + sortField;

            if (!ascending) {
                orderBy += " DESC";
            }

            Query query = em.createQuery(select + from + orderBy);

            try {
                list = query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return list;
    }


	public List<Product> getList(Map<String, Object> searchParams, String sortField, boolean ascending) {
		List<Product> list = null;

		String select = "select p ";
		String from = "from Product p ";
		String where = "";
//		String orderby = "";
                

		String type = (String) searchParams.get("type");
		if (type != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.type like :type ";
		}
                
                String orderBy = "order by p." + sortField;
                if (!ascending) {
                    orderBy += " desc";
                }
		
		Query query = em.createQuery(select + from + where + orderBy);

		if (type != null) {
			query.setParameter("type", type+ "%");
		}
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}