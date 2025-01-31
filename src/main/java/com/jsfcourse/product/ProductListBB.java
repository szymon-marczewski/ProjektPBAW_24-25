package com.jsfcourse.product;

import com.jsf.dao.OrderDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.Flash;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Order;
import com.jsf.entities.Product;
import com.jsf.entities.User;
//import com.jsf.product.UserLoginBB;
import jakarta.annotation.PostConstruct;
//import jakarta.faces.annotation.ManagedProperty;
import java.time.LocalDate;
import org.primefaces.event.data.SortEvent;

@Named
@RequestScoped
public class ProductListBB {

    private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String type;
    private List<Product> list;
    private String sortField = "idProduct";
    private boolean ascending = true; 

    @EJB
    private OrderDAO orderDAO;
    
    @Inject
//    ExternalContext extcontext;
    private ProductDAO productDAO;
    
    @Inject
    private UserLoginBB userLoginBB;

    @Inject
    Flash flash;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    @PostConstruct
    public void init() {
        list = productDAO.getFullList(sortField, ascending);
    }

    public List<Product> getList() {
        if (type != null && !type.isEmpty()) {
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("type", "%" + type + "%");
            return productDAO.getList(searchParams, sortField, ascending);
        }
        return list;
    }
    
    public String newProduct() {
        Product product = new Product();

        flash.put("product", product);

        return PAGE_PRODUCT_EDIT;
    }

    public String editProduct(Product product) {

        flash.put("product", product);

        return PAGE_PRODUCT_EDIT;
    }

    public String deleteProduct(Product product) {
        productDAO.remove(product);
        return PAGE_STAY_AT_THE_SAME;
    }
    
    public void buyProduct(Product p) {
        if (userLoginBB.isLoggedIn()) {
            User activeUser = userLoginBB.getActiveUser();
            
            LocalDate localDate = LocalDate.now();
            java.sql.Date date = java.sql.Date.valueOf(localDate);
            
            Order order = new Order();
            order.setIdUser(activeUser); 
            order.setDate(date);
            order.setStatus(0);
            order.setDescription("Waiting");

            orderDAO.create(order);
        }
    }

}
