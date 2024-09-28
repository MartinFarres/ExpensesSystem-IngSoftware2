/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.departamento;

import com.interisys.business.domain.entity.Departamento; // Cambiado
import com.interisys.business.logic.DepartamentoServiceBean; // Cambiado
import com.interisys.controller.enumeration.Message;
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
 * @author spaul
 */

@ManagedBean
@ViewScoped
public class DepartamentoListController { // Cambiado
    
    private @EJB DepartamentoServiceBean departamentoService; // Cambiado
    
    private Collection<Departamento> departamentos = new ArrayList(); // Cambiado

    @PostConstruct
    public void init() {
        try {
            listarDepartamento(); // Cambiado
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public void listarDepartamento() { // Cambiado
        try {  
            departamentos.clear(); // Cambiado
            departamentos.addAll(departamentoService.listarDepartamentoActivo()); // Cambiado
            
            RequestContext.getCurrentInstance().update("formPpal:panelTablaDepartamento"); // Cambiado
            RequestContext.getCurrentInstance().update("formPpal:tablaDepartamento"); // Cambiado
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }  
    }
    
    public String alta() {
        try {
            guardarDepartamentoSession(CasoDeUsoType.ALTA, null); // Cambiado
            return "editDepartamento"; // Cambiado
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String consultar(Departamento departamento) { // Cambiado
        try {
            guardarDepartamentoSession(CasoDeUsoType.CONSULTAR, departamento); // Cambiado
            return "editDepartamento"; // Cambiado
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String modificar(Departamento departamento) { // Cambiado
        try {
            guardarDepartamentoSession(CasoDeUsoType.MODIFICAR, departamento); // Cambiado
            return "editDepartamento"; // Cambiado
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
            
    public String baja(Departamento departamento) { // Cambiado
        try {
            departamentoService.eliminarDepartamento(departamento.getId()); // Cambiado
            listarDepartamento(); // Cambiado
            Message.show("La baja se realizó correctamente", MessageType.NOTIFICACION);
            RequestContext.getCurrentInstance().update("formPpal:msj");
            return "listDepartamento"; // Cambiado
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }

    public DepartamentoServiceBean getDepartamentoService() { // Cambiado
        return departamentoService; // Cambiado
    }

    public void setDepartamentoService(DepartamentoServiceBean departamentoService) { // Cambiado
        this.departamentoService = departamentoService; // Cambiado
    }

    public Collection<Departamento> getDepartamentos() { // Cambiado
        return departamentos; // Cambiado
    }

    public void setDepartamentos(Collection<Departamento> departamentos) { // Cambiado
        this.departamentos = departamentos; // Cambiado
    }

    private void guardarDepartamentoSession(CasoDeUsoType casoDeUso, Departamento departamento) { // Cambiado
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("CASO_DE_USO", casoDeUso);  
        session.setAttribute("DEPARTAMENTO", departamento); // Cambiado
    }
}
