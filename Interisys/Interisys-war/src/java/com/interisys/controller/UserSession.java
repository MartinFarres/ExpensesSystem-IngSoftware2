/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;
import com.interisys.business.domain.entity.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author martin
 */
@ManagedBean
@ViewScoped
public class UserSession {
    
    private Usuario usuario;

    @PostConstruct
    public void init() {
        usuario = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USUARIO_LOGGED");
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    // Método para obtener la fecha de hoy
    public String getToday() {
        DateFormat df = new SimpleDateFormat("EEEE d 'de' MMMM", new Locale("es", "ES"));
        return df.format(new Date());
    }
}
