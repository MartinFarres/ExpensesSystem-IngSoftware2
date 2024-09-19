/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.persistence.DAOExpensaBean;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author franc
 */

@Stateless
@LocalBean
public class ExpensaServiceBean {
    private DAOExpensaBean dao;

    public void crearExpensa(Date fechaDesde, double importeExpesan)throws ErrorServiceException {
        
        try{
            
            if (fechaDesde == null){
               throw new ErrorServiceException("Debe indicar la fecha inicio de vigencia"); 
            }
            
            if (importeExpesan  <= 0.0){
                throw new ErrorServiceException("El importe de la expensa debe ser mayor a cero"); 
            }
            
            Expensa expensa = new Expensa();
            expensa.setId(UUID.randomUUID().toString());
            expensa.setFechaDesde(fechaDesde);
            expensa.setImporte(importeExpesan);
            expensa.setEliminado(false);
            
            dao.guardarExpensa(expensa);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }
    
    public void modificarExpensa(String idExpensa, Date fechaDesde, Date fechaHasta, double importeExpesan)throws ErrorServiceException {
        
        try{
            
            Expensa expensa = dao.buscarExpensa(idExpensa);
           
            if (fechaDesde == null){
               throw new ErrorServiceException("Debe indicar la fecha inicio de vigencia"); 
            }
            
            if (fechaDesde == null){
               throw new ErrorServiceException("Debe indicar la fecha fin de vigencia"); 
            }
            
            if (importeExpesan  <= 0.0){
                throw new ErrorServiceException("El importe de la expensa debe ser mayor a cero"); 
            }
            
            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(fechaHasta);
            expensa.setImporte(importeExpesan);
            expensa.setEliminado(false);
            
            dao.guardarExpensa(expensa);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }
    
    public void eliminarExpensa(String idExpensa) throws ErrorServiceException {

        try {
            
            Expensa expensa = buscarExpensa(idExpensa);
            expensa.setEliminado(true);
            dao.actualizarExpensa(expensa);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    public Expensa buscarExpensa(String idExpensa) throws ErrorServiceException {

        try {
            
            if (idExpensa == null || !idExpensa.isEmpty()){
               throw new ErrorServiceException("Debe indicar la expensa");  
            }
            
            Expensa expensa = dao.buscarExpensa(idExpensa);
            
            if (expensa.isEliminado()){
                throw new ErrorServiceException("No se encuentra la expensa indicada");
            }

            return expensa;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }
    
    
    public Collection<Expensa> listarExpensasActivo()throws ErrorServiceException {
        
        try{
            return dao.listarExpensaActivo();
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }   
}
