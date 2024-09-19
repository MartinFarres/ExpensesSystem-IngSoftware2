/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.domain.entity.ExpensaInmueble;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author franc
 */
@Stateless
@LocalBean
public class DAOExpensaInmuebleBean {

    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarExpensaInmueble(ExpensaInmueble expensaInmueble)throws ErrorDAOException{
      try{   
       em.persist(expensaInmueble);
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      } 
   }
   
   public void actualizarExpensaInmueble(ExpensaInmueble expensaInmueble)throws ErrorDAOException{
     
      try{  
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(expensaInmueble);
       em.flush();
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      }
   }
   
   public ExpensaInmueble buscarExpensaInmueble(String id) throws NoResultException{
       return em.find(ExpensaInmueble.class, id);
   }
   
   public Collection<Expensa> listarExpensaInmuebleActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT e "
                           + "FROM ExpensaInmueble e"
                           + "WHERE e.eliminado = FALSE"
                           + "ORDER BY e.periodo DESC").
                           getResultList();
       
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      } 
   }
      
}
