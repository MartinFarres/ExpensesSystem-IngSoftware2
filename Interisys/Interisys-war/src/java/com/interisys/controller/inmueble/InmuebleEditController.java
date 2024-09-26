/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.inmueble;

import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.domain.enumeration.EstadoInmueble;
import com.interisys.business.logic.InmuebleServiceBean;
import com.interisys.business.logic.InquilinoServiceBean;
import com.interisys.business.logic.PropietarioServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author franc
 */

@ManagedBean
@ViewScoped
public class InmuebleEditController {

    //Servicios Capa de Negocio
    private @EJB InmuebleServiceBean inmuebleService;
    private @EJB PropietarioServiceBean propietarioService;
    private @EJB InquilinoServiceBean inquilinoService;

    //Variables Capa de Negocio
    private Inmueble inmueble;
    //Variables Vista
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private Collection<SelectItem> propietarios=new ArrayList();
    private Collection<SelectItem> inquilinos=new ArrayList();
    private Collection<SelectItem> estados=new ArrayList();

    @PostConstruct
    public void init() {
        //Se obtiene el caso de uso  
        casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");

        // Cuando el caso de uso es alta, crea inmueble
        if (casoDeUso == CasoDeUsoType.ALTA) {
            inmueble = new Inmueble();
            inmueble.setPropietario(new Propietario());
            inmueble.setInquilino(new Inquilino());

        } else // Obtiene la expensa a traves del controlador de sesion
        {
            inmueble = (Inmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INMUEBLE");
        }

        // Los campos se desactiva en en caso de uso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
        
        cargaComboEstado();
        cargaComboInquilinos();
        cargaComboPropietarios();
    }
    
    public String aceptar() {
        
        try{
            
            switch (casoDeUso)
            {
                case ALTA:
                    inmuebleService.crearInmueble(
                            inmueble.getPropietario().getId(), 
                            inmueble.getInquilino().getId(), 
                            inmueble.getPiso(), 
                            inmueble.getPuerta());
                    break;
                case MODIFICAR:
                    inmuebleService.modificarInmueble(
                            inmueble.getId(),
                            inmueble.getPropietario().getId(), 
                            inmueble.getInquilino().getId(), 
                            inmueble.getPiso(), 
                            inmueble.getPuerta());
                    break;
                case CONSULTAR:
                    break;
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
        return "listInmueble";
    }
    
    public String cancelar() {
        return "listInmueble";
    }
    
    public void cargaComboPropietarios(){
      try{  
        
            propietarios = new ArrayList<>();
            propietarios.add(new SelectItem(null, "Seleccione..."));
            for(Propietario propietario: propietarioService.listarPropietarioActivo()){
              propietarios.add(new SelectItem(propietario.getId(), propietario.nombreApellido()));
            }
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }
    
    public void cargaComboInquilinos(){
      try{  
        
            inquilinos = new ArrayList<>();
            inquilinos.add(new SelectItem(null, "Seleccione..."));
            for(Inquilino inquilino: inquilinoService.listarInquilinoActivo()){
              inquilinos.add(new SelectItem(inquilino.getId(), inquilino.nombreApellido()));
            }
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }
    
    public void cargaComboEstado(){
      try{  
        
            estados = new ArrayList<>();
            estados.add(new SelectItem(null, "Seleccione..."));
            estados.add(new SelectItem(EstadoInmueble.HABITADO, "HABITADO"));
            estados.add(new SelectItem(EstadoInmueble.DESOCUPADO, "DESOCUPADO"));
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public Collection<SelectItem> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(Collection<SelectItem> propietarios) {
        this.propietarios = propietarios;
    }

    public Collection<SelectItem> getInquilinos() {
        return inquilinos;
    }

    public void setInquilinos(Collection<SelectItem> inquilinos) {
        this.inquilinos = inquilinos;
    }

    public Collection<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(Collection<SelectItem> estados) {
        this.estados = estados;
    }
    
    
}
