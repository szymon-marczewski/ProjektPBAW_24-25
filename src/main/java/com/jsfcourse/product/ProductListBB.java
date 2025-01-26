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

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;
import jakarta.annotation.PostConstruct;

@Named
@RequestScoped
public class ProductListBB {

    private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String type;
    private List<Product> list;
    private String sortField = "idProduct";
    private boolean ascending = true; 

    @Inject
//    ExternalContext extcontext;
    private ProductDAO productDAO;

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
}
