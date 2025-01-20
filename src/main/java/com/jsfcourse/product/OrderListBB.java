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

	public List<Order> getList(){
                List<Order> list = null;

                // Retrieve the userId from the activeUser in UserLoginBB
                Integer userId = userLoginBB.getActiveUser().getIdUser();  // Access the active user ID

                // Prepare search parameters
                Map<String,Object> searchParams = new HashMap<String, Object>();
                searchParams.put("idUser", userId);

                // Get the list of orders for the logged-in user
                list = orderDAO.getList(searchParams);

                return list;
        }
        
        public User getActiveUser() {
            return userLoginBB.getActiveUser();
        }
        
        public List<Order> getOrdersForActiveUser() {
            Integer userId = getActiveUser().getIdUser();
            return orderDAO.getOrdersByUserId(userId);
}
}