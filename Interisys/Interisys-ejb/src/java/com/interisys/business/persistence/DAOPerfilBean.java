/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Perfil;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kalit
 */
@Stateless
@LocalBean
public class DAOPerfilBean {
    @PersistenceContext private EntityManager em;
    
    public void guardarPerfil(Perfil perfil) throws ErrorDAOException{
        try{
            em.persist(perfil);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Error de Sistema");
        }
    }
    
    public void actualizarPerfil(Perfil perfil) throws ErrorDAOException{
        try{
            em.setFlushMode(FlushModeType.COMMIT);
            em.merge(perfil);
            em.flush();
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
    
    public Perfil buscarPerfil(String id) throws NoResultException{
        return em.find(Perfil.class, id);
    }
    
    public Perfil buscarPerfilPorNombre(String nombre) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
            return (Perfil) em.createQuery("SELECT p" + " FROM Perfil p" + " WHERE p.nombre = :nombre" + " AND p.eliminado = FALSE").setParameter("nombre", nombre).getSingleResult();
        }catch(NoResultException ex){
            throw new NoResultDAOException("No se encontro informacion");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
    
    public Collection<Perfil> listarPerfilActivo()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT p "
                           + "  FROM Perfil p"
                           + " WHERE p.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
    
}
