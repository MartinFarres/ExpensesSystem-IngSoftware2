package com.interisys.business.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author spaul
 */

@Entity
public class CuentaCorreo {
    @Id 
    private String id;
    private String correo;
    private String clave;
    private String puerto;
    
    private String smtp; //Simple Mail Transfer Protocol 
    /*SMTP protocolo estándar utilizado para el envío de correos electrónicos a través de 
    Internet. Es responsable de la transferencia de mensajes desde el cliente de correo 
    electrónico al servidor de correo saliente y entre servidores de correo.*/
    
    private boolean tls; //Transport Layer Security
    /*TLS protocolo de seguridad que encripta la comunicación entre el cliente y el servidor 
    de correo electrónico. 
    cuando tls = true, Indica si se debe utilizar una conexión segura mediante TLS*/

    private boolean eliminado;
    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Consorcio consorcio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Consorcio getConsorcio() {
        return consorcio;
    }

    public void setConsorcio(Consorcio consorcio) {
        this.consorcio = consorcio;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CuentaCorreo)) {
            return false;
        }
        CuentaCorreo other = (CuentaCorreo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.interisys.business.domain.entity.CuentaCorreo[ id=" + id + " ]";
    }
    
}
