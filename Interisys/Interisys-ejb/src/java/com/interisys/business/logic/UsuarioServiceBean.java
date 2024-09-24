/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.persistence.DAOUsuarioBean;
import com.interisys.business.domain.entity.Usuario;
import com.interisys.business.persistence.ErrorDAOException;
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
    private @EJB PerfilServiceBean perfilService;
    
    public void validar(String nombre, String apellido, String correoElectronico, String telefono, String cuenta, String clave, String claveVerificacion)throws ErrorServiceException {
        
        try{
            
            if (nombre == null || nombre.isEmpty()){
               throw new ErrorServiceException("Debe indicar el nombre"); 
            }
            
            if (apellido == null || apellido.isEmpty()){
               throw new ErrorServiceException("Debe indicar el apellido"); 
            }

            if (correoElectronico == null || correoElectronico.isEmpty()){
               throw new ErrorServiceException("Debe indicar el correo electrónico"); 
            }
            
            if (telefono == null || telefono.isEmpty()){
               throw new ErrorServiceException("Debe indicar el teléfono"); 
            }
            
            if (!clave.equals(claveVerificacion)){
               throw new ErrorServiceException("La clave y su verificación no son iguales");  
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public Usuario crearUsuario(String nombre, String apellido, String correoElectronico, String telefono, String cuenta, String clave, String claveVerificacion)throws ErrorServiceException{
        
        try{
            
            validar(nombre, apellido, correoElectronico, telefono, cuenta, clave, claveVerificacion);
            
            try{
                dao.buscarUsuarioPorCuenta(cuenta);
                throw new ErrorServiceException("Ya existe un usuario con la cuenta indicada");
            } catch (NoResultDAOException ex) {}
            
            Usuario usuario = new Usuario();
            usuario.setId(UUID.randomUUID().toString());
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setTelefono(telefono);
            usuario.setUsuario(cuenta);
            usuario.setClave(clave);
            usuario.setEliminado(false);
            
            dao.guardarUsuario(usuario);
            
            return usuario;
            
        }catch(ErrorServiceException e){
            throw e;
        }catch(Exception e){
          e.printStackTrace();
          throw new ErrorServiceException("Error de sistema");  
        }
    }
    
    public void modificarUsuario(String idUsuario, String nombre, String apellido, String correoElectronico, String telefono, String cuenta, String clave, String claveVerificacion)throws ErrorServiceException{
        
        try{
            
            Usuario usuario = dao.buscarUsuario(idUsuario);

            validar(nombre, apellido, correoElectronico, telefono, cuenta, clave, claveVerificacion);
            
            try{
                dao.buscarUsuarioPorCuenta(cuenta);
                throw new ErrorServiceException("Ya existe un usuario con la cuenta indicada");
            } catch (NoResultDAOException ex) {}
            
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setTelefono(telefono);
            usuario.setUsuario(cuenta);
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
            
            if (id == null || id.trim().isEmpty()) {
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
    
    public Usuario buscarUsuarioPorCuenta(String cuenta) throws ErrorServiceException, NoResultDAOException {

        try {
            
            if (cuenta == null || cuenta.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la cuenta");
            }

            Usuario usuario = dao.buscarUsuarioPorCuenta(cuenta);
            
            if (usuario.isEliminado()){
                throw new NoResultDAOException("No se encuentra el usuario indicado");
            }

            return usuario;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        }catch (NoResultDAOException | ErrorDAOException ex){
            throw new NoResultDAOException("No se encontro el usuario indicado");
        }catch (Exception ex) {
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
    
    public void asignarPerfil(String idUsuario, String idPerfil)throws ErrorServiceException{
        
        try{
            
            Usuario usuario = buscarUsuario(idUsuario);
            Perfil perfil = perfilService.buscarPerfil(idPerfil);
            
            usuario.setPerfil(perfil);
            dao.actualizarUsuario(usuario);
            
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
}
