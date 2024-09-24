
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Inquilino;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author aidal
 */
@Stateless
@LocalBean
public class DAOInquilinoBean {

    @PersistenceContext private EntityManager em;
   
    public void guardarInquilino(Inquilino inquilino)throws ErrorDAOException{
      try{  
       em.persist(inquilino);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      }  
    }
   
    public void actualizarInquilino(Inquilino inquilino)throws ErrorDAOException{
  
      try{
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(inquilino);
       em.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      } 
    }
   
    public Inquilino buscarInquilino(String id) throws NoResultException{
       return em.find(Inquilino.class, id);
    }
   
    public Inquilino buscarInquilinoPorDocumento (String documento) throws NoResultDAOException, ErrorDAOException{
     
      try{
          
       return (Inquilino) em.createQuery("SELECT i "
                                       + "  FROM Inquilino i"
                                       + " WHERE i.documento = :documento"
                                       + "   AND i.eliminado = FALSE").
                                       setParameter("documento", documento).
                                       getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
    }
    
    public Collection<Inquilino> listarInquilinoActivo()throws ErrorDAOException{
     
      try{
          
       return em.createQuery("SELECT i "
                           + "  FROM Inquilino i"
                           + " WHERE i.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
}
