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

@Named
@RequestScoped
public class ProductListBB {

    private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private String type;

    @Inject
    ExternalContext extcontext;

    @Inject
    Flash flash;

    @EJB
    ProductDAO productDAO;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Product> getFullList() {
        return productDAO.getFullList(0, 10);
    }

    public List<Product> getList() {
        List<Product> list = null;

        Map<String, Object> searchParams = new HashMap<String, Object>();

        if (type != null && type.length() > 0) {
            searchParams.put("type", "%" + type + "%");
        }

        list = productDAO.getList(searchParams);

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
