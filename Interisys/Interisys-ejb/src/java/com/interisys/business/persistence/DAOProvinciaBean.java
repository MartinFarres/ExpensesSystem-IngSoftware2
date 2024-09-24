/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.persistence.ErrorDAOException;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
public class DAOProvinciaBean {

   @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarProvincia(Provincia provincia){
       em.persist(provincia);
   }
   
   public void actualizarProvincia(Provincia provincia){
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(provincia);
       em.flush();
   }
   
   public Provincia buscarProvincia(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
        return em.find(Provincia.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
     } 
   }
   
   public Provincia buscarProvinciaPorPaisYNombre (String idPais, String nombre) throws NoResultDAOException, ErrorDAOException{
     
      try{
          
       return (Provincia) em.createQuery("SELECT p "
                                  + "  FROM Provincia p"
                                  + " WHERE p.pais.id = :idPais"
                                  + "   AND p.nombre = :nombre"
                                  + "   AND p.eliminado = FALSE").
                                  setParameter("idPais", idPais).
                                  setParameter("nombre", nombre).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Collection<Provincia> listarProvinciaActiva()throws ErrorDAOException{
     
      try{
          
       return em.createQuery("SELECT p "
                           + "  FROM Provincia p"
                           + " WHERE p.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
   public Collection<Provincia> listarProvinciaActiva(String idPais)throws ErrorDAOException{
      
      try{
       return em.createQuery("SELECT p "
                           + "  FROM Provincia p"
                           + " WHERE p.eliminado = FALSE"
                           + "   AND p.pais.id = :idPais").
                           setParameter("idPais", idPais).
                           getResultList();
       

      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }
   }
   
}

