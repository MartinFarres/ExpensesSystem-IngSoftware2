/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author franc
 */
@Entity
public class DetalleRecibo implements Serializable {
    @Id
    private String id;
    
    private double subtotal;
    
    
    @ManyToOne
    private ExpensaInmueble expensaInmueble;
    
    @ManyToOne
    private Recibo recibo;
    
    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public ExpensaInmueble getExpensaInmueble() {
        return expensaInmueble;
    }

    public void setExpensaInmueble(ExpensaInmueble expensaInmueble) {
        this.expensaInmueble = expensaInmueble;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleRecibo)) {
            return false;
        }
        DetalleRecibo other = (DetalleRecibo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.interisys.business.domain.entity.DetalleRecibo[ id=" + id + " ]";
    }
    
}
