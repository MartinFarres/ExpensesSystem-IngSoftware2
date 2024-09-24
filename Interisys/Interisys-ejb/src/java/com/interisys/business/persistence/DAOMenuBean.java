/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Menu;
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
public class DAOMenuBean {
    @PersistenceContext private EntityManager em;
    
    public void guardarMenu(Menu menu){
        em.persist(menu);
    }
    
    public void actualizarMenu(Menu menu){
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(menu);
        em.flush();
    }
    
    public Menu buscarMenu(String id) throws NoResultException{
        return em.find(Menu.class, id);
    }
    
    public Menu buscarMenuPorNombre(String nombre) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
            return (Menu) em.createQuery("SELECT m " + " FROM Menu m " + " Where m.nombre = :nombre")
                    .setParameter("nombre", nombre).getSingleResult();
        }catch(NoResultException ex){
            throw new NoResultDAOException("No se encontró información");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Erro del Sistema");
        }
    }
    
    public Menu buscarMenuPorOrden(int orden) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
            return (Menu) em.createQuery("SELECT m " + " FROM Menu m " + " Where m.orden = :orden")
                    .setParameter("orden", orden).getSingleResult();
        }catch(NoResultException ex){
            throw new NoResultDAOException("No se encontró información");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Erro del Sistema");
        }
    }
    
    public Menu buscarMenuPorSubmenu(String idSubmenu) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
          
       return (Menu) em.createQuery("SELECT m "
                                  + "  FROM Menu m, IN (m.submenu) sub "
                                  + " WHERE sub.id = :idSubMenu ").
                                  setParameter("idSubMenu", idSubmenu).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
    }
    
    public Collection<Menu> listarMenu()throws ErrorDAOException{
      
      try{
          
       return em.createQuery("SELECT m "
                           + "  FROM Menu m"
                           + " ORDER BY m.orden DESC").
                             getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
    
   public Collection<Menu> listarMenuActivo()throws ErrorDAOException{
      
      try{
          
       return em.createQuery("SELECT m "
                           + "  FROM Menu m"
                           + " WHERE m.eliminado = FALSE"
                           + " ORDER BY m.orden DESC").
                             getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
   }
    
}
