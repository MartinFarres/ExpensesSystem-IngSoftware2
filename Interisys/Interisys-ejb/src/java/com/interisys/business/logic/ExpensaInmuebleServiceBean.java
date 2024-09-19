/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.domain.enumeration.EstadoExpensaInmueble;
import com.interisys.business.persistence.DAOExpensaInmuebleBean;
import java.util.Collection;
import java.util.Date;
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
public class ExpensaInmuebleServiceBean {

    private @EJB InmuebleServiceBean inmuebleService;
    private @EJB ExpensaServiceBean expensaService;
    private @EJB DAOExpensaInmuebleBean dao;
    
    public void crearExpensaInmueble(String idExpensa, String idInmueble, Date periodo, Date fechaVencimiento)throws ErrorServiceException {
        
        try{
            
            Expensa expensa = null;
            Inmueble inmueble = null;
            try{
              expensa = expensaService.buscarExpensa(idExpensa);
              inmueble = inmuebleService.buscarInmueble(idInmueble);
              
              if (periodo == null){
                throw new ErrorServiceException("Debe indicar el periodo");  
              }
            
            }catch(ErrorServiceException e){
              throw new ErrorServiceException("Debe indicar el imnuble y la expensa que desea generar");   
            }  
            
            try{
              dao.buscarExpensaInmueble(idExpensa, idInmueble, periodo);   
              throw new ErrorServiceException("Existe una expensa generada para el inmuble y el período indicado");    
            }catch(ErrorServiceException e){}
            
            ExpensaInmueble expensaInmueble = new ExpensaInmueble();
            expensaInmueble.setId(UUID.randomUUID().toString());
            expensaInmueble.setEstado(EstadoExpensaInmueble.PENDIENTE);
            expensaInmueble.setPeriodo(periodo);
            expensaInmueble.setExpensa(expensa);
            expensaInmueble.setInmueble(inmueble);
            expensaInmueble.setEliminado(false);
            
            dao.guardarExpensaInmueble(expensaInmueble);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }
    
    public void modificarExpensaInmueble(String idExpensaInmueble, String idExpensa, String idInmueble, Date periodo, EstadoExpensaInmueble estado) throws ErrorServiceException {
        
        try{

            ExpensaInmueble expensaInmueble = buscarExpensaInmueble(idExpensaInmueble);
            
            Expensa expensa = null;
            Inmueble inmueble = null;
            try{
              expensa = expensaService.buscarExpensa(idExpensa);
              inmueble = inmuebleService.buscarInmueble(idInmueble);
            }catch(ErrorServiceException e){
              throw new ErrorServiceException("Debe indicar el imnuble y la expensa que desea generar");   
            }  
            
            if (periodo == null){
              throw new ErrorServiceException("Debe indicar el periodo");  
            }
            
            if (estado == null){
              throw new ErrorServiceException("Debe indicar el estado");  
            }
            
            try{
              dao.buscarExpensaInmueble(expensa.getId(), inmueble.getId(), periodo);   
              throw new ErrorServiceException("Existe una expensa generada para el inmuble y el período indicado");    
            }catch(ErrorServiceException e){}
            
            expensaInmueble.setEstado(estado);
            expensaInmueble.setPeriodo(periodo);
            expensaInmueble.setExpensa(expensa);
            expensaInmueble.setInmueble(inmueble);
            
            dao.actualizarExpensaInmueble(expensaInmueble);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void eliminarExpensaInmueble(String idExpensaInmueble) throws ErrorServiceException {

        try {
            
            ExpensaInmueble expensaInmueble = buscarExpensaInmueble(idExpensaInmueble);
            expensaInmueble.setEliminado(true);
            dao.actualizarExpensaInmueble(expensaInmueble);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    public ExpensaInmueble buscarExpensaInmueble(String idExpensaInmueble) throws ErrorServiceException {

        try {
            
            if (idExpensaInmueble == null && !idExpensaInmueble.isEmpty()){
               throw new ErrorServiceException("Debe indicar la expensa e inmubeble generado");  
            }
            
            ExpensaInmueble expensaInmueble = dao.buscarExpensaInmueble(idExpensaInmueble);
            
            if (expensaInmueble.isEliminado()){
                throw new ErrorServiceException("No se encuentra la expensa e inmubeble indicada");
            }

            return expensaInmueble;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    public Collection<ExpensaInmueble> listarExpensaInmuebleActivo()throws ErrorServiceException {
        
        try{
            
            return dao.listarExpensaInmuebleActivo();
            
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
}
