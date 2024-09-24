/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.logic.ProvinciaServiceBean;
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
public class ProvinciaListController {
    
    private @EJB ProvinciaServiceBean provinciaService;
    
    private Collection<Provincia> provincias = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            listarProvincia();
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarProvincia() {
        try {  
            provincias.clear();
            provincias.addAll(provinciaService.listarProvinciaActiva());
            
            RequestContext.getCurrentInstance().update("formPpal:panelTablaProvincia");
            RequestContext.getCurrentInstance().update("formPpal:tablaProvincia");
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }  
    }
    
    public String alta() {
        try {
            guardarProvinciaSession(CasoDeUsoType.ALTA, null);
            return "editProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Provincia provincia) {
        try {
            guardarProvinciaSession(CasoDeUsoType.CONSULTAR, provincia);
            return "editProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Provincia provincia) {
        try {
            guardarProvinciaSession(CasoDeUsoType.MODIFICAR, provincia);
            return "editProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Provincia provincia) {
        try {
            provinciaService.eliminarProvincia(provincia.getId());
            listarProvincia();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");
            return "listProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public ProvinciaServiceBean getProvinciaService() {
        return provinciaService;
    }

    public void setProvinciaService(ProvinciaServiceBean provinciaService) {
        this.provinciaService = provinciaService;
    }

    public Collection<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(Collection<Provincia> provincias) {
        this.provincias = provincias;
    }

    private void guardarProvinciaSession(CasoDeUsoType casoDeUso, Provincia provincia) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("PROVINCIA", provincia);  
    }
}

