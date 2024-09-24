/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Nacionalidad;
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
 * Acceder y manipular los datos de las nacionalidades en la base de datos.
 * 
 * @author aida
 */
@Stateless
@LocalBean
public class DAONacionalidadBean {

    @PersistenceContext(unitName = "Interisys-ejbPU")
    private EntityManager em;


    public void guardarNacionalidad(Nacionalidad nacionalidad) {
        em.persist(nacionalidad);
    }


    public void actualizarNacionalidad(Nacionalidad nacionalidad) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(nacionalidad);
        em.flush();
    }


    public Nacionalidad buscarNacionalidad(String id) throws NoResultException {
        return em.find(Nacionalidad.class, id);
    }

    public Nacionalidad buscarNacionalidadPorNombre(String nombre) throws NoResultDAOException, ErrorDAOException {

        try {
            // Validación de longitud del nombre
            if (nombre.length() > 255) {
                throw new ErrorDAOException("La longitud del nombre debe ser menor o igual que 255 caracteres");
            }

            // Consulta para buscar la nacionalidad por nombre
            return (Nacionalidad) em.createQuery("SELECT n "
                    + "  FROM Nacionalidad n"
                    + " WHERE n.nombre = :nombre"
                    + "   AND n.eliminado = FALSE")
                    .setParameter("nombre", nombre)
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

    public Collection<Nacionalidad> listarNacionalidadActiva() throws ErrorDAOException {

        try {
            return em.createQuery("SELECT n "
                    + "  FROM Nacionalidad n"
                    + " WHERE n.eliminado = FALSE")
                    .getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
}

