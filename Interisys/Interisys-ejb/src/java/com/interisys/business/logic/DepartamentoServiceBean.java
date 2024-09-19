package com.interisys.business.logic;

import com.interisys.business.domain.entity.Departamento;
import com.interisys.business.domain.entity.Provincia;
import com.interisys.business.persistence.NoResultDAOException;
import com.interisys.business.persistence.DAODepartamentoBean;
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
public class DepartamentoServiceBean {

    private @EJB DAODepartamentoBean dao;
    private @EJB ProvinciaServiceBean provinciaService;

    public void crearDepartamento(String idProvincia, String nombre) throws ErrorServiceException {

        try {
            
            Provincia provincia = provinciaService.buscarProvincia(idProvincia);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarDepartamentoPorProvinciaYNombre(idProvincia, nombre);
                throw new ErrorServiceException("Existe un departamento con el nombre y provicnia indicados");
            } catch (NoResultDAOException ex) {}

            Departamento departamento = new Departamento();
            departamento.setId(UUID.randomUUID().toString());
            departamento.setNombre(nombre);
            departamento.setEliminado(false);
            departamento.setProvincia(provincia);

            dao.guardarDepartamento(departamento);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void modificarDepartamento(String idDepartamento, String idProvincia, String nombre) throws ErrorServiceException {

        try {
            
            Provincia provincia = provinciaService.buscarProvincia(idProvincia);
            Departamento departamento = buscarDepartamento(idDepartamento);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                Departamento departamentoExistente = dao.buscarDepartamentoPorProvinciaYNombre(idProvincia, nombre);
                if (!departamentoExistente.getId().equals(idDepartamento)){
                 throw new ErrorServiceException("Existe un departamento con el nombre y provincia indicado");
                }
            } catch (NoResultDAOException ex) {}

            departamento.setNombre(nombre);
            departamento.setProvincia(provincia);

            dao.actualizarDepartamento(departamento);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public Departamento buscarDepartamento(String id) throws ErrorServiceException {

        try {

            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el departamento");
            }

            Departamento departamento = dao.buscarDepartamento(id);

            if (departamento.isEliminado()) {
                throw new ErrorServiceException("No se encuentra el departamento indicado");
            }

            return departamento;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarDepartamento(String id) throws ErrorServiceException {

        try {

            Departamento departamento = buscarDepartamento(id);
            departamento.setEliminado(true);

            dao.actualizarDepartamento(departamento);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<Departamento> listarDepartamentoActivo() throws ErrorServiceException {
        try {

            return dao.listarDepartamentoActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<Departamento> listarDepartamentoPorProvinciaActivo(String idProvincia) throws ErrorServiceException {
        try {
            
            if (idProvincia == null || idProvincia.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la provincia");
            }
            
            return dao.listarDepartamentoPorProvinciaActivo(idProvincia);

        } catch (ErrorServiceException ex) {  
            throw ex;    
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
