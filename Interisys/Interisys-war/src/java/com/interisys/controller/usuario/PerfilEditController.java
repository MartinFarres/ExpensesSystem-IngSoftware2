package com.interisys.controller.usuario;

import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.domain.entity.SubMenu; // Import SubMenu
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.controller.enumeration.MessageType;
import com.interisys.business.logic.PerfilServiceBean;
import com.interisys.business.logic.SubMenuServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class PerfilEditController {

    //Servicios Capara de Negocio
    private @EJB PerfilServiceBean perfilService;
    private @EJB SubMenuServiceBean submenuService;
    
    //Variables Capa de Negocio
    private Perfil perfil;
    private String nombre;
    private String detalle;

    //Variable Vista
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private TreeNode menues;
    private TreeNode[] menuSeleccionados;

    @PostConstruct
    public void init() {
        try{
            
         //Se obtiene el caso de uso  
         casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");
         perfil = ((Perfil) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PERFIL"));
         campoDesactivado = false;
         
         //Se asiganan la variables en caso de ser caso de uso CONSULTAR y MODIFICAR
         //En caso de CONSULTA se desactivan los campos para que no puedan ser editados.
         if (casoDeUso == CasoDeUsoType.CONSULTAR || casoDeUso == CasoDeUsoType.MODIFICAR) {
            nombre = perfil.getNombre();

            if (casoDeUso == CasoDeUsoType.CONSULTAR) {
                campoDesactivado = true;
            }
        }
         
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String aceptar() {
        
        try{
            
            if (casoDeUso.equals("ALTA")){
              perfilService.crearPerfil(nombre, detalle, obtenerSubMenuDeTreeNode());
            }else if (casoDeUso.equals("MODIFICAR")){
              perfilService.modificarPerfil(perfil.getId(), nombre, detalle, obtenerSubMenuDeTreeNode());
            }
            
            return "listPerfil";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    private Collection<SubMenu> obtenerSubMenuDeTreeNode(){
        
        Collection<SubMenu> submenues = new ArrayList<>();

        for (TreeNode tn : menuSeleccionados) {
            if (tn.getData().getClass().getName().contains("SubMenu")) {
                SubMenu sm = new SubMenu();
                try {
                    sm = submenuService.buscarSubMenu(((SubMenu)tn.getData()).getId());
                } catch (ErrorServiceException ex) {
                    Message.show("No se pudo seleccionar los menues indicados", MessageType.ERROR);
                }
                submenues.add(sm);
            }
        }
        
        return submenues;
    }
    
    public String cancelar() {
        return "listPerfil";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public CasoDeUsoType getCasoDeUso() { // Ajuste para usar el tipo enum
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public TreeNode[] getMenuSeleccionados() {
        return menuSeleccionados;
    }

    public void setMenuSeleccionados(TreeNode[] menuSeleccionados) {
        this.menuSeleccionados = menuSeleccionados;
    }

    public TreeNode getMenues() {
        return menues;
    }

    public void setMenues(TreeNode menues) {
        this.menues = menues;
    }
    
    public String getDetalle() {
    return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    
    
}
