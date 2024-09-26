package com.interisys.controller.usuario;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.logic.PerfilServiceBean;
import com.interisys.controller.Message;
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
 * @author martin
 */
@ManagedBean
@ViewScoped
public class PerfilListController {

    private @EJB PerfilServiceBean perfilService;
    
    private Collection<Perfil> perfiles=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarPerfiles();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarPerfiles(){
      try{  
        
        perfiles.clear();
        perfiles.addAll(perfilService.listarPerfilActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaPerfil");
        RequestContext.getCurrentInstance().update("formPpal:tablaPerfil");
            
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarPerfilSession(CasoDeUsoType.ALTA, null);
            return "editPerfil";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Perfil perfil) {
        
        try{
            
            guardarPerfilSession(CasoDeUsoType.CONSULTAR, perfil);
            return "editPerfil";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Perfil perfil) {
        
        try{
            
            guardarPerfilSession(CasoDeUsoType.MODIFICAR, perfil);
            return "editPerfil";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public void baja(Perfil perfil) {
        
        try{
        
            perfilService.eliminarPerfil(perfil.getId());
            listarPerfiles();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public PerfilServiceBean getPerfilService() {
        return perfilService;
    }

    public void setPerfilService(PerfilServiceBean perfilService) {
        this.perfilService = perfilService;
    }

    public Collection<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Collection<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    private void guardarPerfilSession(CasoDeUsoType casoDeUso, Perfil perfil){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("PERFIL", perfil);  
    }
    
}