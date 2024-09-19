/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.Pais;
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
 * @author franc
 */
@Stateless
@LocalBean
public class DAOPaisBean {

   @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
   
   public void guardarPais(Pais pais)throws ErrorDAOException{
      try{ 
       em.persist(pais);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
      } 
   }
   
   public void actualizarPais(Pais pais)throws ErrorDAOException{
       try{
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(pais);
        em.flush();
       } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
      } 
   }
   
   public Pais buscarPais(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
        return em.find(Pais.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
     } 
   }
   
   
   public Pais buscarPaisPorNombre (String nombre) throws NoResultDAOException, ErrorDAOException{
       
      try{ 
         
       if (nombre.length() > 255){
          throw new ErrorDAOException("La longitud del nombre debe ser menor o igual que 255 caracteres");  
       }   
          
       return (Pais) em.createQuery("SELECT p "
                                  + "  FROM Pais p"
                                  + " WHERE p.nombre = :nombre"
                                  + "   AND p.eliminado = FALSE").
                                  setParameter("nombre", nombre).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (ErrorDAOException ex) {
            throw ex;
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
   public Collection<Pais> listarPaisActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT p "
                           + "  FROM Pais p"
                           + " WHERE p.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
}
