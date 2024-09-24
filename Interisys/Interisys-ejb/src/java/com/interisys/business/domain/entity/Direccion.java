package com.interisys.business.domain.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;


/**
 *
 * @author spaul
 */

@Entity
public class Direccion implements Serializable {
  
    @Id
    private String id;
    private String calle;
    private String numeracion;
    private String barrio;
    private String pisoCasa;
    private String puertaManzana;
    private String ubicacionCoordenadaX;
    private String ubicacionCoordenadaY;
    @Column(length = 500)
    private String observacion;
    @ManyToOne 
    private Localidad localidad;
    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.consorcio.dominio.domicilio.Direccion[ id=" + id + " ]";
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getPisoCasa() {
        return pisoCasa;
    }

    public void setPisoCasa(String pisoCasa) {
        this.pisoCasa = pisoCasa;
    }

    public String getPuertaManzana() {
        return puertaManzana;
    }

    public void setPuertaManzana(String puertaManzana) {
        this.puertaManzana = puertaManzana;
    }

    public String getUbicacionCoordenadaY() {
        return ubicacionCoordenadaY;
    }

    public void setUbicacionCoordenadaY(String ubicacionCoordenadaY) {
        this.ubicacionCoordenadaY = ubicacionCoordenadaY;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUbicacionCoordenadaX() {
        return ubicacionCoordenadaX;
    }

    public void setUbicacionCoordenadaX(String ubicacionCoordenadaX) {
        this.ubicacionCoordenadaX = ubicacionCoordenadaX;
    } 
}
