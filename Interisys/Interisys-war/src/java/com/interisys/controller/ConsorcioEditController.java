/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.logic.DireccionServiceBean;
import com.interisys.business.logic.ConsorcioServiceBean;
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
public class ConsorcioEditController {
    private Consorcio consorcio;
    private @EJB ConsorcioServiceBean consorcioService;
    private @EJB DireccionServiceBean direccionService;
    // Variables Vista
    private Collection<SelectItem> direcciones = new ArrayList();
    // El caso de uso determina que función hace esta clase.
    // Esto depende de la opción que haya seleccionado el usuario
    // en la lista de consorcios. Esta variable obtiene su valor desde
    // el controlador de sesión.
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String idDireccion;
    private String nombre;

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    @PostConstruct
    public void init() {
        try {
            // Se obtiene el caso de uso  
            casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            consorcio = (Consorcio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONSORCIO");
            campoDesactivado = false;

            if (casoDeUso.equals("ALTA")) {
                cargarComboDireccion();
            } else if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                idDireccion = consorcio.getDireccion().getId();
                nombre = consorcio.getNombre();
                cargarComboDireccion();
                
                if (casoDeUso.equals("CONSULTAR"))
                    campoDesactivado = true;
            }
            
            cargarComboDireccion();
             
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
            
    public Consorcio getConsorcio() {
        return consorcio;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setConsorcio(Consorcio consorcio) {
        this.consorcio = consorcio;
    }
    
    public Collection<SelectItem> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<SelectItem> direcciones) {
        this.direcciones = direcciones;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }
    
    public String aceptar() {
        try {
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso) {
                case ALTA:
                    consorcioService.crearConsorcio(idDireccion, nombre);
                    Message.show("Consorcio creado exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    consorcioService.modificarConsorcio(
                            consorcio.getId(),
                            nombre,
                            idDireccion);
                    Message.show("Consorcio modificado exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break;
            }

            return "listConsorcio";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void cargarComboDireccion() {
        try {  
        direcciones = new ArrayList<SelectItem>();
        direcciones.add(new SelectItem(null, "Seleccione..."));
        for (Direccion direccion : direccionService.listarDireccionActiva()) {
            String descripcion = direccion.getCalle() + ", " + direccion.getNumeracion() + ", " + direccion.getBarrio() + ", " + direccion.getLocalidad().getNombre();
            direcciones.add(new SelectItem(direccion.getId(), descripcion));}   
        }catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    
    public String cancelar() {
        return "listConsorcio";
    }
}
