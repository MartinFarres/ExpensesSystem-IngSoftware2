package com.interisys.controller.propietario;

import com.interisys.business.domain.entity.Propietario;
import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.logic.PropietarioServiceBean;
import com.interisys.business.logic.DireccionServiceBean;
import com.interisys.controller.enumeration.Message;
import com.interisys.controller.enumeration.CasoDeUsoType;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author aidal
 */
@ManagedBean
@ViewScoped
public class PropietarioEditController {

    private @EJB 
    PropietarioServiceBean propietarioService;
    private @EJB 
    DireccionServiceBean direccionService;

    private Propietario propietario;
    private CasoDeUsoType casoDeUso;
    private boolean campoDesactivado;
    
    private Collection<SelectItem> direcciones = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Obtener el caso de uso desde la sesión
        casoDeUso = (CasoDeUsoType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO");

        // Crear nuevo propietario o cargar el existente según el caso de uso
        if (casoDeUso == CasoDeUsoType.ALTA) {
            propietario = new Propietario();
            propietario.setDireccion(new Direccion());
        } else {
            propietario = (Propietario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PROPIETARIO");
        }

        // Desactivar campos si es un caso de consulta
        campoDesactivado = casoDeUso == CasoDeUsoType.CONSULTAR;
        
        // Cargar direcciones disponibles
        cargarComboDireccion();
    }

public String aceptar() {
    try {
        // Obtener la dirección seleccionada usando el ID
        Direccion direccionSeleccionada = direccionService.buscarDireccion(propietario.getDireccion().getId());

        // Asignar la dirección seleccionada al propietario
        propietario.setDireccion(direccionSeleccionada);

        switch (casoDeUso) {
            case ALTA:
                propietarioService.crearPropietario(
                        propietario.getNombre(),
                        propietario.getApellido(),
                        propietario.getCorreoElectronico(),
                        propietario.getTelefono(),
                        propietario.isHabitaConsorcio(),
                        propietario.getDireccion());
                Message.show("Propietario creado exitosamente", MessageType.NOTIFICACION);
                break;

            case MODIFICAR:
                propietarioService.modificarPropietario(
                        propietario.getId(),
                        propietario.getNombre(),
                        propietario.getApellido(),
                        propietario.getCorreoElectronico(),
                        propietario.getTelefono(),
                        propietario.isHabitaConsorcio(),
                        propietario.getDireccion());
                Message.show("Propietario modificado exitosamente", MessageType.NOTIFICACION);
                break;

            case CONSULTAR:
                // Lógica para consulta, en este caso no hay acciones específicas.
                break;
        }
        return "listPropietario";
    } catch (Exception e) {
        e.printStackTrace();
        Message.show(e.getMessage(), MessageType.ERROR);
        return null;
    }
}


    public String cancelar() {
        return "listPropietario";
    }

    // Método para cargar direcciones desde el servicio
    public void cargarComboDireccion() {
        try {  
        direcciones = new ArrayList<SelectItem>();
        direcciones.add(new SelectItem(null, "Seleccione..."));
        for (Direccion direccion : direccionService.listarDireccionActiva()) {
            String descripcion = direccion.getCalle() + " " + direccion.getNumeracion() + ", " + direccion.getBarrio() + ", " + direccion.getLocalidad().getNombre();
            direcciones.add(new SelectItem(direccion.getId(), descripcion));}   
        }catch (Exception e) {
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }

    // Getters y Setters
    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
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

    public Collection<SelectItem> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<SelectItem> direcciones) {
        this.direcciones = direcciones;
    }
}


