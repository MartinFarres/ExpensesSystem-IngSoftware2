/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.nacionalidad;

import com.interisys.business.domain.entity.Nacionalidad;
import com.interisys.business.logic.NacionalidadServiceBean;
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
public class NacionalidadListController {

    private @EJB NacionalidadServiceBean nacionalidadService;
    
    private Collection<Nacionalidad> nacionalidades =new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarNacionalidad();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarNacionalidad(){
      try{  
          
        nacionalidades.clear();
        nacionalidades.addAll(nacionalidadService.listarNacionalidadActiva());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaNacionalidad");
        RequestContext.getCurrentInstance().update("formPpal:tablaNacionalidad");
            
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarNacionalidadSession(CasoDeUsoType.ALTA, null);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Nacionalidad nacionalidad) {
        
        try{
            
            guardarNacionalidadSession(CasoDeUsoType.CONSULTAR, nacionalidad);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Nacionalidad nacionalidad) {
        
        try{
            
            guardarNacionalidadSession(CasoDeUsoType.MODIFICAR, nacionalidad);
            return "editNacionalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Nacionalidad nacionalidad) {
        
        try{
        
            nacionalidadService.eliminarNacionalidad(nacionalidad.getId());
            listarNacionalidad();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
        return "listNacionalidad";

    }

    private void guardarNacionalidadSession(CasoDeUsoType casoDeUso, Nacionalidad nacionalidad){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("NACIONALIDAD", nacionalidad);  
    }

    public Collection<Nacionalidad> getNacionalidades() {
        return nacionalidades;
    }

    public void setNacionalidades(Collection<Nacionalidad> nacionalidades) {
        this.nacionalidades = nacionalidades;
    }

}