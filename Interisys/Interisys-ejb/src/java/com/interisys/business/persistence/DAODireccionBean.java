package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.Direccion;
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
public class DAODireccionBean {
    
    @PersistenceContext(unitName="Interisys-ejbPU") private EntityManager em;
    
    public void guardarDireccion(Direccion direccion)throws ErrorDAOException{
      try{ 
       em.persist(direccion);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error del sistema");
      } 
   }
   
   public void actualizarDireccion(Direccion direccion)throws ErrorDAOException{
       try{
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(direccion);
        em.flush();
       } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Direccion buscarDireccion(String id) throws NoResultDAOException, ErrorDAOException{
       
     try{
         
        return em.find(Direccion.class, id);
        
     } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
     } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
     } 
   }
   
   public Collection<Direccion> listarDireccionActiva() throws ErrorDAOException{
       
       try{
       
        return em.createQuery("SELECT d "
                           + "  FROM Direccion d"
                           + " WHERE d.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
      }
   }
   
   public Collection<Direccion> listarDireccionActiva(String idLocalidad)throws ErrorDAOException{
      
      try{ 
          
       return em.createQuery("SELECT p "
                           + "  FROM Direccion p"
                           + " WHERE p.eliminado = FALSE"
                           + "   AND p.localidad.id = :idLocalidad").
                           setParameter("idLocalidad", idLocalidad).
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error del sistema");
      } 
   }
    
}
