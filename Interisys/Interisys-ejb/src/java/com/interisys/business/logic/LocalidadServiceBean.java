package com.interisys.business.logic;

import com.interisys.business.domain.entity.Departamento;
import com.interisys.business.domain.entity.Localidad;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.persistence.DAOLocalidadBean;
import com.interisys.business.logic.DepartamentoServiceBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author spaul
 */
@Stateless
@LocalBean
public class LocalidadServiceBean {

    private @EJB DepartamentoServiceBean departamentoService;
    private @EJB DAOLocalidadBean dao;
    
    public void crearLocalidad(String idDepartamento, String nombre, String codigoPostal) throws ErrorServiceException {

        try {
            
            Departamento departamento = departamentoService.buscarDepartamento(idDepartamento);
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (codigoPostal == null || codigoPostal.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el código Postal");
            }

            try {
                dao.buscarLocalidadPorDepartamentoYNombre(idDepartamento, nombre);
                throw new ErrorServiceException("Existe un localidad con el nombre y departamento indicados");
            } catch (NoResultDAOException ex) {}

            Localidad localidad = new Localidad();
            localidad.setId(UUID.randomUUID().toString());
            localidad.setDepartamento(departamento);
            localidad.setNombre(nombre);
            localidad.setCodigoPostal(codigoPostal);
            localidad.setEliminado(false);

            dao.guardarLocalidad(localidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void modificarLocalidad(String idLocalidad, String idDepartamento, String nombre, String codigoPostal) throws ErrorServiceException {

        try {
            
            Departamento departamento = departamentoService.buscarDepartamento(idDepartamento);
            Localidad localidad = buscarLocalidad(idLocalidad);
           
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (codigoPostal == null || codigoPostal.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el código Postal");
            }

            try {
                Localidad localidadExistente = dao.buscarLocalidadPorDepartamentoYNombre(idDepartamento, nombre);
                if (!localidadExistente.getId().equals(idLocalidad)){
                 throw new ErrorServiceException("Existe un localidad con el nombre y departamento indicado");
                }
            } catch (NoResultDAOException ex) {}

            localidad.setDepartamento(departamento);
            localidad.setNombre(nombre);
            localidad.setCodigoPostal(codigoPostal);

            dao.guardarLocalidad(localidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void eliminarLocalidad(String idLocalidad) throws ErrorServiceException {

        try {
            
            Localidad localidad = buscarLocalidad(idLocalidad);
            localidad.setEliminado(true);
            dao.actualizarLocalidad(localidad);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Localidad buscarLocalidad(String idLocalidad) throws ErrorServiceException {

        try {
            
            if (idLocalidad == null) {
                throw new ErrorServiceException("Debe indicar la localidad");
            }

            Localidad localidad = dao.buscarLocalidad(idLocalidad);
            
            if (localidad.isEliminado()){
                throw new ErrorServiceException("No se encuentra la localidad indicada");
            }

            return localidad;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Localidad> listarLocalidadActivo() throws ErrorServiceException {
        try {
            
            return dao.listarLocalidadActiva();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Localidad> listarLocalidadActiva(String idDepartamento) throws ErrorServiceException {
        try {
            
            if (idDepartamento == null || idDepartamento.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el departamento");
            }
            
            return dao.listarLocalidadActiva(idDepartamento);

        } catch (ErrorServiceException ex) {  
            throw ex;    
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}

