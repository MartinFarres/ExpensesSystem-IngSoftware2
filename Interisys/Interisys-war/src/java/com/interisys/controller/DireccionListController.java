/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.logic.DireccionServiceBean;
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
 * @author spaul
 */
@ManagedBean
@ViewScoped
public class DireccionListController {

    private @EJB DireccionServiceBean direccionService;
    
    private Collection<Direccion> direcciones = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            listarDireccion();
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarDireccion() {
        try {  
            direcciones.clear();
            direcciones.addAll(direccionService.listarDireccionActiva());
            
            RequestContext.getCurrentInstance().update("formPpal:panelTablaDireccion");
            RequestContext.getCurrentInstance().update("formPpal:tablaDireccion");
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }  
    }
    
    public String alta() {
        try {
            guardarDireccionSession(CasoDeUsoType.ALTA, null);
            return "editDireccion";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Direccion direccion) {
        try {
            guardarDireccionSession(CasoDeUsoType.CONSULTAR, direccion);
            return "editDireccion";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Direccion direccion) {
        try {
            guardarDireccionSession(CasoDeUsoType.MODIFICAR, direccion);
            return "editDireccion";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Direccion direccion) {
        try {
            direccionService.eliminarDireccion(direccion.getId());
            listarDireccion();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");
            return "listDireccion";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public DireccionServiceBean getDireccionService() {
        return direccionService;
    }

    public void setDireccionService(DireccionServiceBean direccionService) {
        this.direccionService = direccionService;
    }

    public Collection<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    private void guardarDireccionSession(CasoDeUsoType casoDeUso, Direccion direccion) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("DIRECCION", direccion);
    }
}
