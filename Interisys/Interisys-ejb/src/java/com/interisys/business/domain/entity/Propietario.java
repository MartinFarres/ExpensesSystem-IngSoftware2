/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author aidal
 */
@Entity
public class Propietario extends Persona implements Serializable {
    
    private boolean habitaConsorcio;
    @ManyToOne 
    private Direccion direccion;

    public boolean isHabitaConsorcio() {
        return habitaConsorcio;
    }

    public void setHabitaConsorcio(boolean habitaConsorcio) {
        this.habitaConsorcio = habitaConsorcio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    

}
