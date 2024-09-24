/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.inquilino;

import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Nacionalidad;
import com.interisys.business.domain.enumeration.Sexo;
import com.interisys.business.domain.enumeration.TipoDocumento;
import com.interisys.business.logic.InquilinoServiceBean;
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
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class InquilinoEditController {

    private @EJB InquilinoServiceBean inquilinoService;
    private @EJB NacionalidadServiceBean nacionalidadService;

    private Inquilino inquilino;
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    
    private Collection<SelectItem> nacionalidades=new ArrayList();
    private Collection<SelectItem> tiposDocumentos=new ArrayList();
    private Collection<SelectItem> sexos=new ArrayList();
    
    @PostConstruct
    public void init() {
        //Se obtiene el caso de uso  
        casoDeUso = (CasoDeUsoType)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
        
        // Cuando el caso de uso es alta, crea la expensa
        if (casoDeUso == CasoDeUsoType.ALTA)
            inquilino = new Inquilino();
        else
            // Obtiene la expensa a traves del controlador de sesion
            inquilino = (Inquilino)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INQUILINO");
        
        // Los campos se desactiva en en caso de uso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
        
        
        cargarComboNacionalidad();
        cargarComboSexo();
        cargarComboTipoDocumento();
    }
    
    public String aceptar() {
        try{
            
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso)
            {

                case ALTA:
                    inquilinoService.crearInquilino(
                            inquilino.getNombre(),
                            inquilino.getApellido(),
                            inquilino.getTelefono(),
                            inquilino.getCorreoElectronico(),
                            inquilino.getFechaNacimiento(),
                            inquilino.getSexo(),
                            inquilino.getTipoDocumento(),
                            inquilino.getDocumento(),
                            inquilino.getNacionalidad().getId());
                    Message.show("Inquilino creado exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    inquilinoService.modificarInquilino(
                            inquilino.getId(),
                            inquilino.getNombre(),
                            inquilino.getApellido(),
                            inquilino.getTelefono(),
                            inquilino.getCorreoElectronico(),
                            inquilino.getFechaNacimiento(),
                            inquilino.getSexo(),
                            inquilino.getTipoDocumento(),
                            inquilino.getDocumento(),
                            inquilino.getNacionalidad().getId());
                    Message.show("Inquilino modificado exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break;
            }

            return "listInquilino";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String cancelar()
    {
        return "listInquilino";
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
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

    public Collection<SelectItem> getNacionalidades() {
        return nacionalidades;
    }

    public void setNacionalidades(Collection<SelectItem> nacionalidades) {
        this.nacionalidades = nacionalidades;
    }

    public Collection<SelectItem> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(Collection<SelectItem> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public Collection<SelectItem> getSexos() {
        return sexos;
    }

    public void setSexos(Collection<SelectItem> sexos) {
        this.sexos = sexos;
    }

        private void cargarComboSexo() {
        
       try{  
           
         sexos = new ArrayList<>();
         sexos.add(new SelectItem(null, "Seleccione..."));
         sexos.add(new SelectItem(Sexo.FEMENINO, "FEMENINO"));
         sexos.add(new SelectItem(Sexo.MASCULINO, "MASCULINO"));
         sexos.add(new SelectItem(Sexo.OTRO, "OTRO"));
        
       }catch(Exception e){
         Message.errorSystem();  
       }  
    }
    
    private void cargarComboTipoDocumento() {
        
       try{  
           
         tiposDocumentos = new ArrayList<>();
         tiposDocumentos.add(new SelectItem(null, "Seleccione..."));
         tiposDocumentos= new ArrayList<>();
         tiposDocumentos.add(new SelectItem(null, "Seleccione..."));
         tiposDocumentos.add(new SelectItem(TipoDocumento.DOCUMENTO_UNICO, "DNI"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.PASAPORTE, "PASAPORTE"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.CERTIFICADO_MIGRATORIO, "CERTIFICADO MIGRATORIO"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.LIBRETA_CIVICA, "LIBRETA CIVICA"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.LIBRETA_DE_ENROLAMIENTO, "LIBRETA DE ENROLAMIENTO"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.EN_TRAMITE_RECIEN_NACIDO, "EN TRAMITE RECIEN NACIDO"));
         tiposDocumentos.add(new SelectItem(TipoDocumento.SIN_INFORMACION, "SIN INFORMACION"));
        
       }catch(Exception e){
         Message.errorSystem();  
       }  
    }
    
    public void cargarComboNacionalidad(){
      try{  
        
            nacionalidades = new ArrayList<>();
            nacionalidades.add(new SelectItem(null, "Seleccione..."));
            for(Nacionalidad nacionalidad: nacionalidadService.listarNacionalidadActiva()){
              nacionalidades.add(new SelectItem(nacionalidad.getId(), nacionalidad.getNombre()));
            }
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }
    
    
}
