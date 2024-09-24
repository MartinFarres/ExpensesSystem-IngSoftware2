/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Propietario;
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
public class DAOPropietarioBean {

    @PersistenceContext private EntityManager em;
   
    public void guardarPropietario(Propietario propietario){
       em.persist(propietario);
    }
   
    public void actualizarPropietario(Propietario propietario){
       //Establece el modo de sincronización de datos de la transacción
       em.setFlushMode(FlushModeType.COMMIT);
       // Actualiza la entidad existente en la base de datos con los valores del objeto Propietario que recibe como parámetro
       em.merge(propietario);
       //Sincroniza inmediatamente los cambios en el contexto de persistencia con la base de datos
       em.flush();
    }
   
    public Propietario buscarPropietario(String id) throws NoResultException{
       return em.find(Propietario.class, id);
    }
   
    public Propietario buscarPropietarioPorNombreApellido (String nombre, String apellido) throws NoResultDAOException, ErrorDAOException{
     
      try{
          
       return (Propietario) em.createQuery("SELECT p "
                                       + "  FROM Propietario p"
                                       + " WHERE p.nombre = :nombre"
                                       + "   AND p.apellido = :apellido"
                                       + "   AND p.eliminado = FALSE").
                                       setParameter("nombre", nombre).
                                       setParameter("apellido", apellido).
                                       getSingleResult();
       
      } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información del propietario.");
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema al buscar propietario.");
      } 
    }
    
    public Collection<Propietario> listarPropietarioActivo()throws ErrorDAOException{
     
      try{
          
       return em.createQuery("SELECT p "
                           + "  FROM Propietario p"
                           + " WHERE p.eliminado = FALSE").
                           getResultList();
       
      } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema al listar propietarios.");
      }  
   }
}