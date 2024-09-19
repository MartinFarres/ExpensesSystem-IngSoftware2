/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Direccion;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author spaul
 */
@Stateless
@LocalBean
public class DAODireccionBean {
    
    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
    
    public void guardarDireccion(Direccion direccion)throws ErrorDAOException{
      try{ 
       em.persist(direccion);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
      } 
   }
   
   public void actualizarDireccion(Direccion direccion)throws ErrorDAOException{
       try{
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(direccion);
        em.flush();
       } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Direccion buscarDireccion(String id) throws NoResultException{
       return em.find(Direccion.class, id);
   }
    
}
