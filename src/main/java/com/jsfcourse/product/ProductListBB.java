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
import com.jsf.dao.TransactionDAO;
import com.jsf.entities.Order;
import com.jsf.entities.Product;
import com.jsf.entities.Transaction;
import com.jsf.entities.User;
//import com.jsf.product.UserLoginBB;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.annotation.ManagedProperty;
import java.time.LocalDate;
import java.util.Collections;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

@Named
@RequestScoped
public class ProductListBB {

    private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String type;
    private List<Product> list;
    private String sortField = "idProduct";
    private String sortOrder;
    private boolean ascending = true; 
    private Map<Integer, Integer> quantities = new HashMap<>();
    private int selectedQuantity;
    
    private LazyDataModel<Product> lazyModel;

    @EJB
    private OrderDAO orderDAO;
    
    @Inject
//    ExternalContext extcontext;
    private ProductDAO productDAO;
    
    @Inject
    private TransactionDAO transactionDAO;
    
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

    public Map<Integer, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Integer, Integer> quantities) {
        this.quantities = quantities;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<Product>() {
            @Override
            public List<Product> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

                String sortField = null;
                boolean ascending = true;
                if (sortBy != null && !sortBy.isEmpty()) {
                    SortMeta meta = sortBy.values().iterator().next();
                    sortField = meta.getField();
                    ascending = meta.getOrder() == SortOrder.ASCENDING;
                }

                Map<String, Object> filterParams = new HashMap<>();
                if (filterBy != null && !filterBy.isEmpty()) {
                    filterBy.forEach((key, value) -> filterParams.put(key, value.getFilterValue()));
                }
                
                if (type != null && !type.isEmpty()) {
                    filterParams.put("type", type);
                }

                List<Product> resultList = productDAO.getListWithPagination(first, pageSize, sortField, ascending, filterParams);

                lazyModel.setRowCount(productDAO.count(filterParams));

                return resultList;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                Map<String, Object> filterParams = new HashMap<>();
                if (filterBy != null && !filterBy.isEmpty()) {
                    filterBy.forEach((key, value) -> filterParams.put(key, value.getFilterValue()));
                }
                
                if (type != null && !type.isEmpty()) {
                    filterParams.put("type", type);
                }
                
                return productDAO.count(filterParams);
            }
        };
    }

    public LazyDataModel<Product> getLazyModel() {
        return lazyModel;
    }
    
    public void search() {
        lazyModel.setRowCount(productDAO.count(Collections.singletonMap("type", type)));
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
            
            if (selectedQuantity < 1) {
                selectedQuantity = 1; 
            }
            
            User activeUser = userLoginBB.getActiveUser();
            
            LocalDate localDate = LocalDate.now();
            java.sql.Date date = java.sql.Date.valueOf(localDate);
            
            Order order = new Order();
            order.setIdUser(activeUser); 
            order.setDate(date);
            order.setStatus(0);
            order.setDescription("Waiting");

            orderDAO.create(order);
            
            Transaction transaction = new Transaction();
            transaction.setIdOrder(order); 
            transaction.setIdProduct(p);  
            transaction.setAmount(selectedQuantity);
            transaction.setTotalprice(p.getPrice() * selectedQuantity); 

            transactionDAO.create(transaction);
            quantities.put(p.getIdProduct(), 1);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order placed successfully!"));
        }
    }

}
