/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.departamento;

import com.interisys.business.domain.entity.Provincia; 
import com.interisys.business.domain.entity.Departamento;
import com.interisys.business.logic.ProvinciaServiceBean; 
import com.interisys.business.logic.DepartamentoServiceBean;
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
public class DepartamentoEditController {
    private Departamento departamento;
    private @EJB DepartamentoServiceBean departamentoService;
    private @EJB ProvinciaServiceBean provinciaService; 
    // Variables Vista
    private Collection<SelectItem> provincias = new ArrayList(); 
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String idProvincia; 
    private String nombre;
    
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
            departamento = (Departamento) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DEPARTAMENTO");
            campoDesactivado = false;

            if (casoDeUso.equals("ALTA")) {
                cargarComboProvincia(); 
            } else if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")) {
                idProvincia = departamento.getProvincia().getId(); 
                nombre = departamento.getNombre();
                cargarComboProvincia(); 
                
                if (casoDeUso.equals("CONSULTAR"))
                    campoDesactivado = true;
            }
            
            cargarComboProvincia(); 
             
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public String getIdProvincia() { 
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) { 
        this.idProvincia = idProvincia; 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    public Collection<SelectItem> getProvincias() { 
        return provincias; 
    }

    public void setProvincias(Collection<SelectItem> provincias) { 
        this.provincias = provincias; 
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
                    departamentoService.crearDepartamento(idProvincia, nombre); 
                    Message.show("Departamento creado exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    departamentoService.modificarDepartamento(
                            departamento.getId(),
                            idProvincia, 
                            nombre);
                    Message.show("Departamento modificado exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break; 
            }

            return "listDepartamento";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void cargarComboProvincia() { 
        try {  
            provincias = new ArrayList<SelectItem>(); 
            provincias.add(new SelectItem(null, "Seleccione...")); 
            for (Provincia provincia : provinciaService.listarProvinciaActiva()) { 
                provincias.add(new SelectItem(provincia.getId(), provincia.getNombre())); 
            }    
        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String cancelar() {
        return "listDepartamento";
    }
}
