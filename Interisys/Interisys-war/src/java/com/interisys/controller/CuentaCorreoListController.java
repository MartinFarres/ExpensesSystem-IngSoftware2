/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.CuentaCorreo;
import com.interisys.business.logic.CuentaCorreoServiceBean;
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
import org.hibernate.validator.internal.util.logging.Messages;
import org.primefaces.context.RequestContext;

/**
 *
 * @author spaul
 */
@ManagedBean
@ViewScoped
public class CuentaCorreoListController {
    
    private @EJB CuentaCorreoServiceBean cuentaCorreoService;
    
    private Collection<CuentaCorreo> cuentas=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarCuentaCorreo();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarCuentaCorreo(){
      try{  
          
        cuentas.clear();
        cuentas.addAll(cuentaCorreoService.listarCuentaCorreoActiva());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaCuentaCorreo");
        RequestContext.getCurrentInstance().update("formPpal:tablaCuentaCorreo");
            
      } catch (Exception e) {
        e.printStackTrace();
        Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarCuentaCorreoSession("ALTA", null);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(CuentaCorreo cuentaCorreo) {
        
        try{
            
            guardarCuentaCorreoSession("CONSULTAR", cuentaCorreo);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(CuentaCorreo cuentaCorreo) {
        
        try{
            
            guardarCuentaCorreoSession("MODIFICAR", cuentaCorreo);
            return "editCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public void baja(CuentaCorreo cuentaCorreo) {
        
        try{
        
            cuentaCorreoService.eliminarCuentaCorreo(cuentaCorreo.getId());
            listarCuentaCorreo();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public Collection<CuentaCorreo> getCuentas() {
        return cuentas;
    }

    public void setCuentas(Collection<CuentaCorreo> cuentas) {
        this.cuentas = cuentas;
    }

    private void guardarCuentaCorreoSession(String casoDeUso, CuentaCorreo cuentaCorreo){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso.toUpperCase());  
        session.setAttribute("CUENTACORREO", cuentaCorreo);  
    }
    
}

