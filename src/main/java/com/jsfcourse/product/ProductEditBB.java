package com.jsfcourse.product;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;

@Named
@ViewScoped
public class ProductEditBB implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PAGE_PRODUCT_LIST = "productList?faces-redirect=true";
    private static final String PAGE_STAY_AT_THE_SAME = null;

    private Product product = new Product();

    @EJB
    ProductDAO productDAO;

    @Inject
    FacesContext context;

    @Inject
    Flash flash;

    public Product getProduct() {
        return product;
    }

    public void onLoad() throws IOException {
        product = (Product) flash.get("product");

        if (product == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny dostęp do edycji produktu", null));
            context.getExternalContext().redirect(PAGE_PRODUCT_LIST);
        }
    }

    public String saveData() {
        try {
            if (product.getIdProduct() == null) {
                productDAO.create(product);
            } else {
                productDAO.merge(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wystąpił błąd podczas zapisu produktu", null));
            return PAGE_PRODUCT_LIST;
        }

        return PAGE_PRODUCT_LIST;
    }
}
