/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor.
 */
package com.interisys.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author franc
 */
@ManagedBean
@RequestScoped
public class IndexNavigationController {
    
    public String navegarPais() {
        return "editPais";
    }
    
    public String navegarExpensa() {
        return "listExpensa";
    }
    
    public String navegarProvincia() {
        return "listProvincia";
    }

    public String navegarDepartamento()
            
    {
        return "listDepartamento";
    }
    
    public String navegarCuentaCorreo()
    {
        return "listCuentaCorreo";
    }
    
    public String navegarConsorcio()
    {
        return "listConsorcio";
        
    }

    public String navegarDireccion() {
        return "listDireccion";
    }
    
    public String navegarLocalidad() {
        return "listLocalidad";
    }

    public String navegarInquilino() {
        return "listInquilino";

    }
    
    public String navegarPropietario() {
        return "listPropietario";
    }
    
    public String navegarNacionalidad() {
        return "listNacionalidad";
    }
    
    public String navegarInmueble() {
        return "listInmueble";
    }
    
    public String navegarExpensaInmueble() {
        return "listExpensaInmueble";
    }
    
    public String navegarUsuarioPerfiles() {
        return "listUsuarioPerfiles";
    }
    
    public String navegarUsuarioUsuarios(){
        return "listUsuarioUsuarios";
    }
}



