/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.pais;

import com.interisys.business.domain.entity.Pais;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.PaisServiceBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class PaisEditController {

    private Pais pais;
    private @EJB PaisServiceBean paisService;
    
    // El caso de uso determina que función hace esta clase.
    // Esto depende de la opción que haya seleccionado el usuario
    // en la lista de paises. Esta variable obtiene su valor desde
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
        pais = new Pais();
    }
    
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
    
    public String aceptar()
    {
        
        if (casoDeUso.equals("ALTA"))
        {        
            try {
                paisService.crearPais(pais.getNombre());
            } catch (ErrorServiceException ex) {
                Logger.getLogger(PaisEditController.class.getName()).log(Level.SEVERE, null, ex);
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
