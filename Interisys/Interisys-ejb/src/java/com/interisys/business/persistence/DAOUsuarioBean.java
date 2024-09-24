/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.interisys.business.persistence;
import com.interisys.business.domain.entity.Usuario;
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
 * @author martin
 */
@Stateless
@LocalBean
public class DAOUsuarioBean {

   @PersistenceContext private EntityManager em;
   
   public void guardarUsuario(Usuario usuario){
       em.persist(usuario);
   }
   
   public void actualizarUsuario(Usuario usuario){
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(usuario);
       em.flush();
   }
   
   public Usuario buscarUsuario(String id) throws NoResultException{
       return em.find(Usuario.class, id);
   }
   
   public Usuario buscarUsuarioPorCuenta (String cuenta) throws NoResultDAOException, ErrorDAOException{
       
       try{
           
       return (Usuario) em.createQuery("SELECT u "
                                  + "  FROM Usuario u"
                                  + " WHERE u.usuario = :cuenta"
                                  + "   AND u.eliminado = FALSE").
                                  setParameter("cuenta", cuenta).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Collection<Usuario> listarUsuarioActivo()throws ErrorDAOException{
       
      try{ 
          
       return em.createQuery("SELECT u"
                           + "  FROM Usuario u"
                           + " WHERE u.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Usuario buscarUsuarioPorCuentaYClave (String cuenta, String clave) throws NoResultDAOException, ErrorDAOException{
       
      try{ 
          
       return (Usuario) em.createQuery("SELECT u "
                                  + "  FROM Usuario u"
                                  + " WHERE u.usuario = :cuenta"
                                  + "   AND u.clave = :clave"
                                  + "   AND u.eliminado = FALSE").
                                  setParameter("cuenta", cuenta).
                                  setParameter("clave", clave).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }
   }
}