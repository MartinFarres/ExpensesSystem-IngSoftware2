/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.persistence.DAODireccionBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author spaul
 */
@Stateless
@LocalBean
public class DireccionServiceBean {
    
    @EJB LocalidadServiceBean localidadService;
    @EJB DAODireccionBean dao;
    
    public Direccion crearDireccion (String idLocalidad, String calle, String numeracion, String barrio, String pisoCasa, String puertaManzana, String ubicacionCoordenadaX, String ubicacionCoordenadaY, String observacion)throws ErrorServiceException {
        
        try{
            
            Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
            
            if (calle == null || calle.isEmpty()){
               throw new ErrorServiceException("Debe indicar la calle");  
            }
            
            if (numeracion == null || numeracion.isEmpty()){
               throw new ErrorServiceException("Debe indicar la numeración");  
            }
            
            if (barrio == null || barrio.isEmpty()){
               throw new ErrorServiceException("Debe indicar el barrio");  
            }
            
            if (pisoCasa == null || pisoCasa.isEmpty()){
               throw new ErrorServiceException("Debe indicar el piso / casa");  
            }
           
            if (puertaManzana == null || puertaManzana.isEmpty()){
               throw new ErrorServiceException("Debe indicar la puerta / manzana");  
            }
            
            if (ubicacionCoordenadaX == null || ubicacionCoordenadaX.isEmpty()){
               throw new ErrorServiceException("Debe indicar la ubicaciónCoordenadaX");  
            }
            
            if (ubicacionCoordenadaY == null || ubicacionCoordenadaY.isEmpty()){
               throw new ErrorServiceException("Debe indicar la ubicaciónCoordenadaY");  
            }
            
            Direccion direccion = new Direccion();
            direccion.setId(UUID.randomUUID().toString());
            direccion.setLocalidad(localidad);
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setPisoCasa(pisoCasa);
            direccion.setPuertaManzana(puertaManzana);
            direccion.setUbicacionCoordenadaX(ubicacionCoordenadaX);
            direccion.setUbicacionCoordenadaY(ubicacionCoordenadaY);
            direccion.setObservacion(observacion == null ? "": observacion);
            direccion.setEliminado(false);
            
            return direccion;
                    
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public Direccion modificarDireccion (String idDireccion, String idLocalidad, String calle, String numeracion, String barrio, String pisoCasa, String puertaManzana, String ubicacionCoordenadaX, String ubicacionCoordenadaY, String observacion)throws ErrorServiceException {
        
        try{
            
            Direccion direccion = buscarDireccion(idDireccion);
            Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
            
            if (calle == null || calle.isEmpty()){
               throw new ErrorServiceException("Debe indicar la calle");  
            }
            
            if (numeracion == null || numeracion.isEmpty()){
               throw new ErrorServiceException("Debe indicar la numeración");  
            }
            
            if (barrio == null || barrio.isEmpty()){
               throw new ErrorServiceException("Debe indicar el barrio");  
            }
            
            if (pisoCasa == null || pisoCasa.isEmpty()){
               throw new ErrorServiceException("Debe indicar el piso / casa");  
            }
           
            if (puertaManzana == null || puertaManzana.isEmpty()){
               throw new ErrorServiceException("Debe indicar la puerta / manzana");  
            }
            
            if (ubicacionCoordenadaX == null || ubicacionCoordenadaX.isEmpty()){
               throw new ErrorServiceException("Debe indicar la ubicaciónCoordenadaX");  
            }
            
            if (ubicacionCoordenadaY == null || ubicacionCoordenadaY.isEmpty()){
               throw new ErrorServiceException("Debe indicar la ubicaciónCoordenadaY");  
            }
            
            direccion.setLocalidad(localidad);
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setPisoCasa(pisoCasa);
            direccion.setPuertaManzana(puertaManzana);
            direccion.setUbicacionCoordenadaX(ubicacionCoordenadaX);
            direccion.setUbicacionCoordenadaY(ubicacionCoordenadaY);
            direccion.setObservacion(observacion == null ? "": observacion);
            
            return direccion;
                    
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public void eliminarDireccion(String idDireccion) throws ErrorServiceException {

        try {

            Direccion direccion = buscarDireccion(idDireccion);
            direccion.setEliminado(true);
            dao.actualizarDireccion(direccion);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }

    public Direccion buscarDireccion(String idDireccion) throws ErrorServiceException {

        try {
            
            if (idDireccion == null || idDireccion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la dirección");
            }

            Direccion direccion = dao.buscarDireccion(idDireccion);
            
            if (direccion.isEliminado()){
                throw new ErrorServiceException("No se encuentra la dirección indicada");
            }

            return direccion;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public Collection<Direccion> listarDireccionActiva() throws ErrorServiceException {
        try {
            
            return dao.listarDireccionActiva();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public Collection<Direccion> listarDireccionActiva(String idLocalidad) throws ErrorServiceException {
        try {
            
            if (idLocalidad == null || idLocalidad.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el departamento");
            }
            
            return dao.listarDireccionActiva(idLocalidad);

        } catch (ErrorServiceException ex) {  
            throw ex;    
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
}
