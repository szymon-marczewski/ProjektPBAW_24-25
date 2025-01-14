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

import com.jsf.dao.UserDAO;
import com.jsf.entities.Product;
import com.jsf.entities.User;

@Named
@ViewScoped
public class UserLoginBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PRODUCT_LIST = "productList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;

	@EJB
	UserDAO user_s;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public User getUser() {
		return user;
	}
	
	public void onLoad() throws IOException {
		
		loaded = (User) flash.get("user");

		if (loaded != null) {
			user = loaded;
			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			
		}

	}
	
	public String login()
	{
		
		int id = user.getIdUser();
		user.setActive(1);
		return PAGE_STAY_AT_THE_SAME;
	}
}