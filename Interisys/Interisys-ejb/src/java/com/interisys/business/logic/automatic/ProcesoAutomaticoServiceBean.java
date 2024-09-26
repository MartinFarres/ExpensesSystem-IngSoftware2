/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.logic.automatic;

import com.interisys.business.logic.ExpensaInmuebleServiceBean;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TimerService;

/**
 *
 * @author aidal
 */
@Stateless
@LocalBean
public class ProcesoAutomaticoServiceBean {

    @Resource private TimerService timer;
    private @EJB ExpensaInmuebleServiceBean expensaInmuebleService;
  // Esta anotación define un temporizador que invocará el método procesoAutomatico automáticamente el primer día de cada mes a las 6:00 AM.
    @Schedule(dayOfMonth ="1" ,  hour = "6", minute = "0")
    public void procesoAutomatico(){
        try{  
            
          expensaInmuebleService.crearExpesaInmueble();
          
        } catch (Exception ex){
            ex.printStackTrace();
        } 
    }

    public TimerService getTimer() {
        return timer;
    }

    public void setTimer(TimerService timer) {
        this.timer = timer;
    }   
}
