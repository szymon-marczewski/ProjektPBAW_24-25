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
import com.jsf.entities.User;
import java.util.Collections;

@Named
@RequestScoped
public class OrderListBB {
    
        @Inject
        private UserLoginBB userLoginBB; 
		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	OrderDAO orderDAO;

	public List<Order> getFullList(){
		return orderDAO.getFullList();
	}

	public List<Object[]> getList() {
            User activeUser = userLoginBB.getActiveUser();
            if (activeUser != null) {
                Map<String, Object> searchParams = new HashMap<>();
                searchParams.put("idUser", activeUser.getIdUser());
                return orderDAO.getList(searchParams);
            }
            return Collections.emptyList();
        }
        
        public User getActiveUser() {
            return userLoginBB.getActiveUser();
        }
        
        public List<Order> getOrdersForActiveUser() {
            Integer userId = getActiveUser().getIdUser();
            return orderDAO.getOrdersByUserId(userId);
        }
        
        public void cancelOrder(int orderId) {
            Order order = orderDAO.find(orderId);
            if (order != null) {
                order.setStatus(2);
                order.setDescription("Cancelled"); 
                orderDAO.merge(order);
    }
}
}