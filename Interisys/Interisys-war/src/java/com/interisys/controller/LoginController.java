/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller;

import com.interisys.business.domain.entity.Menu;
import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.InitAplicacionServiceBean;
import com.interisys.business.logic.MenuServiceBean;
import com.interisys.business.logic.PerfilServiceBean;
import com.interisys.business.logic.SubMenuServiceBean;
import com.interisys.business.logic.UsuarioServiceBean;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.controller.enumeration.MessageType;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author martin
 */
@ManagedBean
@ViewScoped
public class LoginController implements Serializable {

    //Servicios Capa de Negocio
    private @EJB UsuarioServiceBean usuarioService;
    private @EJB PerfilServiceBean perfilService;
    private @EJB InitAplicacionServiceBean inicioAplicacionService;
    private @EJB SubMenuServiceBean submenuService;
    private @EJB MenuServiceBean menuService;


    //Variables Capa de Negocio
    private String user;
    private String password;
    private Usuario usuario = null;

    @PostConstruct
    public void init() {}

    /**
     * @return Si el logín es exitoso se retorna el Sting que apunta al archivo
     *         faces-config.xml para redireccionar a la página principal /view/ini.xhtml
     */
    public String login(){

        try {
            
            try{
                usuarioService.buscarUsuarioPorCuenta("admin");
            }catch(NoResultDAOException e){
                //Verifico si no exiten datos par crear los datos por defecto
                //Se creal el usuario administrador  
             Usuario user = usuarioService.crearUsuario("admin", "test", "admin@test.com", "12345678", "admin", "thor", "thor");
             
             //Se crean los menues del sistema
            crearMenuInicial();
             
            //Se crea el perfil con acceso a todos los menú y submenú
            Perfil perfil = perfilService.crearPerfil("admin", "Perfil creado para el usuario administrador", submenuService.listarSubMenuActivo());
             
             //Se le asigna al usuario creado el perfil creado.
             usuarioService.asignarPerfil(user.getId(), perfil.getId());
            }
            //Validamos el usuario y la clave
            usuario = usuarioService.login(user, password);

            
            //Redireccionamos a la págima principal.
            return "index";
                    
        } catch (ErrorServiceException u) {
            u.printStackTrace();
            Message.show(u.getMessage(), MessageType.ERROR);
            RequestContext.getCurrentInstance().update("formLogin:msj");
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            RequestContext.getCurrentInstance().update("formLogin:msj");
        }

        return null;
    }
    
    public void crearMenuInicial() throws ErrorServiceException {
        try {
            // Menú Configuración
            menuService.crearMenu("Configuración", 1);
            Menu menuConfiguracion = menuService.buscarMenuPorNombre("Configuración");
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Menú", "/view/menu/listMenu.jsf", 1);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Sub Menú", "/view/menu/listSubMenu.jsf", 2);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM País", "/view/pais/listPais.jsf", 3);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Provincia", "/view/provincia/listProvincia.jsf", 4);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Departamento", "/view/departamento/listDepartamento.jsf", 5);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Localidad", "/view/localidad/listLocalidad.jsf", 6);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Nacionalidad", "/view/nacionalidad/listNacionalidad.jsf", 7);
            submenuService.crearSubMenu(menuConfiguracion.getId(), "ABM Feriado", "/view/feriado/listFeriado.jsf", 8);

            // Menú Usuario
            menuService.crearMenu("Usuario", 2);
            Menu menuUsuario = menuService.buscarMenuPorNombre("Usuario");
            submenuService.crearSubMenu(menuUsuario.getId(), "ABM Perfil", "/view/perfil/listPerfil.jsf", 1);
            submenuService.crearSubMenu(menuUsuario.getId(), "ABM Usuario", "/view/usuario/listUsuario.jsf", 2);
            submenuService.crearSubMenu(menuUsuario.getId(), "Clave", "/view/usuario/editClave.jsf", 3);

            // Menú Consorcio
            menuService.crearMenu("Consorcio", 3);
            Menu menuConsorcio = menuService.buscarMenuPorNombre("Consorcio");
            submenuService.crearSubMenu(menuConsorcio.getId(), "ABM Consorcio", "/view/consorcio/listConsorcio.jsf", 1);
            submenuService.crearSubMenu(menuConsorcio.getId(), "ABM Cuenta Correo", "/view/correo/listCuentaCorreo.jsf", 2);

            // Menú Propietario
            menuService.crearMenu("Propietario", 4);
            Menu menuPropietario = menuService.buscarMenuPorNombre("Propietario");
            submenuService.crearSubMenu(menuPropietario.getId(), "ABM Propietario", "/view/propietario/listPropietario.jsf", 1);

            // Menú Inquilino
            menuService.crearMenu("Inquilino", 5);
            Menu menuInquilino = menuService.buscarMenuPorNombre("Inquilino");
            submenuService.crearSubMenu(menuInquilino.getId(), "ABM Inquilino", "/view/inquilino/listInquilino.jsf", 1);

            // Menú Inmueble
            menuService.crearMenu("Inmueble", 6);
            Menu menuInmueble = menuService.buscarMenuPorNombre("Inmueble");
            submenuService.crearSubMenu(menuInmueble.getId(), "ABM Inmueble", "/view/inmueble/listInmueble.jsf", 1);

            // Menú Expensa
            menuService.crearMenu("Expensa", 7);
            Menu menuExpensa = menuService.buscarMenuPorNombre("Expensa");
            submenuService.crearSubMenu(menuExpensa.getId(), "ABM Valor Expensa", "/view/expensa/listExpensa.jsf", 1);
            submenuService.crearSubMenu(menuExpensa.getId(), "ABM Expensa / Inmueble", "/view/expensa/listExpensaInmueble.jsf", 2);
            submenuService.crearSubMenu(menuExpensa.getId(), "Gestión Expensa", "/view/expensa/administracionExpensa.jsf", 3);
            submenuService.crearSubMenu(menuExpensa.getId(), "Deuda Expensa", "/view/expensa/listDeudaExpensa.jsf", 4);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;

    }  
}
