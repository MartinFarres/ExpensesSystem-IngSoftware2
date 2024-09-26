/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.expensainmueble;

import com.interisys.business.domain.entity.Expensa;
import com.interisys.business.domain.entity.ExpensaInmueble;
import com.interisys.business.domain.entity.Inmueble;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.ExpensaInmuebleServiceBean;
import com.interisys.business.logic.ExpensaServiceBean;
import com.interisys.business.logic.InmuebleServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author franc
 */
@ManagedBean
@ViewScoped
public class ExpensaInmuebleEditController {

    //Servicios Capa de Negocio
    private @EJB
    ExpensaInmuebleServiceBean expensaInmuebleService;
    private @EJB
    ExpensaServiceBean expensaService;
    private @EJB
    InmuebleServiceBean inmuebleService;

    //Variables Capa de Negocio
    private ExpensaInmueble expensaInmueble;

    //Variables Vista
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    private Collection<SelectItem> inmuebles = new ArrayList();
    private Collection<SelectItem> expensas = new ArrayList();

    @PostConstruct
    public void init() {
        // Obtener el caso de uso desde la sesión
        casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");

        // Crear nuevo propietario o cargar el existente según el caso de uso
        if (casoDeUso == CasoDeUsoType.ALTA) {
            expensaInmueble = new ExpensaInmueble();
            expensaInmueble.setExpensa(new Expensa());
            expensaInmueble.setInmueble(new Inmueble());
            expensaInmueble.setPeriodo(new Date());
            expensaInmueble.setFechaVencimiento(new Date());

        } else {
            expensaInmueble = (ExpensaInmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSAINMUEBLE");
        }

        // Desactivar campos si es un caso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
        cargaComboExpensa();
        cargaComboInmueble();
    }

    public String aceptar() {
        try {
            switch (casoDeUso) {
                case ALTA:
                    expensaInmuebleService.crearExpensaInmueble(
                            expensaInmueble.getExpensa().getId(),
                            expensaInmueble.getInmueble().getId(),
                            expensaInmueble.getPeriodo());

                    Message.show("ExpensaInmueble creada exitosamente", MessageType.NOTIFICACION);
                    break;

                case MODIFICAR:
                    expensaInmuebleService.modificarExpensaInmueble(
                            expensaInmueble.getId(),
                            expensaInmueble.getExpensa().getId(),
                            expensaInmueble.getInmueble().getId(),
                            expensaInmueble.getPeriodo(),
                            expensaInmueble.getFechaVencimiento(),
                            expensaInmueble.getEstado());
                    Message.show("ExpensaInmueble modificada exitosamente", MessageType.NOTIFICACION);
                    break;

                case CONSULTAR:
                    // Lógica para consulta, en este caso no hay acciones específicas.
                    break;
            }
            return "listExpensaInmueble";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public String cancelar() {
        return "listExpensaInmueble";
    }
   
    private void cargaComboInmueble() {
        try {

            inmuebles = new ArrayList<>();
            inmuebles.add(new SelectItem(null, "Seleccione..."));
            for (Inmueble inmueble : inmuebleService.listarInmuebleActivo()) {
                inmuebles.add(
                        new SelectItem(
                                inmueble.getId(),
                                "Piso: " + inmueble.getPiso() + ", puerta: " + inmueble.getPuerta()));
            }

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public void cargaComboExpensa() {
        try {

            expensas = new ArrayList<>();
            expensas.add(new SelectItem(null, "Seleccione..."));
            for (Expensa expensa : expensaService.listarExpensasActivo()) {
                if (expensa.getFechaHasta() != null) {
                    expensas.add(new SelectItem(expensa.getId(), expensa.getFechaDesde().toString() + " $:" + expensa.getImporte()));
                } else {
                    expensas.add(new SelectItem(expensa.getId(), "Importe Actual $:" + expensa.getImporte()));
                }

            }

        } catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    public ExpensaInmueble getExpensaInmueble() {
        return expensaInmueble;
    }

    public void setExpensaInmueble(ExpensaInmueble expensaInmueble) {
        this.expensaInmueble = expensaInmueble;
    }

    public CasoDeUsoType getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(CasoDeUsoType casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public Collection<SelectItem> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Collection<SelectItem> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public Collection<SelectItem> getExpensas() {
        return expensas;
    }

    public void setExpensas(Collection<SelectItem> expensas) {
        this.expensas = expensas;
    }
}
