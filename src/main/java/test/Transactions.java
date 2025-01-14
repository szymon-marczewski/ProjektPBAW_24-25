/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author szymo
 */
@Entity
@Table(name = "transactions")
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findByIdTransaction", query = "SELECT t FROM Transactions t WHERE t.idTransaction = :idTransaction"),
    @NamedQuery(name = "Transactions.findByAmount", query = "SELECT t FROM Transactions t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transactions.findByTotalprice", query = "SELECT t FROM Transactions t WHERE t.totalprice = :totalprice")})
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTransaction")
    private Integer idTransaction;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Amount")
    private int amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Total_price")
    private int totalprice;
    @JoinColumn(name = "idOrder", referencedColumnName = "idOrder")
    @ManyToOne(optional = false)
    private Orders idOrder;
    @JoinColumn(name = "idProduct", referencedColumnName = "idProduct")
    @ManyToOne(optional = false)
    private Products idProduct;

    public Transactions() {
    }

    public Transactions(Integer idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Transactions(Integer idTransaction, int amount, int totalprice) {
        this.idTransaction = idTransaction;
        this.amount = amount;
        this.totalprice = totalprice;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Integer idTransaction) {
        this.idTransaction = idTransaction;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public Orders getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Orders idOrder) {
        this.idOrder = idOrder;
    }

    public Products getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Products idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaction != null ? idTransaction.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.idTransaction == null && other.idTransaction != null) || (this.idTransaction != null && !this.idTransaction.equals(other.idTransaction))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.Transactions[ idTransaction=" + idTransaction + " ]";
    }
    
}
