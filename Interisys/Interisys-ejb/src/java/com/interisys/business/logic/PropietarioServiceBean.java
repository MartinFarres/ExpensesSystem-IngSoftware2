/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.persistence.DAOPropietarioBean;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author aidal
 */
@Stateless
@LocalBean
public class PropietarioServiceBean {

    private @EJB DAOPropietarioBean dao;
    
    public void validar(String nombre, String apellido, String correoElectronico, String telefono, Direccion direccion)throws ErrorServiceException {
        
        try{
            
            if (nombre == null || nombre.isEmpty()){
               throw new ErrorServiceException("Debe indicar el nombre"); 
            }
            
            if (apellido == null || apellido.isEmpty()){
               throw new ErrorServiceException("Debe indicar el apellido"); 
            }
            
            if (telefono == null || telefono.isEmpty()){
               throw new ErrorServiceException("Debe indicar el teléfono"); 
            }
            
            if (correoElectronico == null || correoElectronico.isEmpty()){
               throw new ErrorServiceException("Debe indicar el tipo");   
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void crearPropietario (String nombre, String apellido, String correoElectronico, String telefono, boolean habitaConsorcio, Direccion direccion)throws ErrorServiceException {
       
        try{
            
            validar(nombre, apellido, correoElectronico, telefono, direccion);
            
            try{
               dao.buscarPropietarioPorNombreApellido(nombre, apellido);
               throw new ErrorServiceException("Existe un propietario con el nombre y apellido indicado");   
            }catch(NoResultDAOException e){}
            
            if (habitaConsorcio == false && direccion == null){
                throw new ErrorServiceException("Debe indicar la dirección personal, el propietario no habita el consorcio");   
            }
            
            Propietario propietario = new Propietario();
            propietario.setId(UUID.randomUUID().toString());
            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            propietario.setDireccion(habitaConsorcio ? null : direccion);
            propietario.setEliminado(false);
            
            dao.guardarPropietario(propietario);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistema " + ex.toString());
        }  
    }
    
    public void modificarPropietario (String idPropietario, String nombre, String apellido, String correoElectronico, String telefono, boolean habitaConsorcio, Direccion direccion)throws ErrorServiceException {
       
        try{
            
            Propietario propietario = buscarPropietario(idPropietario);
            
            validar(nombre, apellido, correoElectronico, telefono, direccion);
            
            try{
               Propietario propietarioAux = dao.buscarPropietarioPorNombreApellido(nombre, apellido);
               if (!propietarioAux.getId().equals(idPropietario)){
                 throw new ErrorServiceException("Existe un propietario con el nombre y apellido indicado"); 
               }  
            }catch(NoResultDAOException e){}
            
            if (habitaConsorcio == false && direccion == null){
                throw new ErrorServiceException("Debe indicar la dirección personal, el propietario no habita el consorcio");   
            }
            
            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            propietario.setDireccion(habitaConsorcio ? null : direccion);
            
            dao.actualizarPropietario(propietario);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistema " + ex.toString());
        }  
    }
    
    public void eliminarPropietario(String idInmueble) throws ErrorServiceException {

        try {

            Propietario propietario = buscarPropietario(idInmueble);
            propietario.setEliminado(true);
            dao.actualizarPropietario(propietario);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema " + ex.toString());
        }
    }
    
    public Propietario buscarPropietario(String idPropietario) throws ErrorServiceException {

        try {
            
            if (idPropietario == null || idPropietario.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el propietario");
            }

            Propietario propietario = dao.buscarPropietario(idPropietario);
            
            if (propietario.isEliminado()){
                throw new ErrorServiceException("No se encuentra el propietario indicado");
            }

            return propietario;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema " + ex.toString());
        }
    }
    
    public Collection<Propietario> listarPropietarioActivo()throws ErrorServiceException {
        
        try{
            
            return dao.listarPropietarioActivo();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema " + ex.toString());
        }
    }
}
