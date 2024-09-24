/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Menu;
import com.interisys.business.domain.entity.SubMenu;
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
public class DAOSubMenuBean {
    @PersistenceContext private EntityManager em;
    
    public void guardarSubMenu(SubMenu submenu){
        em.persist(submenu);
    }
    
    public void actualizarSubMenu(SubMenu submenu){
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(submenu);
        em.flush();
    }
    
    public SubMenu buscarSubMenu(String id) throws NoResultException{
        return em.find(SubMenu.class, id);
    }
    
    public SubMenu buscarSubMenuPorNombre(String nombre) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
            return (SubMenu) em.createQuery("SELECT m " + " FROM SubMenu m " + " Where m.nombre = :nombre")
                    .setParameter("nombre", nombre).getSingleResult();
        }catch(NoResultException ex){
            throw new NoResultDAOException("No se encontró información");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ErrorDAOException("Erro del Sistema");
        }
    }
    
     public SubMenu buscarSubMenuPorOrden(String idSubMenu, String nombre)throws NoResultDAOException, ErrorDAOException{
      
      try{ 
          
       return (SubMenu) em.createQuery("SELECT m "
                                     + "  FROM SubMenu m"
                                     + " WHERE m.nombre = :nombre"
                                     + "   AND m.id = :id").
                                     setParameter("nombre", nombre).
                                     setParameter("id", idSubMenu).
                                     getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }
   }
    
    public SubMenu buscarSubMenuPorMenuYOrden(String idMenu, int orden) throws NoResultException, NoResultDAOException, ErrorDAOException{
        try{
          
       return (SubMenu) em.createQuery("SELECT sub "
                                  + "  FROM Menu m, IN (m.submenu) sub "
                                  + " WHERE m.id = :idMenu "
                                  + " AND sub.orden = :orden "
                                  + " ORDER BY sub.orden").
                                  setParameter("idMenu", idMenu).
                                  setParameter("orden", orden).
                                  getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      }  
    }
    
    public Collection<SubMenu> listarSubMenu ()throws ErrorDAOException{
      
      try{  
          
       return em.createQuery("SELECT sub "
                           + "  FROM Menu m, IN (m.submenu) sub "
                           + " ORDER BY sub.orden DESC").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Collection<SubMenu> listarSubMenuActivo ()throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT sub "
                           + "  FROM Menu m, IN (m.submenu) sub "
                           + " WHERE sub.eliminado = FALSE"
                           + "   AND m.eliminado = FALSE"
                           + " ORDER BY sub.orden DESC").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
   
   public Collection<SubMenu> listarSubMenu (String idMenu)throws ErrorDAOException{
       
      try{
          
       return em.createQuery("SELECT sub "
                           + "  FROM Menu m, IN (m.submenu) sub "
                           + " WHERE m.id = :idMenu "
                           + " ORDER BY sub.orden DESC").
                           setParameter("idMenu", idMenu).
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
      } 
   }
    
}
