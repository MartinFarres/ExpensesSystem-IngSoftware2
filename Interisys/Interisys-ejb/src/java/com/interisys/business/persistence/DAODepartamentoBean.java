package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Departamento;
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
public class DAODepartamentoBean {

   @PersistenceContext(unitName="Interisys-ejbPU")
   private EntityManager em;
   
   public void guardarDepartamento(Departamento departamento){
       em.persist(departamento);
       /*
       try{ 
       em.persist(departamento);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
      }  */
   }
   
   public void actualizarDepartamento(Departamento departamento){
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(departamento);
       em.flush();
       /*
       } catch (Exception ex) {
        ex.printStackTrace();
        throw new ErrorDAOException("Error de sistema");
     }*/
   }
   
   public Departamento buscarDepartamento(String id) throws NoResultException{
       return em.find(Departamento.class, id);
   }
   
   public Departamento buscarDepartamentoPorProvinciaYNombre(String idProvincia, String nombre) throws NoResultDAOException, ErrorDAOException{
       
      try {
         
       if (nombre.length() > 255) {
          throw new ErrorDAOException("La longitud del nombre debe ser menor o igual que 255 caracteres");  
       }   
          
       return (Departamento) em.createQuery("SELECT d "
                                  + "  FROM Departamento d"
                                  + " WHERE d.nombre = :nombre"
                                  + "   AND d.eliminado = FALSE"
                                  + "   AND d.provincia.id =: idProvincia")
                                  .setParameter("nombre", nombre)
                                  .setParameter("idProvincia", idProvincia)
                                  .getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (ErrorDAOException ex) {
            throw ex;
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
   
   public Collection<Departamento> listarDepartamentoActivo() throws ErrorDAOException {
       
      try {
          
       return em.createQuery("SELECT d "
                           + "  FROM Departamento d"
                           + " WHERE d.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Collection<Departamento> listarDepartamentoPorProvinciaActivo(String idProvincia) throws ErrorDAOException {
       
      try {
          
       return em.createQuery("SELECT d "
                           + "  FROM Departamento d"
                           + " WHERE d.eliminado = FALSE"
                           + " AND d.provincia.id =: idProvincia").
                           setParameter("idProvincia", idProvincia).
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
}
