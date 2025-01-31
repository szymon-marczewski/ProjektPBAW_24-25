package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Product;
import jakarta.persistence.TypedQuery;

@Stateless
public class ProductDAO {
	private final static String UNIT_NAME = "my_persistence_unit";
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
        
        public List<Product> getListWithPagination(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filterParams) {
        StringBuilder queryStr = new StringBuilder("SELECT p FROM Product p WHERE 1=1 ");

        filterParams.forEach((key, value) -> {
            queryStr.append(" AND p.").append(key).append(" LIKE :").append(key);
        });

        if (sortField != null && !sortField.isEmpty()) {
            queryStr.append(" ORDER BY p.").append(sortField);
            queryStr.append(ascending ? " ASC" : " DESC");
        }

        TypedQuery<Product> query = em.createQuery(queryStr.toString(), Product.class);

        filterParams.forEach((key, value) -> {
            query.setParameter(key, "%" + value + "%"); 
        });

        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public int count(Map<String, Object> filterParams) {
        StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1 ");

        filterParams.forEach((key, value) -> {
            countQueryStr.append(" AND p.").append(key).append(" LIKE :").append(key);
        });

        TypedQuery<Long> countQuery = em.createQuery(countQueryStr.toString(), Long.class);

        filterParams.forEach((key, value) -> {
            countQuery.setParameter(key, "%" + value + "%");
        });

        return countQuery.getSingleResult().intValue();
    }

//    public List<Product> getFullList(String sortField, boolean ascending) {
//        List<Product> list = null;
//
//        String select = "SELECT p ";
//        String from = "FROM Product p ";
//        String orderBy = "ORDER BY p." + sortField;
//
//        if (!ascending) {
//            orderBy += " DESC";
//        }
//
//        Query query = em.createQuery(select + from + orderBy); 
//
//        try {
//            list = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;
//        }
//
//
//    public List<Product> getList(Map<String, Object> searchParams, String sortField, boolean ascending) {
//        List<Product> list = null;
//
//        String select = "SELECT p ";
//        String from = "FROM Product p ";
//        String where = "";
//
//        String type = (String) searchParams.get("type");
//        if (type != null) {
//            if (where.isEmpty()) {
//                where = "WHERE ";
//            } else {
//                where += "AND ";
//            }
//            where += "p.type LIKE :type ";
//        }
//
//        String orderBy = "ORDER BY p." + sortField;
//        if (!ascending) {
//            orderBy += " DESC";
//        }
//
//        Query query = em.createQuery(select + from + where + orderBy);
//
//        if (type != null) {
//            query.setParameter("type", "%" + type + "%");
//        }
//
//        try {
//            list = query.getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }

}