/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.DetalleRecibo;
import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.domain.entity.Recibo;
import com.interisys.business.domain.enumeration.EstadoRecibo;
import com.interisys.business.domain.enumeration.FormaDePago;
import com.interisys.business.persistence.DAOReciboBean;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author franc
 */
@Stateless
@LocalBean
public class ReciboServiceBean {

    private @EJB
    DAOReciboBean dao;

    private @EJB
    DetalleReciboServiceBean detalleService;

    private @EJB
    ExpensaInmuebleServiceBean expensaInmuebleService;

    public void crearRecibo(String idExpensaInmueble, FormaDePago formaDePago, String observacion) throws ErrorServiceException {

        try {

            ExpensaInmueble expensaInmueble = expensaInmuebleService.buscarExpensaInmueble(idExpensaInmueble);

            //Se crea el recibo
            Recibo recibo = new Recibo();
            recibo.setId(UUID.randomUUID().toString());
            recibo.setEstado(EstadoRecibo.PENDIENTE_ENTREGA);
            recibo.setFormaDePago(formaDePago);
            recibo.setObservacion(observacion == null ? "" : observacion);
            recibo.setTotal(expensaInmueble.getExpensa().getImporte());
            recibo.setEliminado(false);
            dao.guardarRecibo(recibo);

            //Se crea el detalle y se vincula al recibo
            detalleService.crearDetalleRecibo(
                    expensaInmueble.getExpensa().getImporte(),
                    expensaInmueble.getId(),
                    recibo.getId());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas. No se pudo crear el recibo: " + ex.toString());
        }
    }

    public void modificarRecibo(String idRecibo, Date fechaPago, FormaDePago formaPago, String observacion) throws ErrorServiceException {

        try {

            Recibo recibo = dao.buscarRecibo(idRecibo);

            if (fechaPago == null) {
                throw new ErrorServiceException("Debe indicar la fecha de pago");
            }

            recibo.setFechaPago(fechaPago);
            recibo.setFormaDePago(formaPago);
            recibo.setObservacion(observacion);
            recibo.setEliminado(false);

            dao.guardarRecibo(recibo);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas: " + ex.toString());
        }
    }

    public void eliminarRecibo(String idRecibo) throws ErrorServiceException {

        try {

            Recibo recibo = buscarRecibo(idRecibo);
            recibo.setEliminado(true);
            dao.actualizarRecibo(recibo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }

    public Recibo buscarRecibo(String idRecibo) throws ErrorServiceException {

        try {

            if (idRecibo == null || !idRecibo.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el recibo");
            }

            Recibo recibo = dao.buscarRecibo(idRecibo);

            if (recibo.isEliminado()) {
                throw new ErrorServiceException("No se encuentra el recibo indicado");
            }

            return recibo;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }

    public Collection<Recibo> listarReciboActivo() throws ErrorServiceException {

        try {
            return dao.listarReciboActivo();
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }

    public Collection<Recibo> listarReciboDeInmueble(String idInmueble) throws ErrorServiceException {

        try {
            return dao.listarReciboDeInmueble(idInmueble);
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema: " + ex.toString());
        }
    }

    public void enviarRecibo(String idRecibo) {
        // TODO Completar
    }
}
