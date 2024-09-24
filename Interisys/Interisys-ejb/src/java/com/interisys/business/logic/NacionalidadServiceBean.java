
package com.interisys.business.logic;

import com.interisys.business.domain.entity.Nacionalidad;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.persistence.DAONacionalidadBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * 
 * 
 * @author aida
 */
@Stateless
@LocalBean
public class NacionalidadServiceBean {
    
    @EJB
    private DAONacionalidadBean dao;
    
 
    public void crearNacionalidad(String nombre) throws ErrorServiceException {

        try {
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarNacionalidadPorNombre(nombre);
                throw new ErrorServiceException("Existe una nacionalidad con el nombre indicado");
            } catch (NoResultDAOException ex) {}

            Nacionalidad nacionalidad = new Nacionalidad();
            nacionalidad.setId(UUID.randomUUID().toString());
            nacionalidad.setNombre(nombre);
            nacionalidad.setEliminado(false);

            dao.guardarNacionalidad(nacionalidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistema");
        }
    }


    public void modificarNacionalidad(String idNacionalidad, String nombre) throws ErrorServiceException {

        try {

            Nacionalidad nacionalidad = buscarNacionalidad(idNacionalidad);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                Nacionalidad nacionalidadExistente = dao.buscarNacionalidadPorNombre(nombre);
                if (!nacionalidadExistente.getId().equals(idNacionalidad)){
                    throw new ErrorServiceException("Existe una nacionalidad con el nombre indicado");
                }
            } catch (NoResultDAOException ex) {}

            nacionalidad.setNombre(nombre);
            nacionalidad.setEliminado(false);
            
            dao.actualizarNacionalidad(nacionalidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistema");
        }
    }


    public Nacionalidad buscarNacionalidad(String id) throws ErrorServiceException {

        try {
            
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la nacionalidad");
            }

            Nacionalidad nacionalidad = dao.buscarNacionalidad(id);
            
            if (nacionalidad.isEliminado()){
                throw new ErrorServiceException("No se encuentra la nacionalidad indicada");
            }

            return nacionalidad;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }


    public void eliminarNacionalidad(String id) throws ErrorServiceException {

        try {

            Nacionalidad nacionalidad = buscarNacionalidad(id);
            nacionalidad.setEliminado(true);
            
            dao.actualizarNacionalidad(nacionalidad);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    public Collection<Nacionalidad> listarNacionalidadActiva() throws ErrorServiceException {
        try {
            
            return dao.listarNacionalidadActiva();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
