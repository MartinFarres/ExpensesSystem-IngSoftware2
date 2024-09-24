package com.interisys.controller.propietario;

import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.logic.PropietarioServiceBean;
import com.interisys.controller.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author aidal
 */
@ManagedBean
@ViewScoped
public class PropietarioListController {

    private @EJB PropietarioServiceBean propietarioService;
    
    private Collection<Propietario> propietarios = new ArrayList<>();

    public Collection<Propietario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(Collection<Propietario> propietarios) {
        this.propietarios = propietarios;
    }

    @PostConstruct
    public void init() {
        try {
            listarPropietarios();
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarPropietarios() {
        try {
            propietarios.clear();
            propietarios.addAll(propietarioService.listarPropietarioActivo());

            RequestContext.getCurrentInstance().update("formPpal:panelTablaPropietario");
            RequestContext.getCurrentInstance().update("formPpal:tablaPropietario");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String alta() {
        try {
            guardarPropietarioSession(CasoDeUsoType.ALTA, null);
            return "editPropietario";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Propietario propietario) {
        try {
            guardarPropietarioSession(CasoDeUsoType.CONSULTAR, propietario);
            return "editPropietario";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Propietario propietario) {
        try {
            guardarPropietarioSession(CasoDeUsoType.MODIFICAR, propietario);
            return "editPropietario";
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public void baja(Propietario propietario) {
        try {
            propietarioService.eliminarPropietario(propietario.getId());
            listarPropietarios();
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");

        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    private void guardarPropietarioSession(CasoDeUsoType casoDeUso, Propietario propietario) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);
        session.setAttribute("PROPIETARIO", propietario);
    }
}

