/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Inmueble;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;

/**
 *
 * @author franc
 */
@Stateless
@LocalBean
public class DAOInmuebleBean {

    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
    
    public void guardarInmueble(Inmueble inmueble)
    {
        em.persist(inmueble);
    }
    
    public void actualizarInmueble(Inmueble inmueble)
    {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(inmueble);
        em.flush();   
    }
    
    public Inmueble buscarInmueble(String id) throws ErrorDAOException{
      try{   
         return em.find(Inmueble.class, id);
      } catch (Exception ex) {
        throw new ErrorDAOException("Error de sistema: " + ex.toString());
      }  
    }
    public Collection<Inmueble> listarInmuebleActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT i "
                           + "  FROM Inmueble i"
                           + " WHERE i.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
         throw new ErrorDAOException("Error de sistema: " + ex.toString());
      } 
   }

    public Inmueble buscarInmueblePorPisoYPuerta(String piso, String puerta) throws NoResultDAOException{
       
      try{
          
       return (Inmueble)em.createQuery(
                             "SELECT i "
                           + " FROM Inmueble i"
                           + " WHERE i.puerta = :puerta "
                           + " AND i.piso = :piso"
                           + " AND i.eliminado = FALSE")
               .setParameter("piso", piso)
               .setParameter("puerta", puerta)
               .getSingleResult();
              
      } catch (Exception ex) {
         throw new NoResultDAOException("Error de sistema: " + ex.toString());
      } 
   }
    
}
