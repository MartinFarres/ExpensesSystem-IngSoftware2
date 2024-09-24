/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Menu;
import com.interisys.business.domain.entity.SubMenu;
import com.interisys.business.persistence.DAOMenuBean;
import com.interisys.business.persistence.ErrorDAOException;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class MenuServiceBean {
    
    private @EJB DAOMenuBean dao;
    
    public void crearMenu(String nombre, int orden) throws ErrorServiceException, NoResultException, ErrorDAOException{
        try{
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("El Nombre no puede ser vacío");
            }   
            if (orden <= 0) {
                throw new ErrorServiceException("El orden debe de ser mayor que cero");
            }
            
            try{
                dao.buscarMenuPorNombre(nombre);
                throw new ErrorServiceException("Existe un menu con el nombre indicado");
            }catch(NoResultDAOException ex){}
            
            try{
                dao.buscarMenuPorOrden(orden);
                throw new ErrorServiceException("Existe un menu con el orden indicado");
            }catch(NoResultDAOException ex){}
            
            Menu menu = new Menu();
            menu.setId(UUID.randomUUID().toString());
            menu.setNombre(nombre);
            menu.setOrden(orden);
            menu.setEliminado(false);

            dao.guardarMenu(menu);
            
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public void modificarMenu(String idMenu, Collection<SubMenu> subMenus) throws ErrorServiceException {

        try {

            Menu menu = buscarMenu(idMenu);
            
            if (subMenus.isEmpty()){
              throw new ErrorServiceException("Debe indicar los sub menú");   
            }
            
            menu.setSubmenu(subMenus);
            
            dao.actualizarMenu(menu);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public void modificarMenu(String idMenu, String nombre, int orden) throws ErrorServiceException {

        try {

            Menu menu = buscarMenu(idMenu);

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                Menu menuAux = dao.buscarMenuPorNombre(nombre); 
                if (!menuAux.getId().equals(menu.getId())){
                   throw new ErrorServiceException("Existe un menú con el nombre indicado"); 
                }
            } catch (NoResultDAOException ex) {}

            if (orden <= 0) {
                throw new ErrorServiceException("Debe indicar el orden");
            } else {
                try {
                  Menu menuAux = dao.buscarMenuPorOrden(orden);   
                  if (!menuAux.getId().equals(menu.getId())){
                    throw new ErrorServiceException("Existe un menú con el orden indicado");
                  }  
                } catch (NoResultDAOException nrx) {}
            }

            menu.setNombre(nombre);
            menu.setOrden(orden);
            
            dao.actualizarMenu(menu);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarMenu(String idMenu) throws ErrorServiceException {

        try {

            Menu menu = buscarMenu(idMenu);

            if (menu.isEliminado()){
              throw new ErrorServiceException("No se encuentra el menú indicado");  
            }
            
            menu.setEliminado(true);
            
            dao.actualizarMenu(menu);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Menu buscarMenuPorSubMenu(String idSubmenu) throws ErrorServiceException {

        try {

            if (idSubmenu == null || idSubmenu.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el sub menú");
            }

            return dao.buscarMenuPorSubmenu(idSubmenu);

        } catch(ErrorServiceException e){
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException(ex.getMessage());
        }
    }

    public Menu buscarMenu(String id) throws ErrorServiceException {

        try {

            if (id == null || id.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el menú");
            }

            return dao.buscarMenu(id);

        } catch(ErrorServiceException e){
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException(ex.getMessage());
        }
    }

    public Menu buscarMenuPorNombre(String nombre) throws ErrorServiceException{

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            return dao.buscarMenuPorNombre(nombre);
            
        } catch(ErrorServiceException e){
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException(ex.getMessage());
        }
    }
    
    public Collection<Menu> listarMenu() throws ErrorServiceException {

        try {
            
            return dao.listarMenu();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Collection<Menu> listarMenuActivo() throws ErrorServiceException {

        try {
            return dao.listarMenuActivo();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
}
