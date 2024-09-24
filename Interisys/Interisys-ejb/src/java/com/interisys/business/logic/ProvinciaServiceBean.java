/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Pais;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.persistence.DAOProvinciaBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author spaul
 */

@Stateless
@LocalBean
public class ProvinciaServiceBean {
    
    private @EJB DAOProvinciaBean dao;
    private @EJB PaisServiceBean paisService;
    
    public void crearProvincia(String idPais, String nombre) throws ErrorServiceException {

        try {
            
            Pais pais = paisService.buscarPais(idPais);
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarProvinciaPorPaisYNombre(idPais, nombre);
                throw new ErrorServiceException("Existe una provincia con el nombre para el país indicado");
            } catch (NoResultDAOException ex) {}
            
            Provincia provincia = new Provincia();
            provincia.setId(UUID.randomUUID().toString());
            provincia.setPais(pais);
            provincia.setNombre(nombre);
            provincia.setEliminado(false);
            
            dao.guardarProvincia(provincia);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void modificarProvincia(String idProvincia, String idPais, String nombre) throws ErrorServiceException {

        try {
            
            Pais pais = paisService.buscarPais(idPais);
            Provincia provincia = buscarProvincia(idProvincia);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                
                Provincia provinciaExistente = dao.buscarProvinciaPorPaisYNombre(idPais, nombre);
                if (!provinciaExistente.getId().equals(idProvincia)){
                 throw new ErrorServiceException("Existe una provincia con el nombre para el país indicado");
                } 
            } catch (NoResultDAOException ex) {}

            provincia.setPais(pais);
            provincia.setNombre(nombre);

            dao.guardarProvincia(provincia);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public Provincia buscarProvincia(String id) throws ErrorServiceException {

        try {
            
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la provincia");
            }

            Provincia provincia = dao.buscarProvincia(id);
            
            if (provincia.isEliminado()){
                throw new ErrorServiceException("No se encuentra la provincia indicada");
            }

            return provincia;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarProvincia(String id) throws ErrorServiceException {

        try {

            Provincia provincia = buscarProvincia(id);
            provincia.setEliminado(true);
            
            dao.actualizarProvincia(provincia);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Collection<Provincia> listarProvinciaActiva() throws ErrorServiceException {
        try {
            
            return dao.listarProvinciaActiva();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<Provincia> listarProvinciaActiva(String idPais) throws ErrorServiceException {
        try {
            
            if (idPais == null || idPais.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el país");
            }
            
            return dao.listarProvinciaActiva(idPais);

        } catch (ErrorServiceException ex) {  
            throw ex;    
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
} 

