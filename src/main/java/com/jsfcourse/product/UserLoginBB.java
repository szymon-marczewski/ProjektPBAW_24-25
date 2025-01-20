package com.jsfcourse.product;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.UserDAO;
import com.jsf.entities.Product;
import com.jsf.entities.User;

@Named
@SessionScoped
public class UserLoginBB implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PAGE_PRODUCT_LIST = "productList?faces-redirect=true";
//    private static final String PAGE_STAY_AT_THE_SAME = null;

    private User user = new User();
    private User activeUser = null;

    @EJB
    UserDAO userDAO;

    public User getUser() {
        return user;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public boolean isLoggedIn() {
        return activeUser != null;
    }

    public String login() {
        User foundUser = userDAO.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            foundUser.setActive(1);
            userDAO.merge(foundUser);

            activeUser = foundUser;
            return PAGE_PRODUCT_LIST;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne dane logowania", null));
            return null;
        }
    }

    public String logout() {
        if (activeUser != null) {
            activeUser.setActive(0);
            userDAO.merge(activeUser);
            activeUser = null; 
        }
        user = new User();
        return PAGE_PRODUCT_LIST;
    }
    
    public boolean hasRole(String roleName) {
        return activeUser != null && activeUser.getRolename().getRolename().equals(roleName);
    }
}
