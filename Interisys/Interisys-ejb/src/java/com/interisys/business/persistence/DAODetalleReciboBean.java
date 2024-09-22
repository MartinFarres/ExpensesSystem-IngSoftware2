/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.DetalleRecibo;
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
public class DAODetalleReciboBean {

    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarDetalleRecibo(DetalleRecibo detalleRecibo)throws ErrorDAOException{
      try{   
       em.persist(detalleRecibo);
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al guardar DetalleRecibo " + ex.toString());
      } 
   }
   
   public void actualizarDetalleRecibo(DetalleRecibo detalleRecibo)throws ErrorDAOException{
     
      try{  
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(detalleRecibo);
       em.flush();
      } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al actualizar DetalleRecibo " + ex.toString());
      }
   }
   
   public DetalleRecibo buscarDetalleRecibo(String id) throws NoResultException{
       return em.find(DetalleRecibo.class, id);
   }
   
    public Collection<DetalleRecibo> listarDetalleReciboActivo(Recibo recibo) throws ErrorDAOException {
        try {
            return em.createQuery("SELECT r "
                                + " FROM DetalleRecibo r "
                                + " WHERE r.eliminado = FALSE AND r.recibo = :recibo ")
                     .setParameter("recibo", recibo)
                     .getResultList();
        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema al listar detalles de recibo activos: " + ex.toString());
        }
    }
}