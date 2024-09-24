
package com.interisys.business.domain.entity;

import com.interisys.business.domain.enumeration.Sexo;
import com.interisys.business.domain.enumeration.TipoDocumento;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author aidal
 */
//Falta la clase persona
@Entity
public class Inquilino extends Persona implements Serializable {
    
    @ManyToOne
    private Nacionalidad nacionalidad;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;
    
    private Sexo sexo;
    private TipoDocumento tipoDocumento;
    private String documento;

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }


    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
