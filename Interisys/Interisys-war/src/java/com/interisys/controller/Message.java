/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.controller.enumeration.MessageType;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author franc
 */
public class Message {
    
    public static void show(String texto, MessageType tipo){
    
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;;
        
        switch(tipo){
        case NOTIFICACION:
            severity = FacesMessage.SEVERITY_INFO;
            break;
        case ALERTA:
            severity = FacesMessage.SEVERITY_WARN;
            break;
        case ERROR:
            severity = FacesMessage.SEVERITY_ERROR;
            break;
        case ERRORFATAL:    
            severity = FacesMessage.SEVERITY_FATAL;
            break;
        }
                    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "", texto));
        
    }
    
    public static void errorSystem(){
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Error de Sistemas"));
    }
    
}
