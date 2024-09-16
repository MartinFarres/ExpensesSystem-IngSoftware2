package test;

import com.interisys.business.domain.entity.Pais;
import com.interisys.business.logic.ErrorServiceException;
import com.interisys.business.logic.PaisServiceBean;


import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean(name = "paisController")
@SessionScoped
public class PaisController implements Serializable {

    private Pais pais;

    @PostConstruct
    public void init() {
        pais = new Pais();
    }
    
    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
    
    //Servicios Capa de Negocio
    private @EJB PaisServiceBean paisService;
    
    public void crear() throws ErrorServiceException
    {
        paisService.crearPais(pais.getNombre());
    }
    
    public void eliminar() throws ErrorServiceException
    {
        paisService.eliminarPais(pais.getId());
    }

}
