/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.inquilino;

import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.logic.InquilinoServiceBean;
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
 * @author franc
 */
@ManagedBean
@ViewScoped
public class InquilinoListController {

    private @EJB InquilinoServiceBean inquilinoService;
    
    private Collection<Inquilino> inquilinos=new ArrayList();

    public Collection<Inquilino> getInquilinos() {
        return inquilinos;
    }

    public void setInquilinos(Collection<Inquilino> inquilinos) {
        this.inquilinos = inquilinos;
    }

    @PostConstruct
    public void init() {
        try{
            
             listarInquilinos();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarInquilinos(){
      try{  
          
        inquilinos.clear();
        inquilinos.addAll(inquilinoService.listarInquilinoActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaInquilino");
        RequestContext.getCurrentInstance().update("formPpal:tablaInquilino");
        
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarInquilinoSession(CasoDeUsoType.ALTA, null);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Inquilino inquilino) {
        
        try{
            
            guardarInquilinoSession(CasoDeUsoType.CONSULTAR, inquilino);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Inquilino inquilino) {
        
        try{
            
            guardarInquilinoSession(CasoDeUsoType.MODIFICAR, inquilino);
            return "editInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Inquilino inquilino) {
        
        try{
        
            inquilinoService.eliminarInquilino(inquilino.getId());
            listarInquilinos();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
        return "listInquilino";
    }

    private void guardarInquilinoSession(CasoDeUsoType casoDeUso, Inquilino inquilino){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("INQUILINO", inquilino);  
    }
}