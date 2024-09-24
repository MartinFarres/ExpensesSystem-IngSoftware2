/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Inquilino.Sexo;
import com.interisys.business.domain.entity.Inquilino.TipoDocumento;
import com.interisys.business.domain.entity.Nacionalidad;
import com.interisys.business.persistence.DAOInquilinoBean;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.util.UtilFechaBean;
import java.util.Collection;
import java.util.Date;
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
public class InquilinoServiceBean {

    private @EJB NacionalidadServiceBean nacionalidadService;
    private @EJB DAOInquilinoBean dao;
    
    public void validar(String nombre, String apellido, String telefono, String correoElectronico, Date fechaNacimiento, Sexo sexo, TipoDocumento tipoDocumento, String documento)throws ErrorServiceException {
        
        try{
            
           if (nombre == null || nombre.isEmpty()){
               throw new ErrorServiceException("Debe indicar el nombre"); 
            }
            
            if (apellido == null || apellido.isEmpty()){
               throw new ErrorServiceException("Debe indicar el apellido"); 
            }
            
            if (fechaNacimiento == null){
               throw new ErrorServiceException("Debe indicar la fecha de nacimiento"); 
            }
            
            if (UtilFechaBean.edad(fechaNacimiento) < 18){
               throw new ErrorServiceException("La edad del inquilino debe ser mayor o igual a 18 años");  
            }
            
            if (sexo == null){
               throw new ErrorServiceException("Debe indicar el sexo");  
            }
            
            if (telefono == null || telefono.isEmpty()){
               throw new ErrorServiceException("Debe indicar el teléfono"); 
            }
            
            if (tipoDocumento == null){
               throw new ErrorServiceException("Debe indicar el tipo de documento"); 
            }
            
            if (documento == null || documento.isEmpty()){
               throw new ErrorServiceException("Debe indicar el documento"); 
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
    
    public void crearInquilino (String nombre, String apellido, String telefono, String correoElectronico, Date fechaNacimiento, Sexo sexo, TipoDocumento tipoDocumento, String documento, String idNacionalidad)throws ErrorServiceException {
       
        try{
            
            Nacionalidad nacionalidad = nacionalidadService.buscarNacionalidad(idNacionalidad);
            
            validar(nombre, apellido,  telefono,  correoElectronico,  fechaNacimiento, sexo, tipoDocumento, documento);
            
            try{
               dao.buscarInquilinoPorDocumento(documento);
               throw new ErrorServiceException("Existe un inquilino con el documento indicado");   
            }catch(NoResultDAOException e){}
            
            Inquilino inquilino = new Inquilino();
            inquilino.setId(UUID.randomUUID().toString());
            inquilino.setNombre(nombre);
            inquilino.setApellido(apellido);
            inquilino.setTelefono(telefono);
            inquilino.setSexo(sexo);
            inquilino.setTipoDocumento(tipoDocumento);
            inquilino.setDocumento(documento);
            inquilino.setCorreoElectronico(correoElectronico);
            inquilino.setNacionalidad(nacionalidad);
            inquilino.setEliminado(false);
            
            dao.guardarInquilino(inquilino);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }  
    }
    
    public void modificarInquilino (String idInquilino, String nombre, String apellido, String telefono, String correoElectronico, Date fechaNacimiento, Sexo sexo, TipoDocumento tipoDocumento, String documento, String idNacionalidad)throws ErrorServiceException {
       
        try{
            
            Inquilino inquilino = buscarInquilino(idInquilino);
            
            Nacionalidad nacionalidad = nacionalidadService.buscarNacionalidad(idNacionalidad);
            
            validar(nombre, apellido,  telefono,  correoElectronico,  fechaNacimiento, sexo, tipoDocumento, documento);
            
            try{
               Inquilino inquilinoAux = dao.buscarInquilinoPorDocumento(documento);
               if (!inquilinoAux.getId().equals(idInquilino)){
                 throw new ErrorServiceException("Existe un inquilino con el documento indicado");
               }  
            }catch(NoResultDAOException e){}
            
            inquilino.setNombre(nombre);
            inquilino.setApellido(apellido);
            inquilino.setTelefono(telefono);
            inquilino.setSexo(sexo);
            inquilino.setTipoDocumento(tipoDocumento);
            inquilino.setDocumento(documento);
            inquilino.setCorreoElectronico(correoElectronico);
            inquilino.setNacionalidad(nacionalidad);
            
            dao.actualizarInquilino(inquilino);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }  
    }
    
    public void eliminarInquilino(String idInmueble) throws ErrorServiceException {

        try {

            Inquilino inquilino = buscarInquilino(idInmueble);
            inquilino.setEliminado(true);
            dao.actualizarInquilino(inquilino);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Inquilino buscarInquilino(String idInquilino) throws ErrorServiceException {

        try {
            
            if (idInquilino == null || idInquilino.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el inquilino");
            }

            Inquilino inquilino = dao.buscarInquilino(idInquilino);
            
            if (inquilino.isEliminado()){
                throw new ErrorServiceException("No se encuentra el inquilino indicado");
            }

            return inquilino;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Inquilino> listarInquilinoActivo()throws ErrorServiceException {
        
        try{
            
            return dao.listarInquilinoActivo();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
