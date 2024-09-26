/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.CuentaCorreo;
import com.interisys.business.domain.entity.DetalleRecibo;
import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Propietario;
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
import javax.mail.Message;
import javax.mail.Session;

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


    private CuentaCorreo correoOrigen;
    private Propietario propietario;
    private Inquilino inquilino;
    private String tituloCorreo;
    private String contenidoMensaje;
    private String destinatario;
    
    private DetalleReciboServiceBean serviceDetalle;
    private CuentaCorreoServiceBean serviceCuentaCorreo;
    private GestionMailServiceBean serviceGestionMail;
    
    public void enviarRecibo(String idRecibo, String path) throws ErrorServiceException {
        /*
        contenidoMensaje: cuerpo del mensaje en HTML.
        pathArchivoAdjunto: ruta al archivo que se desea adjuntar (puede ser null si no hay adjunto).
        */
        try{
            
            Collection<DetalleRecibo> detalles = serviceDetalle.listarDetalleReciboActivo(idRecibo);
            //obtengo el primer detalle
            if (!detalles.isEmpty()) {
                DetalleRecibo detalle = detalles.iterator().next(); 
           
                correoOrigen = serviceCuentaCorreo.buscarCuentaCorreoActiva();
                propietario = detalle.getExpensaInmueble().getInmueble().getPropietario();
                inquilino = detalle.getExpensaInmueble().getInmueble().getInquilino();
                tituloCorreo = "Envío de recibo expensa";
                contenidoMensaje = "A continuación dejamos registro del recibo en un archivo PDF. Gracias!";
            }
   
            //NO SE ENVIAN IMAGENES //String imagen = "<img src=\"cid:image\">";  
            /*el CID se utiliza para incrustar imágenes dentro del contenido HTML del correo de manera 
            que se muestren directamente en el cuerpo del mensaje en lugar de ser enlaces externos*/
            StringBuilder html = new StringBuilder(); //crear estructura básica de HTML
            //tabla con estilos CSS 
            html.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=us-ascii\"></head><body>");
            html.append("<table style=\"font-family: Verdana,sans-serif; font-size: 13px; color: #0C1C29; width: 750px\"> <tbody>");
            html.append("<tr>");
            html.append("<td align=\"left\" style=\"background-color: #0011fb; color: #FFFFFF; font-size:1.2em; font-weight: bold; padding: 0\"> " + "OSPELSYM." + "  </td>");
            html.append("</tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr>");
            html.append("<td align=\"left\">");
            html.append(contenidoMensaje + "</td>"); //mensaje
            html.append("</tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            
            
            if (inquilino == null) {
                destinatario = propietario.getCorreoElectronico();
            } else {
                destinatario = inquilino.getCorreoElectronico();
            }
               //,pathArchivoAdjunto
            serviceGestionMail.enviarEmailHTML(destinatario, html.toString(), correoOrigen.getSmtp(), correoOrigen.getPuerto(), correoOrigen.getCorreo(), correoOrigen.getClave(), correoOrigen.isTls(), "CONSORCIO", tituloCorreo, path);
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    
}
