/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.administracionexpensa;

import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.domain.entity.Recibo;
import com.interisys.business.domain.enumeration.FormaDePago;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.ExpensaInmuebleServiceBean;
import com.interisys.business.logic.InmuebleServiceBean;
import com.interisys.business.logic.ReciboServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class AdministracionExpensaController {

    private String filtro;

    //Servicios Capa de Negocio
    private @EJB
    InmuebleServiceBean inmuebleService;
    private @EJB
    ExpensaInmuebleServiceBean expensaInmuebleService;
    private @EJB
    ReciboServiceBean reciboPagoService;

    //Varaible Capa de Negocio
    private Inmueble inmueble;
    private Collection<Inmueble> inmuebles = new ArrayList();
    private Collection<ExpensaInmueble> expensasInmueble = new ArrayList();
    private ExpensaInmueble expensaInmueble;
    private FormaDePago formaDePago;
    private String observacionPago;
    private Collection<Recibo> recibos = new ArrayList();
    private Collection<SelectItem> formasDePago = new ArrayList();

    private String linkWhatsApp;

    @PostConstruct
    public void init() {
        buscar();
    }

    public void buscar() {

        try {
            inmuebles.clear();
            if (filtro == null || filtro.isEmpty()) {
                inmuebles.addAll(inmuebleService.listarInmuebleActivo());
            } else {
                inmuebles.addAll(inmuebleService.listarInmuebleConFiltro(filtro));
            }
            System.out.println("Se encontraron: " + inmuebles.size());
            RequestContext.getCurrentInstance().update("formPpal:panelBusqueda");
            RequestContext.getCurrentInstance().update("formPpal:tablaBusqueda");
        } catch (ErrorServiceException e) {
            Message.show("Error al listar los inmuebles", MessageType.ERROR);
        }
    }

    public void actualzarExpensaRecibo() {

        try {

            //Se buscan las expensas del inmueble
            expensasInmueble.clear();
            expensasInmueble.addAll(expensaInmuebleService.listarExpensaInmuebleDeInmueble(inmueble.getId()));

            //Se buscan los recibos de las expensas
            recibos.clear();
            recibos.addAll(reciboPagoService.listarReciboDeInmueble(inmueble.getId()));
            //Se actualiza la pantalla
            RequestContext.getCurrentInstance().update("formPpal:panelExpensaInmueble");
            RequestContext.getCurrentInstance().update("formPpal:tablaExpensaInmueble");
            RequestContext.getCurrentInstance().update("formPpal:panelRecibo");
            RequestContext.getCurrentInstance().update("formPpal:tablaRecibo");
            RequestContext.getCurrentInstance().update("formPpal:panelGeneralInformacion");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public void abrirWhatsApp(ExpensaInmueble expensa) {

//        try {
//
//            if (expensa.getInmueble().getPropietario().getTelefono() != null && !expensa.getInmueble().getPropietario().getTelefono().trim().isEmpty()) {
//                linkWhatsApp = "https://api.whatsapp.com/send?phone=+549" + expensa.getInmueble().getPropietario().getTelefono() + "&text=" + expensaInmuebleService.crearMensajeNotificacion(expensa.getId());
//                RequestContext.getCurrentInstance().update("formPpal:panelLinkWhatsApp");
//                RequestContext.getCurrentInstance().update("formPpal:panelDetalleLinkWhatsApp");
//                RequestContext.getCurrentInstance().execute("PF('dlgLinkWhatsApp').show()");
//            }
//
//        } catch (Exception e) {
//            Message.show(e.getMessage(), MessageType.ERROR);
//        }
    }

    public void irWhatsApp() {

        try {

            RequestContext.getCurrentInstance().execute("PF('dlgLinkWhatsApp').hide()");

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(linkWhatsApp);

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public void abrirPopUpPagoExpensa(ExpensaInmueble expensaInmueble) {

        try {

            this.expensaInmueble = expensaInmueble;
            formaDePago = null;
            observacionPago = "";
            cargarComboFormaDePago();

            RequestContext.getCurrentInstance().update("formPpal:panelPagoExpensa");
            RequestContext.getCurrentInstance().execute("PF('dlgPagoExpensa').show()");

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    private void cargarComboFormaDePago() {

        try {

            formasDePago = new ArrayList<>();
            formasDePago.add(new SelectItem(null, "Seleccione..."));
            formasDePago.add(new SelectItem(FormaDePago.EFECTIVO, "EFECTIVO"));
            formasDePago.add(new SelectItem(FormaDePago.TRANSFERENCIA, "TRANSFERENCIA"));
            formasDePago.add(new SelectItem(FormaDePago.BILLETERA_VIRTUAL, "BILLETERA VIRTUAL"));

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public void aceptarPagoExpensa() {

        try {
            expensaInmuebleService.pagarExpensaInmueble(expensaInmueble.getId(), formaDePago, observacionPago);

            actualzarExpensaRecibo();

            RequestContext.getCurrentInstance().update("formPpal:panelExpensaInmueble");
            RequestContext.getCurrentInstance().update("formPpal:tablaExpensaInmueble");
            RequestContext.getCurrentInstance().update("formPpal:panelRecibo");
            RequestContext.getCurrentInstance().update("formPpal:tablaRecibo");
            RequestContext.getCurrentInstance().execute("PF('dlgPagoExpensa').hide()");

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

        public void enviarReciboPago (Recibo recibo){
        
        try{
           
             reciboPagoService.enviarRecibo(recibo.getId(), "C:\\");
             Message.show("El recibo se envió correctamente", MessageType.NOTIFICACION);
             
        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Collection<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Collection<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public Collection<ExpensaInmueble> getExpensasInmueble() {
        return expensasInmueble;
    }

    public void setExpensasInmueble(Collection<ExpensaInmueble> expensasInmueble) {
        this.expensasInmueble = expensasInmueble;
    }

    public ExpensaInmueble getExpensaInmueble() {
        return expensaInmueble;
    }

    public void setExpensaInmueble(ExpensaInmueble expensaInmueble) {
        this.expensaInmueble = expensaInmueble;
    }

    public FormaDePago getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(FormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public String getObservacionPago() {
        return observacionPago;
    }

    public void setObservacionPago(String observacionPago) {
        this.observacionPago = observacionPago;
    }

    public Collection<Recibo> getRecibos() {
        return recibos;
    }

    public void setRecibos(Collection<Recibo> recibos) {
        this.recibos = recibos;
    }

    public Collection<SelectItem> getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(Collection<SelectItem> formasDePago) {
        this.formasDePago = formasDePago;
    }

    public String getLinkWhatsApp() {
        return linkWhatsApp;
    }

    public void setLinkWhatsApp(String linkWhatsApp) {
        this.linkWhatsApp = linkWhatsApp;
    }

}
