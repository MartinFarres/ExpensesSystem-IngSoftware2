/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.domain.entity;

import com.interisys.business.domain.enumeration.EstadoExpensaInmueble;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author franc
 */
@Entity
public class ExpensaInmueble implements Serializable {
    @Id
    private String id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    
    private EstadoExpensaInmueble estado;
    
    @ManyToOne
    private Inmueble inmueble;
    
    @ManyToOne
    private Expensa expensa;
    
    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoExpensaInmueble getEstado() {
        return estado;
    }

    public void setEstado(EstadoExpensaInmueble estado) {
        this.estado = estado;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Expensa getExpensa() {
        return expensa;
    }

    public void setExpensa(Expensa expensa) {
        this.expensa = expensa;
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
        if (!(object instanceof ExpensaInmueble)) {
            return false;
        }
        ExpensaInmueble other = (ExpensaInmueble) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.interisys.business.domain.entity.ExpensaInmueble[ id=" + id + " ]";
    }
    
}
