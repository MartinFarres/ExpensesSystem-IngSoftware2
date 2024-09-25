
package com.interisys.controller.localidad;

import com.interisys.business.domain.entity.Departamento;
import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.logic.DepartamentoServiceBean;
import com.interisys.business.logic.LocalidadServiceBean;
import com.interisys.business.logic.ProvinciaServiceBean;
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
* @author aidal
 */
@ManagedBean
@ViewScoped
public class LocalidadEditController {
    private Localidad localidad;
    private @EJB LocalidadServiceBean localidadService;
    private @EJB DepartamentoServiceBean departamentoService; 
    // Variables Vista
    private Collection<SelectItem> departamentos = new ArrayList(); 
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String idDepartamento; 
    private String nombre;
    private String codigoPostal;
    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
            localidad = (Localidad) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOCALIDAD");
            campoDesactivado = false;

            if (casoDeUso.equals("ALTA")) {
                cargarComboDepartamento(); 
            } else if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                idDepartamento = localidad.getDepartamento().getId(); 
                nombre = localidad.getNombre();
                codigoPostal = localidad.getCodigoPostal();
                cargarComboDepartamento(); 
                
                if (casoDeUso.equals("CONSULTAR"))
                    campoDesactivado = true;
            }
            
            cargarComboDepartamento(); 
             
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public String getIdDepartamento() { 
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) { 
        this.idDepartamento = idDepartamento; 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }
    
    public Collection<SelectItem> getDepartamentos() { 
        return departamentos; 
    }

    public void setDepartamentos(Collection<SelectItem> departamentos) { 
        this.departamentos = departamentos; 
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }
    
    public String aceptar() {
        try {
            switch (casoDeUso) {
                case ALTA:
                    localidadService.crearLocalidad(idDepartamento, nombre ,codigoPostal ); 
                    Message.show("Localidad creada exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    localidadService.modificarLocalidad(
                            localidad.getId(),
                            idDepartamento, 
                            nombre,
                            codigoPostal);
                    Message.show("Localidad modificada exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break; 
            }

            return "listLocalidad";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void cargarComboDepartamento() { 
        try {  
            departamentos = new ArrayList<SelectItem>(); 
            departamentos.add(new SelectItem(null, "Seleccione...")); 
            for (Departamento departamento : departamentoService.listarDepartamentoActivo()) { 
                departamentos.add(new SelectItem(departamento.getId(), departamento.getNombre())); 
            }    
        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String cancelar() {
        return "listLocalidad";
    }
}
