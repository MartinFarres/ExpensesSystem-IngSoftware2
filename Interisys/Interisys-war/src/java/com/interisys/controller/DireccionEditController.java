
package com.interisys.controller;

import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.logic.LocalidadServiceBean;
import com.interisys.business.logic.DireccionServiceBean;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author spaul
 */

@ManagedBean
@ViewScoped
public class DireccionEditController {
    private Direccion direccion;
    private @EJB DireccionServiceBean direccionService;
    private @EJB LocalidadServiceBean localidadService; 
    // Variables Vista
    private Collection<SelectItem> localidades = new ArrayList(); 
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String idLocalidad; 
    private String calle;
    private String numeracion;
    private String barrio;
    private String pisoCasa;
    private String puertaManzana;
    private String ubicacionCoordenadaX;
    private String ubicacionCoordenadaY;
    private String observacion;
    

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            direccion = (Direccion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DIRECCION");
            campoDesactivado = false;

            if (casoDeUso.equals("ALTA")) {
                cargarComboLocalidad(); 
            } else if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                idLocalidad = direccion.getLocalidad().getId(); 
                calle = direccion.getCalle();
                numeracion = direccion.getNumeracion();
                barrio = direccion.getBarrio();
                pisoCasa = direccion.getPisoCasa();
                puertaManzana = direccion.getPuertaManzana();
                ubicacionCoordenadaX = direccion.getUbicacionCoordenadaX();
                ubicacionCoordenadaY = direccion.getUbicacionCoordenadaY();
                observacion = direccion.getObservacion();
                cargarComboLocalidad(); 
                
                if (casoDeUso.equals("CONSULTAR"))
                    campoDesactivado = true;
            }
            
            cargarComboLocalidad(); 
             
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public String getIdLocalidad() { 
        return idLocalidad;
    }

    public void setIdLocalidad(String idLocalidad) { 
        this.idLocalidad = idLocalidad; 
    }
    
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
    public Collection<SelectItem> getLocalidades() { 
        return localidades; 
    }

    public void setLocalidades(Collection<SelectItem> localidades) { 
        this.localidades = localidades; 
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public String getCalle() {
        return calle;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getUbicacionCoordenadaX() {
        return ubicacionCoordenadaX;
    }

    public void setUbicacionCoordenadaX(String ubicacionCoordenadaX) {
        this.ubicacionCoordenadaX = ubicacionCoordenadaX;
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

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
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
    
    
    
    public String aceptar() {
        try {
            switch (casoDeUso) {
                case ALTA:
                    direccionService.crearDireccion(idLocalidad, calle, numeracion, barrio, pisoCasa, puertaManzana, ubicacionCoordenadaX, ubicacionCoordenadaY, observacion ); 
                    Message.show("Direccion creada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    direccionService.modificarDireccion(
                            direccion.getId(),
                            idLocalidad, 
                            calle,
                            numeracion, barrio, pisoCasa,puertaManzana,ubicacionCoordenadaX, ubicacionCoordenadaY, observacion);
                    Message.show("Direccion modificada exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break; 
            }

            return "listDireccion";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void cargarComboLocalidad() { 
        try {  
            localidades = new ArrayList<SelectItem>(); 
            localidades.add(new SelectItem(null, "Seleccione...")); 
            for (Localidad localidad : localidadService.listarLocalidadActivo()) { 
                localidades.add(new SelectItem(localidad.getId(), localidad.getNombre())); 
            }    
        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String cancelar() {
        return "listDireccion";
    }
}

