/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.CuentaCorreo;
import com.interisys.business.persistence.DAOCuentaCorreoBean;
import com.interisys.business.persistence.NoResultDAOException;
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
public class CuentaCorreoServiceBean {
    
    @EJB ConsorcioServiceBean consorcioService;
    @EJB DAOCuentaCorreoBean dao;
    
    public void crearCuentaCorreo(String idConsorcio, String correo, String clave, String smtp, String puerto, boolean tls) throws ErrorServiceException {
       
        try {
            
            Consorcio consorcio = consorcioService.buscarConsorcio(idConsorcio);
            
            if (correo == null || correo.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar el correo");
            }
            if (clave == null || clave.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar la clave");
            }
            if (smtp == null || smtp.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar la dirección SMTP");
                
            }
            if (puerto == null || puerto.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar el puerto");
                
            }
            try {
                
                dao.buscarCuentaCorreoPorCorreo(correo);
                
                throw new ErrorServiceException("Ya existe un cuenta correo con el correo indicado");
                
            } catch (NoResultDAOException ex) {}
            
            CuentaCorreo cuenta = new CuentaCorreo();
            cuenta.setId(UUID.randomUUID().toString());
            cuenta.setConsorcio(consorcio);
            cuenta.setCorreo(correo);
            cuenta.setClave(clave);
            cuenta.setSmtp(smtp);
            cuenta.setPuerto(puerto);
            cuenta.setTls(tls);
            cuenta.setEliminado(false);

            dao.guardarCuentaCorreo(cuenta);

        } catch (ErrorServiceException e) {
            
            throw e;
            
        } catch (Exception ex){
            
            ex.printStackTrace();
            
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    
    public CuentaCorreo buscarCuentaCorreo(String id) throws ErrorServiceException {
        try {
            
            if (id == null) {
                
                throw new ErrorServiceException("Debe indicar la cuenta correo");
            }

            CuentaCorreo correo = dao.buscarCuentaCorreo(id);
            
            if (correo.isEliminado()){
                
                throw new ErrorServiceException("No se encuentra el correo indicada");
            }

            return correo;
            
        } catch (ErrorServiceException ex) { 
            
            throw ex;
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    
    public void eliminarCuentaCorreo(String id) throws ErrorServiceException {

        try {

            CuentaCorreo cuentaCorreo = buscarCuentaCorreo(id);
            cuentaCorreo.setEliminado(true);
            
            dao.actualizarCuentaCorreo(cuentaCorreo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public void modificarCuentaCorreo(String idConsorcio, String idCorreo, String correo, String clave, String smtp, String puerto, boolean tls) throws ErrorServiceException {
        
        try {
            
            CuentaCorreo cuentaCorreo = buscarCuentaCorreo(idCorreo);
            
            Consorcio consorcio = consorcioService.buscarConsorcio(idConsorcio);
            
            
            if (correo == null || correo.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar el correo");
                
            }
            
            if (clave == null || clave.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar la clave");
                
            }
            
            if (smtp == null || smtp.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar la dirección SMTP ");
                
            }
            
            if (puerto == null || puerto.isEmpty()) {
                
                throw new ErrorServiceException("Debe indicar el puerto SMTP ");
                
            }

            try {
                
                dao.buscarCuentaCorreoPorCorreo(correo);
                
                throw new ErrorServiceException("Ya existe un cuenta correo con el correo indicado");
                
            } catch (NoResultDAOException ex) {}


            cuentaCorreo.setConsorcio(consorcio);
            cuentaCorreo.setCorreo(correo);
            cuentaCorreo.setClave(clave);
            cuentaCorreo.setSmtp(smtp);
            cuentaCorreo.setPuerto(puerto);
            cuentaCorreo.setTls(tls);

            dao.guardarCuentaCorreo(cuentaCorreo);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    
    }
    
    public Collection<CuentaCorreo> listarCuentaCorreoActiva()throws ErrorServiceException {
        
        try{
            
            return dao.listarCuentaCorreoActiva();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public CuentaCorreo buscarCuentaCorreoActiva()throws ErrorServiceException {
        
        try{
            
            return dao.buscarCuentaCorreoActiva();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    
    
    
}
