package com.interisys.controller.usuario;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.logic.UsuarioServiceBean;
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

/**
 *
 * @author martin
 */
@ManagedBean
@ViewScoped
public class UsuarioListController {

    private @EJB UsuarioServiceBean usuarioService;
    
    private Collection<Usuario> usuarios=new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
             listarUsuarios();
             
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarUsuarios(){
      try{  
        
        usuarios.clear();
        usuarios.addAll(usuarioService.listarUsuarioActivo());
        
        RequestContext.getCurrentInstance().update("formPpal:panelTablaUsuario");
        RequestContext.getCurrentInstance().update("formPpal:tablaUsuario");
            
      } catch (Exception e) {
        e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
      }  
    }
    
    public String alta() {
        
        try{
            
            guardarUsuarioSession(CasoDeUsoType.ALTA, null);
            return "editUsuario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Usuario usuario) {
        
        try{
            
            guardarUsuarioSession(CasoDeUsoType.CONSULTAR, usuario);
            return "editUsuario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Usuario usuario) {
        
        try{
            
            guardarUsuarioSession(CasoDeUsoType.MODIFICAR, usuario);
            return "editUsuario";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public void baja(Usuario usuario) {
        
        try{
        
            usuarioService.eliminarUsuario(usuario.getId());
            listarUsuarios();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public UsuarioServiceBean getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioServiceBean usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Collection<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Collection<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    private void guardarUsuarioSession(CasoDeUsoType casoDeUso, Usuario usuario){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("USUARIO", usuario);  
    }
    
}