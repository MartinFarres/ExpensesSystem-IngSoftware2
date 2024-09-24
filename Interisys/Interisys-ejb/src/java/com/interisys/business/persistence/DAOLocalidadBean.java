package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.Localidad;
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
public class DAOLocalidadBean {

   @PersistenceContext private EntityManager em;
   
   public void guardarLocalidad(Localidad localidad)throws ErrorDAOException{
      try{ 
       em.persist(localidad);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public void actualizarLocalidad(Localidad localidad)throws ErrorDAOException{
       try{
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(localidad);
        em.flush();
       } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Localidad buscarLocalidad(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
            return em.find(Localidad.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
     } 
   }
   
   public Localidad buscarLocalidadPorDepartamentoYNombre (String idDepartamento, String nombre) throws NoResultDAOException, ErrorDAOException{
      
      try{
          
        return (Localidad) em.createQuery("SELECT d "
                                  + "  FROM Localidad d"
                                  + " WHERE d.nombre = :nombre"
                                  + "   AND d.departamento.id = :idDepartamento"
                                  + "   AND d.eliminado = FALSE").
                                  setParameter("idDepartamento", idDepartamento).
                                  setParameter("nombre", nombre).
                                  getSingleResult();
        
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");  
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
   public Collection<Localidad> listarLocalidadActiva() throws ErrorDAOException{
       
       try{
       
        return em.createQuery("SELECT d "
                           + "  FROM Localidad d"
                           + " WHERE d.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }
   }
   
   public Collection<Localidad> listarLocalidadActiva(String idDepartamento)throws ErrorDAOException{
      
      try{ 
          
       return em.createQuery("SELECT p "
                           + "  FROM Localidad p"
                           + " WHERE p.eliminado = FALSE"
                           + "   AND p.departamento.id = :idDepartamento").
                           setParameter("idDepartamento", idDepartamento).
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
}
