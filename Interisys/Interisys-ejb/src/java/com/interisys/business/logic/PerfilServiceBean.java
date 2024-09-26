/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Perfil;
import com.interisys.business.domain.entity.SubMenu;
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
    
    public void validar(String nombre, String detalle, Collection<SubMenu> submenues)throws ErrorServiceException {
        
        try{
            
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (detalle == null || detalle.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el detalle");
            }

            if (submenues == null || submenues.isEmpty()) {
                throw new ErrorServiceException("Debe indicar los submenues");
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public Perfil crearPerfil(String nombre, String detalle, Collection<SubMenu> submenues) throws ErrorServiceException {
        
        try {
            
            validar(nombre, detalle, submenues);

            try {
               dao.buscarPerfilPorNombre(nombre);
               throw new ErrorServiceException("Existe una perfil con el nombre indicado");
            } catch (NoResultDAOException ex) {}

            Perfil perfil = new Perfil();
            perfil.setId(UUID.randomUUID().toString());
            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            perfil.setSubmenu(submenues);
            perfil.setEliminado(false);

            dao.actualizarPerfil(perfil);
            
            return perfil;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    
    public void modificarPerfil(String idPerfil, String nombre, String detalle, Collection<SubMenu> submenues) throws ErrorServiceException {
        try {

            Perfil perfil = buscarPerfil(idPerfil);
            
            validar(nombre, detalle, submenues);

            try {
               Perfil perfilAux = dao.buscarPerfilPorNombre(nombre);
               if (!perfilAux.getId().equals(idPerfil)){
                throw new ErrorServiceException("Existe una perfil con el nombre indicado");
               } 
            } catch (NoResultDAOException ex) {}

            perfil.setNombre(nombre);
            perfil.setDetalle(detalle);
            perfil.setSubmenu(submenues);
            perfil.setEliminado(false);

            dao.actualizarPerfil(perfil);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public Perfil buscarPerfil(String idPerfil) throws ErrorServiceException {

        try {

            if (idPerfil == null || idPerfil.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar un Perfil");
            }

            Perfil perfil = dao.buscarPerfil(idPerfil);
            
            if (perfil.isEliminado()){
               throw new ErrorServiceException("No existe el perfil indicado"); 
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
}
    

