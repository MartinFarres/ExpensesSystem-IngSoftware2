/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Expensa;
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
public class DAOExpensaBean {

    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarExpensa(Expensa expensa)throws ErrorDAOException{
      try{   
       em.persist(expensa);
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      } 
   }
   
   public void actualizarExpensa(Expensa expensa)throws ErrorDAOException{
     
      try{  
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(expensa);
       em.flush();
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      }
   }
   
   public Expensa buscarExpensa(String id) throws NoResultException{
       return em.find(Expensa.class, id);
   }
   
   public Collection<Expensa> listarExpensaActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT e "
                           + "FROM Expensa e"
                           + "WHERE e.eliminado = FALSE"
                           + "ORDER BY e.fechaDesde DESC").
                           getResultList();
       
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
      } 
   }
   
}
