/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Recibo;
import com.interisys.business.domain.entity.DetalleRecibo;
import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.persistence.DAODetalleReciboBean;
import com.interisys.business.persistence.DAOExpensaInmuebleBean;
import com.interisys.business.persistence.DAOReciboBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
 *
 * @author franc
 */

@Stateless
@LocalBean
public class DetalleReciboServiceBean {
    private DAODetalleReciboBean dao;
    private DAOExpensaInmuebleBean daoExpensaInmueble;
    private DAOReciboBean daoRecibo;

    public void crearDetalleRecibo(int cantidad, double subtotal, String idExpensaInmueble, String idRecibo)throws ErrorServiceException {
        
        try{
            // Valida los paramtros
            if (cantidad <= 0)
               throw new ErrorServiceException("Debe indicar una cantidad positiva"); 
            
            if (subtotal <= 0)
                throw new ErrorServiceException("Debe indicar una subtotal positivo"); 

            if (idExpensaInmueble == null || idExpensaInmueble.isEmpty())
                throw new ErrorServiceException("Debe indicar la expensa del inmueble"); 

            if (idRecibo == null || idRecibo.isEmpty())
                throw new ErrorServiceException("Debe indicar el recibo"); 
            
            // Busca las entidades
            ExpensaInmueble expensaInmueble = null;
            try
            {            
                expensaInmueble = daoExpensaInmueble.buscarExpensaInmueble(idExpensaInmueble);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar la expensa de inmueble de ID: " + idExpensaInmueble); 
            }

            Recibo recibo = null;
            try
            {            
                recibo = daoRecibo.buscarRecibo(idRecibo);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar el recibo de ID: " + idRecibo); 
            }
            
            // Setea los atributos
            DetalleRecibo detalle = new DetalleRecibo();
            detalle.setId(UUID.randomUUID().toString());
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            detalle.setExpensaInmueble(expensaInmueble);
            detalle.setRecibo(recibo);
            recibo.setEliminado(false);
            
            dao.guardarDetalleRecibo(detalle);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }
    
    public void modificarDetalleRecibo(String idDetalle, int cantidad, double subtotal, String idExpensaInmueble, String idRecibo)throws ErrorServiceException {
        
        try{
            
           
            if (cantidad <= 0)
               throw new ErrorServiceException("Debe indicar una cantidad positiva"); 
            
            if (subtotal <= 0)
                throw new ErrorServiceException("Debe indicar una subtotal positivo"); 

            if (idExpensaInmueble == null || idExpensaInmueble.isEmpty())
                throw new ErrorServiceException("Debe indicar la expensa del inmueble"); 

            if (idRecibo == null || idRecibo.isEmpty())
                throw new ErrorServiceException("Debe indicar el recibo"); 
            
            if (idDetalle == null || idDetalle.isEmpty())
                throw new ErrorServiceException("Debe indicar el detalle"); 
               
            
            // Busca el detalle
            DetalleRecibo detalle = null;
            try
            {            
                detalle = dao.buscarDetalleRecibo(idDetalle);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar el detalle de ID: " + idDetalle); 
            }
            
            
            // Busca la expensa del inmueble
            ExpensaInmueble expensaInmueble = null;
            try
            {            
                expensaInmueble = daoExpensaInmueble.buscarExpensaInmueble(idExpensaInmueble);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar la expensa de inmueble de ID: " + idExpensaInmueble); 
            }

            
            // Busca el recibo
            Recibo recibo = null;
            try
            {            
                recibo = daoRecibo.buscarRecibo(idRecibo);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar el recibo de ID: " + idRecibo); 
            }
            
            // Modifica los atributos del objeto y persiste
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            detalle.setExpensaInmueble(expensaInmueble);
            detalle.setRecibo(recibo);
            recibo.setEliminado(false);
            dao.actualizarDetalleRecibo(detalle);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }
    
    public void eliminarDetalleRecibo(String idDetalle) throws ErrorServiceException {

        try {
            
            DetalleRecibo detalle = buscarDetalleRecibo(idDetalle);
            detalle.setEliminado(true);
            dao.actualizarDetalleRecibo(detalle);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    public DetalleRecibo buscarDetalleRecibo(String idDetalle) throws ErrorServiceException {

        try {
            
            if (idDetalle == null || idDetalle.isEmpty()){
               throw new ErrorServiceException("Debe indicar el recibo");  
            }
            
            DetalleRecibo detalle = dao.buscarDetalleRecibo(idDetalle);
            
            if (detalle.isEliminado()){
                throw new ErrorServiceException("No se encuentra el recibo indicado");
            }

            return detalle;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    
    public Collection<DetalleRecibo> listarDetalleReciboActivo(String idRecibo)throws ErrorServiceException {
        
        try{
            // Busca el recibo
            Recibo recibo = null;
            try
            {            
                recibo = daoRecibo.buscarRecibo(idRecibo);
            }catch(NoResultException ex){
                throw new ErrorServiceException("No se pudo encontrar el recibo de ID: " + idRecibo); 
            }
            
            // Busca los detalles de ese recibo
            return dao.listarDetalleReciboActivo(recibo);
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }   
}
