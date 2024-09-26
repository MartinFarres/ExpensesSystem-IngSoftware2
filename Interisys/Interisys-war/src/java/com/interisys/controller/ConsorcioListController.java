/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.logic.ConsorcioServiceBean;
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
public class ConsorcioListController {
    
    private @EJB ConsorcioServiceBean consorcioService;
    
    private Collection<Consorcio> consorcios = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            listarConsorcio();
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarConsorcio() {
        try {  
            consorcios.clear();
            consorcios.addAll(consorcioService.listarConsorcioActivo());
            
            RequestContext.getCurrentInstance().update("formPpal:panelTablaConsorcio");
            RequestContext.getCurrentInstance().update("formPpal:tablaConsorcio");
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }  
    }
    
    public String alta() {
        try {
            guardarConsorcioSession(CasoDeUsoType.ALTA, null);
            return "editConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Consorcio consorcio) {
        try {
            guardarConsorcioSession(CasoDeUsoType.CONSULTAR, consorcio);
            return "editConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Consorcio consorcio) {
        try {
            guardarConsorcioSession(CasoDeUsoType.MODIFICAR, consorcio);
            return "editConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Consorcio consorcio) {
        try {
            consorcioService.eliminarConsorcio(consorcio.getId());
            listarConsorcio();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");
            return "listConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public ConsorcioServiceBean getConsorcioService() {
        return consorcioService;
    }

    public void setConsorcioService(ConsorcioServiceBean consorcioService) {
        this.consorcioService = consorcioService;
    }

    public Collection<Consorcio> getConsorcios() {
        return consorcios;
    }

    public void setConsorcios(Collection<Consorcio> consorcios) {
        this.consorcios = consorcios;
    }

    private void guardarConsorcioSession(CasoDeUsoType casoDeUso, Consorcio consorcio) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("CONSORCIO", consorcio);  
    }
}
