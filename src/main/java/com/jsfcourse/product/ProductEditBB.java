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
	private Product loaded = null;

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
		
		loaded = (Product) flash.get("product");

		if (loaded != null) {
			product = loaded;
			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			
		}

	}

	public String saveData() {
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (product.getIdProduct() == null) {
				// new record
				productDAO.create(product);
			} else {
				// existing record
				productDAO.merge(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_PRODUCT_LIST;
	}
}