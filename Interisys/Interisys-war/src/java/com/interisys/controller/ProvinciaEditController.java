/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.ProvinciaServiceBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author spaul
 */
public class ProvinciaEditController {
    private Provincia provincia;
    private @EJB ProvinciaServiceBean provinciaService;
    
    // El caso de uso determina que función hace esta clase.
    // Esto depende de la opción que haya seleccionado el usuario
    // en la lista de provincias. Esta variable obtiene su valor desde
    // el controlador de sesión.
    private String casoDeUso;

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    @PostConstruct
    public void init() {
        provincia = new Provincia();
    }
    
    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    
    public String aceptar()
    {
        
        if (casoDeUso.equals("ALTA"))
        {        
            try {
                provinciaService.crearProvincia(provincia.getNombre());
            } catch (ErrorServiceException ex) {
                Logger.getLogger(ProvinciaEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
        }
            
        return null;
    }
    
    public String cancelar()
    {
        
        return "index";
    }
    
}
