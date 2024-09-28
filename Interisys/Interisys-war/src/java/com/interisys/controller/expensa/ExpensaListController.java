/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.expensa;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.logic.ExpensaServiceBean;
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
 * @author franc
 */
@ManagedBean
@ViewScoped
public class ExpensaListController {

    private @EJB ExpensaServiceBean expensaService;
    
    private Collection<Expensa> expensas=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarExpensa();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarExpensa(){
      try{  
          
        expensas.clear();
        expensas.addAll(expensaService.listarExpensasActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaExpensa");
        RequestContext.getCurrentInstance().update("formPpal:tablaExpensa");
            
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarExpensaSession(CasoDeUsoType.ALTA, null);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Expensa expensa) {
        
        try{
            
            guardarExpensaSession(CasoDeUsoType.CONSULTAR, expensa);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Expensa expensa) {
        
        try{
            
            guardarExpensaSession(CasoDeUsoType.MODIFICAR, expensa);
            return "editExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Expensa expensa) {
        
        try{
        
            expensaService.eliminarExpensa(expensa.getId());
            listarExpensa();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
        return "listExpensa";
    }

    public ExpensaServiceBean getExpensaService() {
        return expensaService;
    }

    public void setExpensaService(ExpensaServiceBean expensaService) {
        this.expensaService = expensaService;
    }

    public Collection<Expensa> getExpensas() {
        return expensas;
    }

    public void setExpensas(Collection<Expensa> expensas) {
        this.expensas = expensas;
    }

    private void guardarExpensaSession(CasoDeUsoType casoDeUso, Expensa expensa){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("EXPENSA", expensa);  
    }
    
}