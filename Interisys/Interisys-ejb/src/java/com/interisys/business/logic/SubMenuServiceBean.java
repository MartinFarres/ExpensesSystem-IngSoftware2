/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Menu;
import com.interisys.business.domain.entity.SubMenu;
import com.interisys.business.persistence.DAOMenuBean;
import com.interisys.business.persistence.DAOSubMenuBean;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class SubMenuServiceBean {
    
    private @EJB DAOSubMenuBean dao;
    private @EJB MenuServiceBean  menuService;
    
    public void validar(Menu menu, String nombre, String url, int orden)throws ErrorServiceException {
        
        try{
            
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del sub menú");
            }
           
            if (url == null || url.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la url");
            }

            if (orden <= 0) {
                throw new ErrorServiceException("Debe indicar el orden");
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void crearSubMenu(String idMenu, String nombre, String url, int orden) throws ErrorServiceException {

        try {

            Menu menu = menuService.buscarMenu(idMenu);
            
            validar(menu, nombre, url, orden);

            try {
                dao.buscarSubMenuPorNombre(nombre);
                throw new ErrorServiceException("Existe un sub menú con el nombre indicado");
            } catch (NoResultDAOException ex) {}

            try {
                dao.buscarSubMenuPorMenuYOrden(menu.getId(), orden);
                throw new ErrorServiceException("Existe un sub menú para el menú y orden indicado");
            } catch (NoResultDAOException nrx) {}
            
            //Se crea el submenu
            SubMenu subMenu = new SubMenu();
            subMenu.setId(UUID.randomUUID().toString());
            subMenu.setNombre(nombre);
            subMenu.setUrl(url);
            subMenu.setOrden(orden);

            //Se agrega el submenu a la lista existente
            Collection<SubMenu> subMenus = new ArrayList<SubMenu>();
            if (menu.getSubmenu() != null) {
                subMenus.addAll(menu.getSubmenu());
            }
            subMenus.add(subMenu);

            menuService.modificarMenu(menu.getId(), subMenus);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void modificarSubMenu(String idMenu, String idSubMenu, String nombre, String url,  int orden) throws ErrorServiceException {

        try {

            Menu menu = menuService.buscarMenu(idMenu);

            SubMenu subMenu = buscarSubMenu(idSubMenu);
            
            validar(menu, nombre, url, orden);
            
            try {
                SubMenu subMenuAux = dao.buscarSubMenuPorOrden(subMenu.getId(), nombre);
                if (!subMenuAux.getId().equals(subMenu.getId())){
                   throw new ErrorServiceException("Existe un sub menu para el menú y nombre indicado");
                }
            } catch (NoResultDAOException ex) {}
            
            try {
                SubMenu subMenuAux = dao.buscarSubMenuPorMenuYOrden(menu.getId(), orden);
                if (!subMenuAux.getId().equals(subMenu.getId())){
                  throw new ErrorServiceException("Existe un sub menu para el menú y nombre indicado");
                } 
            } catch (NoResultDAOException nrx) {}

            subMenu.setNombre(nombre);
            subMenu.setUrl(url);
            subMenu.setOrden(orden);
            
            dao.actualizarSubMenu(subMenu);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarSubMenu(String idSubMenu) throws ErrorServiceException {

        try {

            SubMenu sub = buscarSubMenu(idSubMenu);
            sub.setEliminado(true);
            dao.actualizarSubMenu(sub);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public SubMenu buscarSubMenu(String id) throws ErrorServiceException {

        try {

            if (id == null || id.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar un sub menú");
            }

            SubMenu sub= dao.buscarSubMenu(id);
            
            if (sub.isEliminado()){
               throw new ErrorServiceException("No se encuentra el sub menú indicado"); 
            }
            
            return sub;

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public SubMenu buscarSubMenuPorNombre(String nombre) throws ErrorServiceException{

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            return dao.buscarSubMenuPorNombre(nombre);
            
        } catch(ErrorServiceException e){
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException(ex.getMessage());
        }
    }

    public Collection<SubMenu> listarSubMenuActivo() throws ErrorServiceException {

        try {
            
            return dao.listarSubMenuActivo();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Collection<SubMenu> listarSubMenu(String idMenu) throws ErrorServiceException {

        try {
            
            if (idMenu == null || idMenu.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el menú");
            }
            
            return dao.listarSubMenu(idMenu);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

}
  
