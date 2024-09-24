/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.persistence.DAOUsuarioBean;
import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.persistence.NoResultDAOException;
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
public class UsuarioServiceBean {
    
    private @EJB DAOUsuarioBean dao;
    
    public void crearUsuario(String nombre, String apellido, String telefono, String correoElectronico, String usuarioCuenta, String clave, String claveVerificacion) throws ErrorServiceException{
        try{
            
            validateUserInput(nombre, apellido, telefono, correoElectronico, clave, claveVerificacion);
            
            try{
                // Si Existe el Usuario
                dao.buscarUsuarioPorCuenta(usuarioCuenta);
                throw new ErrorServiceException("Ya existe el usuario");
            }catch(ErrorServiceException ex){}
            // -----------------------------------------------------------------
            
            // Creacion de Usuario
            Usuario usuario = new Usuario();
            usuario.setId(UUID.randomUUID().toString());
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setUsuario(usuarioCuenta);
            usuario.setClave(clave);
            usuario.setEliminado(false);
            
            dao.guardarUsuario(usuario);
            
        }catch(ErrorServiceException e){
            throw e;}
        catch(Exception e){
          e.printStackTrace();
          throw new ErrorServiceException("Error de sistema");  
        }
        
    }
    
    public void modificarUsuario(String idUsuario, String nombre,String apellido, String correoElectronico, String telefono, String usuarioCuenta, String clave, String claveVerificacion) throws ErrorServiceException{
        try{
             validateUserInput(nombre, apellido, telefono, correoElectronico, clave, claveVerificacion);
            
            try{
                // Si Existe el Usuario
                dao.buscarUsuarioPorCuenta(usuarioCuenta);
                throw new ErrorServiceException("Ya existe el usuario");
            }catch(ErrorServiceException ex){}
            // -----------------------------------------------------------------
            
            // Actualizacion de Usuario
            Usuario usuario = dao.buscarUsuario(idUsuario);
            
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setTelefono(telefono);
            usuario.setUsuario(usuarioCuenta);
            usuario.setClave(clave);
            usuario.setEliminado(false);
            
            dao.actualizarUsuario(usuario);
            
        }catch(ErrorServiceException e){
            throw e;
        }catch(Exception e){
          e.printStackTrace();
          throw new ErrorServiceException("Error de sistema");  
        }
    }
    public void eliminarUsuario(String idUsuario) throws ErrorServiceException {

        try {
            
            Usuario usuario = buscarUsuario(idUsuario);
            usuario.setEliminado(true);
            dao.actualizarUsuario(usuario);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Usuario buscarUsuario(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            Usuario usuario = dao.buscarUsuario(id);
            
            if (usuario.isEliminado()){
                throw new ErrorServiceException("No se encuentra el usuario indicado");
            }

            return usuario;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public void modificarClave(String idUsuario, String nuevaClave, String verificacionNuevaClave)throws ErrorServiceException {
        
        try{
            
            Usuario usuario = buscarUsuario(idUsuario);
            
            if (nuevaClave == null || nuevaClave.isEmpty()){
               throw new ErrorServiceException("Debe indicar la clave"); 
            }
            
            if (verificacionNuevaClave == null || verificacionNuevaClave.isEmpty()){
               throw new ErrorServiceException("Debe indicar la verificación de la clave"); 
            }
            
            if (!nuevaClave.equals(verificacionNuevaClave)){
               throw new ErrorServiceException("La clave y su verificación no son iguales");  
            }
            
            usuario.setClave(nuevaClave);
            
            dao.actualizarUsuario(usuario);
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Usuario> listarUsuarioActivo()throws ErrorServiceException{
        
        try{
            
            return dao.listarUsuarioActivo();
        
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    private void validateUserInput(String nombre, String apellido, String telefono, String correoElectronico, String clave, String claveVerificacion) throws ErrorServiceException {
    if (nombre == null || nombre.isEmpty()) {
        throw new ErrorServiceException("El Nombre no puede ser vacío");
    }
    if (apellido == null || apellido.isEmpty()) {
        throw new ErrorServiceException("El Apellido no puede ser vacío");
    }
    if (telefono == null || telefono.isEmpty()) {
        throw new ErrorServiceException("El Telefono no puede ser vacío");
    }
    if (correoElectronico == null || correoElectronico.isEmpty()) {
        throw new ErrorServiceException("El Correo Electrónico no puede ser vacío");
    }
    if (!clave.equals(claveVerificacion)) {
        throw new ErrorServiceException("Ambas claves deben coincidir");
    }
}
    
    public Usuario login (String cuenta, String clave)throws ErrorServiceException{
        
        try{
        
            if (cuenta == null || cuenta.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }
            
            if (clave == null || clave.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            Usuario usuario = null;
            try{
                usuario = dao.buscarUsuarioPorCuentaYClave(cuenta, clave);
            }catch(NoResultDAOException e){
                throw new ErrorServiceException("Usuario o Clave incorrecto");
            }
            
            if (usuario.getPerfil() == null){
               throw new ErrorServiceException("El usuario indicado no posee perfil asignado"); 
            }
            
            return usuario;
            
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    
}
