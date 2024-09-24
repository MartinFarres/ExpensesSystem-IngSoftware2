/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.nacionalidad;

import com.interisys.business.domain.entity.Nacionalidad;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.NacionalidadServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class NacionalidadEditController {

    private Nacionalidad nacionalidad;
    private @EJB NacionalidadServiceBean nacionalidadService;
    private boolean campoDesactivado;
    
    // El caso de uso determina que función hace esta clase.
    // Esto depende de la opción que haya seleccionado el usuario
    // en la lista de paises. Esta variable obtiene su valor desde
    // el controlador de sesión.
    private CasoDeUsoType casoDeUso;

    @PostConstruct
    public void init() {
        nacionalidad = new Nacionalidad();
        casoDeUso = CasoDeUsoType.ALTA;
    }

    public String aceptar() {

        try {
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso) {
                
                case ALTA:
                    nacionalidadService.crearNacionalidad(
                            nacionalidad.getNombre());
                    Message.show("Nacionalidad creada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    nacionalidadService.modificarNacionalidad(
                            nacionalidad.getId(),
                            nacionalidad.getNombre());
                    Message.show("Nacionalidad modificada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case CONSULTAR:
                    break;
            }
            
        } catch (ErrorServiceException ex) {
            Message.show(ex.getMessage(), MessageType.ERROR);
        }
        
        return "listNacionalidad";
    }

    public String cancelar() {

        return "listNacionalidad";
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    


}
