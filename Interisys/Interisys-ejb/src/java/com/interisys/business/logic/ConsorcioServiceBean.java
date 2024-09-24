package com.interisys.business.logic;

import com.interisys.business.domain.entity.Consorcio;
import com.interisys.business.domain.entity.Direccion;
import com.interisys.business.persistence.DAOConsorcioBean;
import com.interisys.business.persistence.NoResultDAOException;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;

/**
 *
 * @author spaul
 */
public class ConsorcioServiceBean {
    
    @EJB DireccionServiceBean direccionService;
    @EJB DAOConsorcioBean dao;
    
    public void crearConsorcio(Direccion direccion, String nombre) throws ErrorServiceException {

        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (direccion == null) {
                throw new ErrorServiceException("Debe indicar la dirección");
            }
            
            try {
                dao.buscarConsorcioPorNombre(nombre);
                throw new ErrorServiceException("Existe un consorcio con el nombre indicado");
            } catch (NoResultDAOException ex) {}

            Consorcio consorcio = new Consorcio();
            consorcio.setId(UUID.randomUUID().toString());
            consorcio.setNombre(nombre);
            consorcio.setEliminado(false);
            consorcio.setDireccion(direccion);

            dao.guardarConsorcio(consorcio);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public void eliminarConsorcio(String id) throws ErrorServiceException {

        try {

            Consorcio consorcio = buscarConsorcio(id);
            consorcio.setEliminado(true);
            
            dao.actualizarConsorcio(consorcio);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }

    }
    
    public Consorcio buscarConsorcio(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el consorcio");
            }

            Consorcio consorcio = dao.buscarConsorcio(id);
            
            if (consorcio.isEliminado()){
                throw new ErrorServiceException("No se encuentra en consorcio indicado");
            }

            return consorcio;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    
    }
    
    public void modificarConsorcio(String idConsorcio, String nombre, Direccion direccion) throws ErrorServiceException {

        try {

            Consorcio consorcio = buscarConsorcio(idConsorcio);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (direccion == null){
                throw new ErrorServiceException("Debe indicar la dirección");
            }

            try{
                Consorcio consorcioExsitente = dao.buscarConsorcioPorNombre(nombre);
                if (!consorcioExsitente.getId().equals(idConsorcio)){
                  throw new ErrorServiceException("Ya existe un consorcio con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}

            consorcio.setNombre(nombre);
            consorcio.setDireccion(direccion);
            consorcio.setEliminado(false);
            
            dao.actualizarConsorcio(consorcio);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
    
    public Collection<Consorcio> listarConsorcioActivo() throws ErrorServiceException {
        try {
            
            return dao.listarConsorcioActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error del sistema");
        }
    }
}
