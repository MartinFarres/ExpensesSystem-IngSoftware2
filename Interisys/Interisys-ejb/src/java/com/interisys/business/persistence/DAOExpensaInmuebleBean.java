/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.persistence;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.domain.entity.ExpensaInmueble;
import java.util.Collection;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author franc
 */
@Stateless
@LocalBean
public class DAOExpensaInmuebleBean {

    @PersistenceContext(unitName = "Interisys-ejbPU")
    private EntityManager em;

    public void guardarExpensaInmueble(ExpensaInmueble expensaInmueble) throws ErrorDAOException {
        try {
            em.persist(expensaInmueble);
        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
        }
    }

    public void actualizarExpensaInmueble(ExpensaInmueble expensaInmueble) throws ErrorDAOException {

        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.merge(expensaInmueble);
            em.flush();
        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema. No se pudo actualizar la ExpensaInmueble: " + ex.toString());
        }
    }

    public ExpensaInmueble buscarExpensaInmueble(String id) throws NoResultException {
        return em.find(ExpensaInmueble.class, id);
    }

    public ExpensaInmueble buscarExpensaInmueble(String idExpensa, String idInmueble, Date periodo) throws NoResultDAOException, ErrorDAOException {
        try {

            return (ExpensaInmueble) em.createQuery("SELECT ei "
                    + " FROM ExpensaInmueble ei"
                    + " WHERE ei.expensa.id = :idExpensa"
                    + " AND ei.inmueble.id = :idInmueble"
                    + " AND ei.periodo = :periodo"
                    + " AND ei.eliminado = FALSE")
                    .setParameter("idExpensa", idExpensa)
                    .setParameter("idInmueble", idInmueble)
                    .setParameter("periodo", periodo)
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getSingleResult();

        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema");
        }
    }

    public Collection<ExpensaInmueble> listarExpensaInmuebleActivo() throws ErrorDAOException {

        try {

            return em.createQuery(" SELECT e "
                    + " FROM ExpensaInmueble e"
                    + " WHERE e.eliminado = FALSE"
                    + " ORDER BY e.periodo DESC").
                    getResultList();

        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
        }
    }

    public Collection<ExpensaInmueble> listarExpensaInmuebleDeInmueble(String idInmueble) throws ErrorDAOException {

        try {

            return em.createQuery(" SELECT e "
                    + " FROM ExpensaInmueble e"
                    + " WHERE e.eliminado = FALSE"
                    + "  AND  e.inmueble.id = :idInmueble"
                    + " ORDER BY e.periodo DESC")
                    .setParameter("idInmueble", idInmueble)
                    .getResultList();

        } catch (Exception ex) {
            throw new ErrorDAOException("Error de sistema: " + ex.toString());
        }
    }
}
