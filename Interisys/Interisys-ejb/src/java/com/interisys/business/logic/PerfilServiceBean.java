/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Menu;
import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.domain.entity.SubMenu;
import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.persistence.DAOPerfilBean;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author kalit
 */
@Stateless
@LocalBean
public class PerfilServiceBean {
    
    private @EJB DAOPerfilBean dao;
    
    public void crearPerfil(String nombre, String detalle, Collection<SubMenu> submenues) throws ErrorServiceException{
        try{
            
            validateUserInput(nombre, detalle, submenues);
            
            try{
                // Si Existe el Perfil
                dao.buscarPerfilPorNombre(nombre);
                throw new ErrorServiceException("Ya existe el perfil");
            }catch(ErrorServiceException ex){}
            // -----------------------------------------------------------------
            
            // Creacion de Perfil
            Perfil perfil = new Perfil();
            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            perfil.setEliminado(false);
            perfil.setSubmenu(submenues);
            
            dao.guardarPerfil(perfil);
            
        }catch(ErrorServiceException e){
            throw e;}
        catch(Exception e){
          e.printStackTrace();
          throw new ErrorServiceException("Error de sistema");  
        }
        
    }
    
    public void modificarPerfil(String nombre, String detalle, Collection<SubMenu> submenues) throws ErrorServiceException{
        try{
             validateUserInput(nombre, detalle, submenues);
            
            try{
                // Si Existe el Perfil
                dao.buscarPerfilPorNombre(nombre);
                throw new ErrorServiceException("Ya existe el perfil");
            }catch(ErrorServiceException ex){}
            // -----------------------------------------------------------------
            
            // Actualizacion de Perfil
            Perfil perfil = new Perfil();
            
            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            perfil.setEliminado(false);
            perfil.setSubmenu(submenues);
            
            dao.actualizarPerfil(perfil);
            
        }catch(ErrorServiceException e){
            throw e;
        }catch(Exception e){
          e.printStackTrace();
          throw new ErrorServiceException("Error de sistema");  
        }
    }
    public void eliminarPerfil(String idPerfil) throws ErrorServiceException {

        try {
            
            Perfil perfil = buscarPerfil(idPerfil);
            perfil.setEliminado(true);
            dao.actualizarPerfil(perfil);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Perfil buscarPerfil(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el perfil");
            }

            Perfil perfil = dao.buscarPerfil(id);
            
            if (perfil.isEliminado()){
                throw new ErrorServiceException("No se encuentra el perfil indicado");
            }

            return perfil;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Perfil buscarPerfilPorNombre(String nombre) throws ErrorServiceException {

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            return dao.buscarPerfilPorNombre(nombre);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Perfil> listarPerfilActivo() throws ErrorServiceException {

        try {
            
            return dao.listarPerfilActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    private void validateUserInput(String nombre, String detalle, Collection<SubMenu> submenues) throws ErrorServiceException {
    if (nombre == null || nombre.isEmpty()) {
        throw new ErrorServiceException("El Nombre no puede ser vacío");
    }
    if (detalle == null || detalle.isEmpty()) {
        throw new ErrorServiceException("El Detalle no puede ser vacío");
    }
    if (submenues == null || submenues.isEmpty()) {
        throw new ErrorServiceException("El menu no puede ser vacío");
    }
    }
}
    

