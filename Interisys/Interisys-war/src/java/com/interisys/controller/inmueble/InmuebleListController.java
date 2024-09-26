package com.interisys.controller.inmueble;

import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.logic.InmuebleServiceBean;
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


@ManagedBean
@ViewScoped
public class InmuebleListController {

    //Servicio Capa de Negocio
    private @EJB InmuebleServiceBean inmuebleService;
    
    //Variable Capa de Negocio
    private Collection<Inmueble> inmuebles=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarInmueble();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarInmueble(){
      try{  
          
        inmuebles.clear();
        inmuebles.addAll(inmuebleService.listarInmuebleActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaInmueble");
        RequestContext.getCurrentInstance().update("formPpal:tablaInmueble");
            
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarInmuebleSession(CasoDeUsoType.ALTA, null);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Inmueble inmueble) {
        
        try{
            
            guardarInmuebleSession(CasoDeUsoType.CONSULTAR, inmueble);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Inmueble inmueble) {
        
        try{
            
            guardarInmuebleSession(CasoDeUsoType.MODIFICAR, inmueble);
            return "editInmueble";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Inmueble inmueble) {
        
        try{
        
            inmuebleService.eliminarInmueble(inmueble.getId());
            listarInmueble();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
        return "listInmueble";
    }

    public Collection<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Collection<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }
    
    private void guardarInmuebleSession(CasoDeUsoType casoDeUso, Inmueble inmueble){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("INMUEBLE", inmueble);  
    }
    
}