/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.interisys.business.domain.entity.Perfil;
import com.interisys.controller.enumeration.MessageType;
import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.logic.PerfilServiceBean;
import com.interisys.business.logic.UsuarioServiceBean;
import com.interisys.controller.enumeration.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
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
 * @author martin
 */
@ManagedBean
@ViewScoped
public class UsuarioEditController {

    private @EJB UsuarioServiceBean usuarioService;
    private @EJB PerfilServiceBean perfilService;
    
    private Usuario usuario;
    private Collection<SelectItem> perfiles = new ArrayList();
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private String claveConfirmacion;
    
    @PostConstruct
    public void init() {
        //Se obtiene el caso de uso  
        casoDeUso = (CasoDeUsoType)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
        
        // Cuando el caso de uso es alta, crea la expensa
        if (casoDeUso == CasoDeUsoType.ALTA)
        {            
            usuario = new Usuario();
            usuario.setPerfil(new Perfil());
        }
        else
            // Obtiene la expensa a traves del controlador de sesion
            usuario = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USUARIO");
            this.claveConfirmacion = usuario.getClave();
        
        // Los campos se desactiva en en caso de uso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
        
        cargarComboPerfiles();
         
    }
    
    public String aceptar() {
        try{
            
            // Dependiendo del caso de uso hace distintas acciones
            switch (casoDeUso)
            {

                case ALTA:
                    Usuario usuarioRes = usuarioService.crearUsuario(
                            usuario.getNombre(), 
                            usuario.getApellido(),
                            usuario.getCorreoElectronico(),
                            usuario.getTelefono(),
                            usuario.getUsuario(),
                            usuario.getClave(),
                            claveConfirmacion);
                    usuarioService.asignarPerfil(usuarioRes.getId(), usuario.getPerfil().getId());
                    Message.show("Usuario creado exitosamente", MessageType.NOTIFICACION);
                    break;
                    
                case MODIFICAR:
                    usuarioService.modificarUsuario(
                            usuario.getId(), 
                            usuario.getNombre(), 
                            usuario.getApellido(),
                            usuario.getCorreoElectronico(),
                            usuario.getTelefono(),
                            usuario.getUsuario(),
                            usuario.getClave(),
                            claveConfirmacion);
                    usuarioService.asignarPerfil(usuario.getId(), usuario.getPerfil().getId());
                    Message.show("Usuario modificado exitosamente", MessageType.NOTIFICACION);
                    break;
                
                case CONSULTAR:
                    break;
            }

            return "listUsuarioUsuarios";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String cancelar() {
        return "listUsuarioUsuarios";
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }
    
    public String getClaveConfirmacion() {
        return this.claveConfirmacion;
    }
    
    public void setClaveConfirmacion(String claveConfirmacion) {
        this.claveConfirmacion = claveConfirmacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    
    public Collection<SelectItem> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Collection<SelectItem> perfiles) {
        this.perfiles = perfiles;
    }
    
    public void cargarComboPerfiles() {
        try {

            perfiles = new ArrayList<>();
            perfiles.add(new SelectItem(null, "Seleccione..."));
            for (Perfil perfil : perfilService.listarPerfilActivo()) {
                perfiles.add(new SelectItem(perfil.getId(), perfil.getNombre()));
            }

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
} 