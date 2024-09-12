/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Pais;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.persistence.pais.DAOPaisBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author franc
 */
@Stateless
@LocalBean
public class PaisServiceBean {
    
    private @EJB DAOPaisBean dao;
    
    
    public void crearPais(String nombre) throws ErrorServiceException {

        try {
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarPaisPorNombre(nombre);
                throw new ErrorServiceException("Existe un país con el nombre indicado");
            } catch (NoResultDAOException ex) {}

            Pais pais = new Pais();
            pais.setId(UUID.randomUUID().toString());
            pais.setNombre(nombre);
            pais.setEliminado(false);

            dao.guardarPais(pais);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void modificarPais(String idPais, String nombre) throws ErrorServiceException {

        try {

            Pais pais = buscarPais(idPais);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try{
                Pais paisExsitente = dao.buscarPaisPorNombre(nombre);
                if (!paisExsitente.getId().equals(idPais)){
                  throw new ErrorServiceException("Existe un país con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}

            pais.setNombre(nombre);
            pais.setEliminado(false);
            
            dao.actualizarPais(pais);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public Pais buscarPais(String id) throws ErrorServiceException {

        try {
            
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el país");
            }

            Pais pais = dao.buscarPais(id);
            
            if (pais.isEliminado()){
                throw new ErrorServiceException("No se encuentra en país indicado");
            }

            return pais;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarPais(String id) throws ErrorServiceException {

        try {

            Pais pais = buscarPais(id);
            pais.setEliminado(true);
            
            dao.actualizarPais(pais);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    public Collection<Pais> listarPaisActivo() throws ErrorServiceException {
        try {
            
            return dao.listarPaisActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
