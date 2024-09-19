/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Recibo;
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
public class DAOReciboBean {

    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarRecibo(Recibo recibo)throws ErrorDAOException{
      try{   
       em.persist(recibo);
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al guardar Recibo " + ex.toString());
      } 
   }
   
   public void actualizarRecibo(Recibo recibo)throws ErrorDAOException{
     
      try{  
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(recibo);
       em.flush();
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al actualizar Recibo " + ex.toString());
      }
   }
   
   public Recibo buscarRecibo(String id) throws NoResultException{
       return em.find(Recibo.class, id);
   }
   
   public Collection<Recibo> listarReciboActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT e "
                           + "FROM Recibo r"
                           + "WHERE r.eliminado = FALSE"
                           + "ORDER BY r.fechaPago DESC").
                           getResultList();
       
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al listar recibos activos " + ex.toString());
      } 
   }
   
}
