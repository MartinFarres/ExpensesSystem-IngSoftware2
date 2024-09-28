/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.provincia;

import com.interisys.business.domain.entity.Pais;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.logic.PaisServiceBean;
import com.interisys.business.logic.ProvinciaServiceBean;
import com.interisys.controller.enumeration.Message;
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
 * @author spaul
 */

@ManagedBean
@ViewScoped
public class ProvinciaEditController {
    private Provincia provincia;
    private @EJB ProvinciaServiceBean provinciaService;
    private @EJB PaisServiceBean paisService;
    //Variables Vista
    private Collection<SelectItem> paises=new ArrayList();
    // El caso de uso determina que función hace esta clase.
    // Esto depende de la opción que haya seleccionado el usuario
    // en la lista de provincias. Esta variable obtiene su valor desde
    // el controlador de sesión.
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String idPais;
    private String nombre;

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    @PostConstruct
    public void init() {
        try{
        //Se obtiene el caso de uso  
        casoDeUso = (CasoDeUsoType)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
        provincia = (Provincia)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PROVINCIA");
        campoDesactivado = false;

        if (casoDeUso.equals("ALTA")){
           cargarComboPais();
         } else if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")){
           idPais = provincia.getPais().getId();
           nombre = provincia.getNombre();
           cargarComboPais();
           
           if (casoDeUso.equals("CONSULTAR"))
            campoDesactivado = true;
         }
        
        cargarComboPais();
         
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
            
    
     
  
   
    
    public Provincia getProvincia() {
        return provincia;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    
    public Collection<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(Collection<SelectItem> paises) {
        this.paises = paises;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }
    
    
    public String aceptar() {
        try{
            
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso)
            {

                case ALTA:
                    provinciaService.crearProvincia(idPais, nombre);
                    Message.show("Provincia creada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    provinciaService.modificarProvincia(
                            provincia.getId(),
                            idPais,
                            nombre);
                    Message.show("Provincia modificada exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break;
            }

            return "listProvincia";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void cargarComboPais(){
      try{  
        
            paises = new ArrayList<SelectItem>();
            paises.add(new SelectItem(null, "Seleccione..."));
            for(Pais pais: paisService.listarPaisActivo()){
              paises.add(new SelectItem(pais.getId(), pais.getNombre()));
            }
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }
    
    public String cancelar()
    {
        return "listProvincia";
    }
    
}
