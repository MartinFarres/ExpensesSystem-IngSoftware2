/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.controller.enumeration.MessageType;
import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.logic.ExpensaServiceBean;
import com.interisys.controller.enumeration.CasoDeUsoType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class ExpensaEditController {

    private @EJB ExpensaServiceBean expensaService;
    private Expensa expensa;
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    
    @PostConstruct
    public void init() {
        //Se obtiene el caso de uso  
        casoDeUso = (CasoDeUsoType)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
        
        // Cuando el caso de uso es alta, crea la expensa
        if (casoDeUso == CasoDeUsoType.ALTA)
            expensa = new Expensa();
        else
            // Obtiene la expensa a traves del controlador de sesion
            expensa = (Expensa)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSA");
        
        // Los campos se desactiva en en caso de uso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
         
    }
    
    public String aceptar() {
        try{
            
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso)
            {

                case ALTA:
                    expensaService.crearExpensa(
                            expensa.getFechaDesde(), 
                            expensa.getFechaHasta(), 
                            expensa.getImporte());
                    Message.show("Expensa creada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    expensaService.modificarExpensa(
                            expensa.getId(), 
                            expensa.getFechaDesde(), 
                            expensa.getFechaHasta(), 
                            expensa.getImporte());
                    Message.show("Expensa modificada exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break;
            }

            return "listExpensa";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String cancelar() {
        return "index";
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public Expensa getExpensa() {
        return expensa;
    }
    
    public void setExpensa(Expensa expensa)
    {
        this.expensa = expensa;
    }

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
} 