package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import java.util.Collection;
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
public class DAOConsorcioBean {
    
   @PersistenceContext private EntityManager em;
   
    public void guardarConsorcio(Consorcio consorcio)throws ErrorDAOException{
       
     try{ 
         
       em.persist(consorcio);
       
     } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
     }   
   }
    
    public Consorcio buscarConsorcio(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
        return em.find(Consorcio.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
     } 
   }
   
    public void actualizarConsorcio(Consorcio consorcio)throws ErrorDAOException{
       
      try{
          
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(consorcio);
        em.flush();
       
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
      }  
   }

    public Consorcio buscarConsorcioPorNombre (String nombre) throws NoResultDAOException, ErrorDAOException{
       
      try{ 
          
       return (Consorcio) em.createQuery("SELECT C "
                                  + "  FROM Consorcio C"
                                  + " WHERE C.nombre = :nombre"
                                  + "   AND C.eliminado = FALSE").
                                  setParameter("nombre", nombre).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
      }  
   }
   
   public Collection<Consorcio> listarConsorcioActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT c "
                           + "  FROM Consorcio c"
                           + " WHERE c.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
      } 
   }
    
}
