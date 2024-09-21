/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.CuentaCorreo;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import javax.persistence.FlushModeType;

/**
 *
 * @author spaul
 */
@Stateless
@LocalBean
public class DAOCuentaCorreoBean {
    
    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
    
    
    public void guardarCuentaCorreo(CuentaCorreo cuenta)throws ErrorDAOException{
       
     try{   
       em.persist(cuenta);
       
     } catch (Exception ex) {
         
        ex.printStackTrace();
        
        throw new ErrorDAOException("Error de sistema");
     }   
   }
    
    public void actualizarCuentaCorreo(CuentaCorreo cuenta)throws ErrorDAOException{
       
      try{ 
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(cuenta);
       em.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
     }  
   }
    
   public CuentaCorreo buscarCuentaCorreo(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
        return em.find(CuentaCorreo.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
     } 
   }
   
   public CuentaCorreo buscarCuentaCorreoPorCorreo (String correo) throws NoResultDAOException, ErrorDAOException{
       
      try{ 
          
       return (CuentaCorreo) em.createQuery("SELECT p "
                                  + "  FROM CuentaCorreo p"
                                  + " WHERE p.correo = :correo"
                                  + "   AND p.eliminado = FALSE").
                                  setParameter("correo", correo).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
 
   public CuentaCorreo buscarCuentaCorreoActiva () throws NoResultDAOException, ErrorDAOException{
       
      try{ 
          
       return (CuentaCorreo) em.createQuery("SELECT p "
                                  + "  FROM CuentaCorreo p"
                                  + " WHERE p.correo = :correo"
                                  + "   AND p.eliminado = FALSE").
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
   public Collection<CuentaCorreo> listarCuentaCorreoActiva () throws ErrorDAOException{
       
      try{ 
          
       return  em.createQuery("SELECT p "
                            + "  FROM CuentaCorreo p"
                            + " WHERE p.eliminado = FALSE").
                            getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
}
        
