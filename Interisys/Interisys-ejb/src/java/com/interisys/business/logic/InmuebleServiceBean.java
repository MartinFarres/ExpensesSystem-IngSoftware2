/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.domain.enumeration.EstadoInmueble;
import com.interisys.business.persistence.DAOInmuebleBean;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author franc
 */

@Stateless
@LocalBean
public class InmuebleServiceBean {
    private @EJB DAOInmuebleBean dao;
    private @EJB PropietarioServiceBean propietarioService;
    private @EJB InquilinoServiceBean inquilinoService;
    public void crearInmueble(String idPropietario, String idInquilino, String piso, String puerta)throws ErrorServiceException {
        
        try{
            
            if (idPropietario == null || idPropietario.isEmpty()){
               throw new ErrorServiceException("Debe indicar el inmueble");  
            }
            
            if (idInquilino == null || idInquilino.isEmpty()){
               throw new ErrorServiceException("Debe indicar el piso");  
            }
                       
            if (piso == null || piso.trim().isEmpty()){
               throw new ErrorServiceException("Debe indicar el piso");  
            }
            
            if (puerta == null || puerta.trim().isEmpty()){
               throw new ErrorServiceException("Debe indicar la puerta");  
            }
            
            try {
                dao.buscarInmueblePorPisoYPuerta(piso, puerta);
                throw new ErrorServiceException("Existe un inmueble con el piso y puerta indicado");
            } catch (NoResultDAOException ex) {}
            
            Propietario propietario = propietarioService.buscarPropietario(idPropietario);
            Inquilino inquilino = null;
            try
            {
                inquilino = inquilinoService.buscarInquilino(idInquilino);
            } catch(ErrorServiceException ex)
            {}
            
            Inmueble inmueble = new Inmueble();
            inmueble.setId(UUID.randomUUID().toString());
            inmueble.setInquilino(inquilino);
            inmueble.setPropietario(propietario);
            inmueble.setEstado((inquilino == null ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO) : EstadoInmueble.HABITADO));
            inmueble.setEstado(EstadoInmueble.DESOCUPADO);
            inmueble.setPiso(piso);
            inmueble.setPuerta(puerta);
            inmueble.setEliminado(false);
            dao.guardarInmueble(inmueble);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas al crear inmueble: " + ex.toString());
        }
    }

    public void modificarInmueble(String idInmueble, String idPropietario, String idInquilino, String piso, String puerta)throws ErrorServiceException {
        
        try{
            
            Inmueble inmueble = buscarInmueble(idInmueble);
            
            Propietario propietario = propietarioService.buscarPropietario(idPropietario);
            Inquilino inquilino =null;
            if (idInquilino != null && !idInquilino.trim().isEmpty()){
             inquilino = inquilinoService.buscarInquilino(idInquilino);
            } 
            
            if (piso == null || piso.trim().isEmpty()){
               throw new ErrorServiceException("Debe indicar el piso");  
            }
            
            if (puerta == null || puerta.trim().isEmpty()){
               throw new ErrorServiceException("Debe indicar la puerta");  
            }
            
            try {
                Inmueble inmuebleAux = dao.buscarInmueblePorPisoYPuerta(piso, puerta);
                if(!inmuebleAux.getId().equals(idInmueble)){
                 throw new ErrorServiceException("Existe un inmueble con el piso y puerta indicado");
                } 
            } catch (NoResultDAOException ex) {}
            
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setEstado((inquilino == null ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO) : EstadoInmueble.HABITADO));
            inmueble.setEstado(EstadoInmueble.HABITADO);
            inmueble.setPiso(piso);
            inmueble.setPuerta(puerta);
            
            dao.actualizarInmueble(inmueble);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas al modificar inmueble: " + ex.toString());
        }
    }
    
    public void eliminarInmueble(String idInmueble) throws ErrorServiceException {

        try {

            Inmueble inmueble = buscarInmueble(idInmueble);
            inmueble.setEliminado(true);
            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas al eliminar inmueble: " + ex.toString());
        }
    }
    
    public Inmueble buscarInmueble(String idInmueble) throws ErrorServiceException {

        try {
            
            if (idInmueble == null || idInmueble.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el inmubele");
            }

            Inmueble inmueble = dao.buscarInmueble(idInmueble);
            
            if (inmueble.isEliminado()){
                throw new ErrorServiceException("No se encuentra el inmueble indicado");
            }

            return inmueble;
            
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas al buscar inmueble: " + ex.toString());
        }
    }
    
    public Collection<Inmueble> listarInmuebleActivo() throws ErrorServiceException {
        try {
            return dao.listarInmuebleActivo();
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas al lisar inmuebles activos: " + ex.toString());
        }
    }
}
