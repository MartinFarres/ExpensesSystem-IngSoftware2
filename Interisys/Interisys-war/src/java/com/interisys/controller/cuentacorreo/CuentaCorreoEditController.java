/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.controller.cuentacorreo;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.CuentaCorreo;
import com.interisys.business.logic.ConsorcioServiceBean;
import com.interisys.business.logic.CuentaCorreoServiceBean;
import com.interisys.controller.enumeration.Message;
import com.interisys.controller.enumeration.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.hibernate.validator.internal.util.logging.Messages;

/**
 *
 * @author spaul
 */
@ManagedBean
@ViewScoped
public class CuentaCorreoEditController {
    
    //Servicios Capa de Negocio
    private @EJB CuentaCorreoServiceBean cuentaCorreoService;
    private @EJB ConsorcioServiceBean consorcioService;

    //Variables Capa de Negocio
    private CuentaCorreo cuentaCorreo;
    private String idConsorcio;
    private String correo;
    private String clave;
    private String direccionSMTP;
    private String puertoSMTP;
    private String tls;
    
    //Variables Vista
    private String casoDeUso;
    private boolean campoDesactivado;
    private Collection<SelectItem> consorcios = new ArrayList();
    private Collection<SelectItem> protocolosTLS = new ArrayList();

    @PostConstruct
    public void init() {
        try{
            
         //Se obtiene el caso de uso  
         casoDeUso = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CASO_DE_USO"));
         cuentaCorreo = ((CuentaCorreo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CUENTACORREO"));
         campoDesactivado = false;
         cargaComboConsorcio();
         cargaComboProtocoloTLS();
         
         //Se asiganan la variables en caso de ser caso de uso CONSULTAR y MODIFICAR
         //En caso de CONSULTA se desactivan los campos para que no puedan ser editados.
         if (casoDeUso.equals("CONSULTAR") || casoDeUso.equals("MODIFICAR")){
           idConsorcio = cuentaCorreo.getConsorcio().getId();
           correo = cuentaCorreo.getCorreo();
           clave = cuentaCorreo.getClave();
           direccionSMTP = cuentaCorreo.getSmtp();
           puertoSMTP = cuentaCorreo.getPuerto();
           tls = cuentaCorreo.isTls() == true ? "SI" : "NO";
                   
           if (casoDeUso.equals("CONSULTAR"))
            campoDesactivado = true;
         }
         
       } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
        }
    }
    
    public String aceptar() {
        
        try{
            
            if (casoDeUso.equals("ALTA")){
              cuentaCorreoService.crearCuentaCorreo(idConsorcio, correo, clave, direccionSMTP, puertoSMTP, tls.equals("SI"));
            }else if (casoDeUso.equals("MODIFICAR")){
              cuentaCorreoService.modificarCuentaCorreo(cuentaCorreo.getId(), idConsorcio, correo, clave, direccionSMTP, puertoSMTP, tls.equals("SI"));
            }
            
            return "listCuentaCorreo";
            
        } catch (Exception e) {
            e.printStackTrace();
            Message.show(e.getMessage(), MessageType.ERROR);
            return null;
        }
    }
    
    public String cancelar() {
        return "listCuentaCorreo";
    }

    public boolean isCampoDesactivado() {
        return campoDesactivado;
    }

    public void setCampoDesactivado(boolean campoDesactivado) {
        this.campoDesactivado = campoDesactivado;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public CuentaCorreo getCuentaCorreo() {
        return cuentaCorreo;
    }

    public void setCuentaCorreo(CuentaCorreo cuentaCorreo) {
        this.cuentaCorreo = cuentaCorreo;
    }

    public String getIdConsorcio() {
        return idConsorcio;
    }

    public void setIdConsorcio(String idConsorcio) {
        this.idConsorcio = idConsorcio;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDireccionSMTP() {
        return direccionSMTP;
    }

    public void setDireccionSMTP(String direccionSMTP) {
        this.direccionSMTP = direccionSMTP;
    }

    public String getPuertoSMTP() {
        return puertoSMTP;
    }

    public void setPuertoSMTP(String puertoSMTP) {
        this.puertoSMTP = puertoSMTP;
    }

    public String getTls() {
        return tls;
    }

    public void setTls(String tls) {
        this.tls = tls;
    }
    
    private void cargaComboProtocoloTLS() {
        
       try{  
           
         protocolosTLS = new ArrayList<>();
         protocolosTLS.add(new SelectItem(null, "Seleccione..."));
         protocolosTLS.add(new SelectItem("SI", "SI"));
         protocolosTLS.add(new SelectItem("NO", "NO"));
        
       }catch(Exception e){
         Message.show(e.getMessage(), MessageType.ERROR); 
       }  
    }

    public Collection<SelectItem> getProtocolosTLS() {
        return protocolosTLS;
    }

    public void setProtocolosTLS(Collection<SelectItem> protocolosTLS) {
        this.protocolosTLS = protocolosTLS;
    }

    public void cargaComboConsorcio(){
      try{  
        
            consorcios = new ArrayList<SelectItem>();
            consorcios.add(new SelectItem(null, "Seleccione..."));
            for(Consorcio consorcio: consorcioService.listarConsorcioActivo()){
              consorcios.add(new SelectItem(consorcio.getId(), consorcio.getNombre()));
            }
                
      }catch(Exception e){
        Message.show(e.getMessage(), MessageType.ERROR);
      }
    }

    public Collection<SelectItem> getConsorcios() {
        return consorcios;
    }

    public void setConsorcios(Collection<SelectItem> consorcios) {
        this.consorcios = consorcios;
    }
    
    
}
