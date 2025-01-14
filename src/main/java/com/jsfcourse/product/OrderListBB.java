package com.jsfcourse.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.Flash;

import com.jsf.dao.OrderDAO;
import com.jsf.entities.Order;

@Named
@RequestScoped
public class OrderListBB {
		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	OrderDAO orderDAO;

	public List<Order> getFullList(){
		return orderDAO.getFullList();
	}

	public List<Order> getList(){
		List<Order> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		//searchParams.put("idUser", "7");
		//2. Get list
		list = orderDAO.getList(searchParams);
		
		return list;
	}
}