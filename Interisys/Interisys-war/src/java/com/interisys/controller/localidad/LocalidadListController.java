/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.localidad;

import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.logic.LocalidadServiceBean;
import com.interisys.controller.enumeration.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author aidal
 */
@ManagedBean
@ViewScoped
public class LocalidadListController {

    private @EJB LocalidadServiceBean localidadService;
    
    private Collection<Localidad> localidades = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            listarLocalidad();
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarLocalidad() {
        try {  
            localidades.clear();
            localidades.addAll(localidadService.listarLocalidadActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:panelTablaLocalidad");
            RequestContext.getCurrentInstance().update("formPpal:tablaLocalidad");
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }  
    }
    
    public String alta() {
        try {
            guardarLocalidadSession(CasoDeUsoType.ALTA, null);
            return "editLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Localidad localidad) {
        try {
            guardarLocalidadSession(CasoDeUsoType.CONSULTAR, localidad);
            return "editLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Localidad localidad) {
        try {
            guardarLocalidadSession(CasoDeUsoType.MODIFICAR, localidad);
            return "editLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Localidad localidad) {
        try {
            localidadService.eliminarLocalidad(localidad.getId());
            listarLocalidad();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");
            return "listLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public LocalidadServiceBean getLocalidadService() {
        return localidadService;
    }

    public void setLocalidadService(LocalidadServiceBean localidadService) {
        this.localidadService = localidadService;
    }

    public Collection<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(Collection<Localidad> localidades) {
        this.localidades = localidades;
    }

    private void guardarLocalidadSession(CasoDeUsoType casoDeUso, Localidad localidad) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("LOCALIDAD", localidad);
    }
}

