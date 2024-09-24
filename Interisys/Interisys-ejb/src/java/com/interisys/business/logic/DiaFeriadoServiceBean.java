/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic;

import com.interisys.business.domain.entity.DiaFeriado;
import com.interisys.business.persistence.DAODiaFeriadoBean;
import com.interisys.business.util.UtilFechaBean;
import static com.interisys.business.util.UtilFechaBean.llevarFinDia;
import static com.interisys.business.util.UtilFechaBean.llevarFinMes;
import static com.interisys.business.util.UtilFechaBean.llevarInicioDia;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author aidal
 */
@Stateless
@LocalBean
public class DiaFeriadoServiceBean {
    
    private @EJB DAODiaFeriadoBean dao;

    public void validar(Date dia, String motivo)throws ErrorServiceException {
        
        try{
            
            if (dia == null) {
                throw new ErrorServiceException("Debe indicar el día");
            }

            if (motivo == null || motivo.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el motivo del feriado");
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    public void crearDiaFeriado(Date dia, String motivo) throws ErrorServiceException {

        try {
            
            validar(dia, motivo);
            
            try{
                buscarDiaFeriadoPorFecha(dia);
                throw new ErrorServiceException("Existe un feriado para el día indicado");
            }catch(ErrorServiceException e){
                
              DiaFeriado diaFeriado = new DiaFeriado();
              diaFeriado.setId(UUID.randomUUID().toString());
              diaFeriado.setFecha(dia);
              diaFeriado.setMotivo(motivo);

              dao.guardarDiaFeriado(diaFeriado);
            
            }

        } catch (ErrorServiceException e) {
            throw e; 
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void modificarDiaFeriado(String idDiaFeriado, Date dia, String motivo) throws ErrorServiceException {

        try {
            
            DiaFeriado diaFeriado = buscarDiaFeriado(idDiaFeriado);

            validar(dia, motivo);
            
            DiaFeriado diaAux = buscarDiaFeriadoPorFecha(dia);
            if (!diaAux.getId().equals(idDiaFeriado)){
                throw new ErrorServiceException("Existe un feriado para el día indicado");
            } 

            diaFeriado.setFecha(dia);
            diaFeriado.setMotivo(motivo);

            dao.actualizarDiaFeriado(diaFeriado);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void eliminarDiaFeriado(String idFeriado) throws ErrorServiceException {

        try {

            DiaFeriado diaFeriado = buscarDiaFeriado(idFeriado);
            diaFeriado.setEliminado(true);
            dao.actualizarDiaFeriado(diaFeriado);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public DiaFeriado buscarDiaFeriadoPorFecha(Date fecha) throws ErrorServiceException{

        try {
            
            if(fecha == null){
                throw new ErrorServiceException("Debe indicar una fecha");
            }
            
            return dao.buscarDiaFeriadoPorFecha(fecha);
            
        }catch(ErrorServiceException e){
            throw e;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public DiaFeriado buscarDiaFeriado(String idDiaFeriado) throws ErrorServiceException {

        try {

            if (idDiaFeriado == null || idDiaFeriado.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el día feriado");
            }

            DiaFeriado dia = dao.buscarDiaFeriado(idDiaFeriado);
            
            if(dia.isEliminado()){
               throw new ErrorServiceException("No se encuentra el feriado indicado"); 
            }
        
            return dia;
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<DiaFeriado> listarDiaFeriados(Date fechaDesde, Date fechaHasta) throws ErrorServiceException {
        
        try {
            
            if (fechaDesde == null){
                throw new ErrorServiceException("Debe indicar la fecha desde");
            }
            
            if (fechaHasta == null){
                throw new ErrorServiceException("Debe indicar la fecha hasta");
            }
            
            return dao.listarFeriadoActivo(fechaDesde, fechaHasta);

        } catch (ErrorServiceException e) {
            throw e;    
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<DiaFeriado> listarDiaFeriadosDesde(Date fechaDesde) throws ErrorServiceException {
        
        try {
            
            if (fechaDesde == null){
                throw new ErrorServiceException("Debe indicar la fecha desde");
            }
            
            return dao.listarFeriadosDesdeFecha(fechaDesde);

        } catch (ErrorServiceException e) {
            throw e;     
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<DiaFeriado> listarDiaFeriadoDeAnio(Long anio) throws ErrorServiceException {
        try {
            
            if (anio == null){
              throw new ErrorServiceException("Debe indicar el año");  
            }
            
            Date fechaDesde = llevarInicioDia(UtilFechaBean.convertirAnioMesEnFecha(Long.valueOf(anio + "01")));
            Date fechaHasta = llevarFinDia(llevarFinMes(UtilFechaBean.convertirAnioMesEnFecha(Long.valueOf(anio + "12"))));
            
            return listarDiaFeriados(fechaDesde, fechaHasta);

        } catch (ErrorServiceException e) {
            throw e; 
        } catch (Exception ex) {
             ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public Collection<DiaFeriado> listarDiaFeriadoActivo() throws ErrorServiceException {
        
        try {
            
            return dao.listarFeriadoActivo();

        } catch (Exception ex) {
             ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public boolean esHabil(Date fecha) throws ErrorServiceException {
     
        try {
            
            DiaFeriado diaFeriado = null;
            boolean retorno= false;
           
            try {
              diaFeriado = buscarDiaFeriadoPorFecha(fecha);
            } catch (ErrorServiceException e){  
            } catch (Exception ex) {
              throw new ErrorServiceException(ex.getMessage());
            }
        
            String dia = fecha.toString().substring(0, 3);
            if (!(dia.equals("Sat")) && (!(dia.equals("Sun"))) && diaFeriado == null) {
             return true;
            }

            return retorno;
            
        } catch (Exception ex) {
             ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
