/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.expensainmueble;

import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.ExpensaInmuebleServiceBean;
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
public class ExpensaInmuebleListController {

    //Servicio Capa de Negocio
    private @EJB ExpensaInmuebleServiceBean expensaService;
    
    //Variable Capa de Negocio
    private Collection<ExpensaInmueble> expensasInmueble=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarExpensaInmueble();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarExpensaInmueble(){
      try{  
          
        expensasInmueble.clear();
        expensasInmueble.addAll(expensaService.listarExpensaInmuebleActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaExpensaInmueble");
        RequestContext.getCurrentInstance().update("formPpal:tablaExpensaInmueble");
            
      } catch (Exception e) {
        e.printStackTrace();
        Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarExpensaInmuebleSession(CasoDeUsoType.ALTA, null);
            return "editExpensaInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public void generarExpensas()
    {
        try {
            expensaService.crearExpesaInmueble();
            RequestContext.getCurrentInstance().update("formPpal:panelTablaExpensaInmueble");
            RequestContext.getCurrentInstance().update("formPpal:tablaExpensaInmueble");
        } catch (ErrorServiceException e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String consultar(ExpensaInmueble expensa) {
        
        try{
            
            guardarExpensaInmuebleSession(CasoDeUsoType.CONSULTAR, expensa);
            return "editExpensaInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(ExpensaInmueble expensa) {
        
        try{
            
            guardarExpensaInmuebleSession(CasoDeUsoType.MODIFICAR, expensa);
            return "editExpensaInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public void baja(ExpensaInmueble expensa) {
        
        try{
        
            expensaService.eliminarExpensaInmueble(expensa.getId());
            listarExpensaInmueble();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public ExpensaInmuebleServiceBean getExpensaInmuebleService() {
        return expensaService;
    }

    public void setExpensaInmuebleService(ExpensaInmuebleServiceBean expensaService) {
        this.expensaService = expensaService;
    }

    public Collection<ExpensaInmueble> getExpensasInmuebles() {
        return expensasInmueble;
    }

    public void setExpensasInmuebles(Collection<ExpensaInmueble> expensasInmueble) {
        this.expensasInmueble = expensasInmueble;
    }

    private void guardarExpensaInmuebleSession(CasoDeUsoType casoDeUso, ExpensaInmueble expensa){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("EXPENSAINMUEBLE", expensa);  
    }
    
}
