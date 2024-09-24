/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interisys.business.util;

import com.interisys.business.domain.entity.DiaFeriado;
import com.interisys.business.logic.DiaFeriadoServiceBean;
import com.interisys.business.logic.ErrorServiceException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author aidal
 */
@Stateless
@LocalBean
public class UtilFechaBean {
    
    private static final String FORMATO_CORTO_FECHA = "dd/MM/yyyy";
    private static final String FORMATO_LARGO_FECHA = "EEEEE', 'dd' de 'MMMMM' de 'yyyy";
    private static final String FORMATO_CORTO_HORA = "hh:mm";
    private static final String FORMATO_LARGO_HORA = "hh:mm:ss";
    private static Locale locale = new Locale("es", "AR");
    private static DateFormat df = DateFormat.getDateInstance(2, locale);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
    private static SimpleDateFormat sdfHora = new SimpleDateFormat("H", locale);
    private static SimpleDateFormat sdfMes = new SimpleDateFormat("M", locale);
    private static DecimalFormat df1 = new DecimalFormat("00");
    
    private @EJB DiaFeriadoServiceBean diaFeriadoService;
    
    public static Date llevarFinDia(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.MILLISECOND, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        return calendario.getTime();
    }
    
    public static Date llevarFinMes(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        int ultimoDia = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendario.set(Calendar.DAY_OF_MONTH, ultimoDia);
        calendario.set(Calendar.MILLISECOND, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        return calendario.getTime();
    }
    
    public static Date llevarInicioMes(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.MILLISECOND, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        return calendario.getTime();
    }

    /**
     * Devuelve un Date con la fecha (dias corridos )resultante de la suma entre
     * la cantidad de dias a agregar y la fecha inicial.
     *
     * @param Date fecha fecha desde la que se empieza a contar
     * @param int dias cantidad de dias a contar
     * @return Date fecha resultante del conteo
     */
    public static Date agregaDiasAFecha(Date fecha, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.add(Calendar.DATE, dias);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Valida si un anio es viciesto
     *
     * @param anio a consultar
     * @return true si es viciesto
     */
    public static boolean esViciesto(long anio) {
        return (anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0));
    }

    /**
     * Calcula la fecha habil inmedita posterior de una fecha
     *
     * @param fecha a consultar
     * @return la fecha habil inmediata posterior
     */
    public Date calculaFechaHabilInmeditaPosterior(Date fecha) throws ErrorServiceException {

        while (!diaFeriadoService.esHabil(fecha)) {
            fecha = agregaDiasAFecha(fecha, 1);
        }

        return fecha;
    }

    /**
     * Calcula la cantidad de dias habiles existentes entre dos fechas
     *
     * @param anio a consultar
     * @return true si es viciesto
     */
    public int calculaDiasHabiles(Date fechaInicial, Date fechaFinal) throws ErrorServiceException {

        Date aux = fechaInicial;
        int cuentaFinDeSemanas = 0;
        int cuentaFeriados = 0;

        //Cuenta dias corridos descontando los fin de
        //semana
        while (aux.before(fechaFinal) || aux.equals(fechaFinal)) {

            String dia = aux.toString().substring(0, 3);

            //Suma un dia
            aux = agregaDiasAFecha(aux, 1);

            //Si no es fin de semana
            if ((dia.equals("Sat") || dia.equals("Sun"))) {
                cuentaFinDeSemanas++;
            }
        }

        //Obtengo los feriados, en caso que existan entre
        //las dos fechas.
        Collection<DiaFeriado> feriados = diaFeriadoService.listarDiaFeriados(fechaInicial, fechaFinal);

        //Descarto los feriados caidos en fin de semana
        for (DiaFeriado feriado : feriados) {
            String dia = feriado.getFecha().toString().substring(0, 3);
            //Si es distinto de sabado y domingo
            if ((!dia.equals("Sat") && !dia.equals("Sun"))) {
                cuentaFeriados++;
            }
        }

        return calculaDias(fechaInicial, fechaFinal) - (cuentaFinDeSemanas + cuentaFeriados);
    }

    public Date sumarDiasHabilesAFecha(Date fecha, int cantidadDias) throws ErrorServiceException {

        Date fechaRetorno = fecha;
        Date fechaAux = fecha;
        int contador = 1;
        if (cantidadDias != 0){
         while (true) {
            fechaAux = agregaDiasAFecha(fechaAux, 1);
            if (diaFeriadoService.esHabil(fechaAux)) {
                fechaRetorno = fechaAux;
                if (contador == cantidadDias) {
                    break;
                }
                contador++;
            }
         }
        } 

        return fechaRetorno;
    }
    
    public Date restarDiasHabilesAFecha(Date fecha, int cantidadDias) throws ErrorServiceException {

        Date fechaRetorno = fecha;
        Date fechaAux = fecha;
        int contador = 1;
        if (cantidadDias != 0){
         while (true) {
            fechaAux = restarDiasAFecha(fechaAux, 1);
            if (diaFeriadoService.esHabil(fechaAux)) {
                fechaRetorno = fechaAux;
                if (contador == cantidadDias) {
                    break;
                }
                contador++;
            }
         }
        } 

        return fechaRetorno;
    }
    

    /**
     * Calcula la cantidad de dias que existe entre dos fechas dadas.
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve la cantidad de dias transcurridos entre la fecha inicial
     * y final.
     */
    public static int calculaDias(Date fechaInicial, Date fechaFinal) {

        long dia = 0;

        //los milisegundos
        long diferenciaMils = fechaFinal.getTime() - fechaInicial.getTime();

        //obtenemos los segundos
        long segundos = diferenciaMils / 1000;

        //obtenemos los minutos
        long minutos = segundos / 60;

        //obtenemos los minutos
        dia = minutos / 1440;

        dia = Math.abs(dia);

        return (int) dia;

    }

    /**
     * Determina si la primer fecha es menor que la segunda
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve verdadero si la fecha inicial es menor que la fecha
     * final
     */
    public static boolean fechaEsMenor(Date fechaInicio, Date fechaFin) {
        return estaAntes(fechaInicio, fechaFin);
    }

    /**
     * Determina si la primer fecha es menor que la segunda
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve verdadero si la fecha inicial es menor que la fecha
     * final
     */
    public static boolean fechaEsMenorIgual(Date fechaInicio, Date fechaFin) {
        return comparar(fechaInicio, fechaFin) <= 0;
    }

    /**
     * Determina si la primer fecha es menor que la segunda
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve verdadero si la fecha inicial es menor que la fecha
     * final
     */
    public static boolean fechaEsMayor(Date fechaInicio, Date fechaFin) {
        return estaDespues(fechaInicio, fechaFin);
    }

    /**
     * Determina si la primer fecha es menor que la segunda
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve verdadero si la fecha inicial es menor que la fecha
     * final
     */
    public static boolean fechaEsMayorIgual(Date fechaInicio, Date fechaFin) {
        return comparar(fechaInicio, fechaFin) >= 0;
    }

    /**
     * Determina si la primer fecha es menor que la segunda
     *
     * @param fechaInicial Date con la fecha y hora inicial
     * @param fechaFinal Date con la fecha y hora Final
     * @return Devuelve verdadero si la fecha inicial es menor que la fecha
     * final
     */
    public static boolean fechaEsIgual(Date fechaInicio, Date fechaFin) {
        return comparar(fechaInicio, fechaFin) == 0;
    }

    public static Date diaConHoraMinutoSegundo(Date fch, int hora, int minuto, int segundos) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fch);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, segundos);
        return cal.getTime();
    }

    public static synchronized java.util.Date getDateString(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }
    
    public static synchronized java.util.Date getDateString(String fechaString, String formato) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }
    
    public static synchronized java.util.Date getDateStringDiaMesAnioConGuionEspacioHoraMinutoSeparadoPorDosPuntos(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy hh:ss");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }
    
    public static synchronized java.util.Date getDateStringAnioMesDiaConGuionEspacioHoraMinutoSeparadoPorDosPuntos(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }

    public static String convertirFechaConBarraDiaMesAnioAFechaConGuionAnioMesDia(String fechaString) throws ErrorServiceException {
        try {

            String dia = fechaString.substring(0, 2);
            String mes = fechaString.substring(3, 5);
            String anio = fechaString.substring(6, fechaString.length());

            if (dia.length() == 1) {
                dia = "0".concat(dia);
            }

            if (mes.length() == 1) {
                mes = "0".concat(mes);
            }

            return anio.concat("-").concat(mes).concat("-").concat(dia);

        } catch (Exception e) {
            throw new ErrorServiceException("Eror al tratatar de convertir fecha");
        }
    }

    public static synchronized java.util.Date getDateStringConBarra(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }
    
    public static synchronized java.util.Date getDateStringConGuion(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }
    
    public static synchronized java.util.Date getDateStringAnioMesDiaConGuion(String fechaString) throws ErrorServiceException {
        try {

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatoDelTexto.parse(fechaString);
            return fecha;

        } catch (ParseException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        } catch (NullPointerException e) {
            throw new ErrorServiceException("El formato de la fecha no es adecuado");
        }
    }

    public static synchronized java.util.Date getUtilDate(String fecha)
            throws ParseException {
        try {
            return df.parse(fecha);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static synchronized String getStringDate(java.util.Date fecha) {
        try {
            return sdf.format(fecha);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static Date inicioDia(Date fecha) {
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.setTime(fecha);
        calendario.set(11, 0);
        calendario.set(12, 0);
        calendario.set(13, 0);
        calendario.set(14, 0);
        return calendario.getTime();
    }

    public static long diferenciaDias(Date fechaInicial, Date fechaFinal) {
        long dias = Math.round(Math.abs((inicioDia(fechaFinal).getTime() - inicioDia(fechaInicial).getTime()) / 0x5265c00L));
        return dias;
    }

    public static String getFechaCorta() {
        return getFechaCorta(0);
    }

    public static String getFechaCorta(int desplazamiento) {
        return (new SimpleDateFormat("dd/MM/yyyy")).format(getFecha(desplazamiento));
    }

    public static String getFechaLarga() {
        return getFechaLarga(0);
    }

    public static String getFechaLarga(int desplazamiento) {
        String fecha = (new SimpleDateFormat("EEEEE', 'dd' de 'MMMMM' de 'yyyy")).format(getFecha(desplazamiento));
        fecha = (new StringBuilder()).append(Character.toUpperCase(fecha.charAt(0))).append(fecha.substring(1)).toString();
        return fecha;
    }

    public static String getHoraCorta() {
        return getHoraCorta(0);
    }

    public static String getHoraCorta(int desplazamiento) {
        return (new SimpleDateFormat("hh:mm")).format(getHora(desplazamiento));
    }

    public static String getHoraLarga() {
        return getHoraLarga(0);
    }

    public static String getHoraLarga(int desplazamiento) {
        return (new SimpleDateFormat("hh:mm:ss")).format(getHora(desplazamiento));
    }

    public static Date getFecha() {
        return getFecha(0);
    }

    public static Date getFecha(int desplazamiento) {
        return getFecha(new Date(), desplazamiento);
    }

    public static Date getFecha(Date fechaBase, int desplazamiento) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fechaBase);
        calendario.add(5, desplazamiento);
        return calendario.getTime();
    }

    public static Date getHora() {
        return getHora(0);
    }

    public static Date getHora(int desplazamiento) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.add(14, desplazamiento);
        return calendario.getTime();
    }

    public static int comparar(Date fecha1, Date fecha2) {
        return fecha1.compareTo(fecha2);
    }

    public static boolean estaAntes(Date fecha1, Date fecha2) {
        return comparar(fecha1, fecha2) < 0;
    }

    public static boolean estaDespues(Date fecha1, Date fecha2) {
        return comparar(fecha1, fecha2) > 0;
    }

    public static int compararConFechaActual(Date fecha) {
        return comparar(fecha, inicioDia(new Date()));
    }

    public static boolean estaAntesQueFechaActual(Date fecha) {
        return estaAntes(inicioDia(fecha), inicioDia(new Date()));
    }

    public static boolean estaDespuesQueFechaActual(Date fecha) {
        return estaDespues(inicioDia(fecha), inicioDia(new Date()));
    }

    public static boolean entre(Date fechaInicio, Date fechaFin) {
        return entre(new Date(), fechaInicio, fechaFin);
    }

    public static boolean entre(Date fechaAEvaluar, Date fechaInicio, Date fechaFin) {
        if (fechaAEvaluar == null) {
            throw new NullPointerException();
        }
        if (fechaInicio == null && fechaFin == null) {
            throw new NullPointerException();
        }
        if (fechaInicio == null) {
            return comparar(inicioDia(fechaAEvaluar), inicioDia(fechaFin)) <= 0;
        }
        if (fechaFin == null) {
            return comparar(inicioDia(fechaInicio), inicioDia(fechaAEvaluar)) <= 0;
        } else {
            return comparar(inicioDia(fechaInicio), inicioDia(fechaAEvaluar)) <= 0 && comparar(inicioDia(fechaAEvaluar), inicioDia(fechaFin)) <= 0;
        }
    }

    public static boolean entreConHoraYMinutos(Date fechaAEvaluar, Date fechaInicio, Date fechaFin) {
        
        if (fechaAEvaluar == null) {
            throw new NullPointerException();
        }
        if (fechaInicio == null && fechaFin == null) {
            throw new NullPointerException();
        }
        if (fechaInicio == null) {
            return comparar(fechaAEvaluar, fechaFin) <= 0;
        }
        if (fechaFin == null) {
            return comparar(fechaInicio, fechaAEvaluar) <= 0;
        } else {
            return comparar(fechaInicio, fechaAEvaluar) <= 0 && comparar(fechaAEvaluar, fechaFin) <= 0;
        }
    }
    
    public static Date desplazarFechaActualEnSegundos(int cantidad) {
        return desplazarEnSegundos(new Date(), cantidad);
    }

    public static Date desplazarEnSegundos(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(13, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnMinutos(int cantidad) {
        return desplazarEnMinutos(new Date(), cantidad);
    }

    public static Date desplazarEnMinutos(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(12, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnHoras(int cantidad) {
        return desplazarEnHoras(new Date(), cantidad);
    }

    public static Date desplazarEnHoras(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(11, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnDias(int cantidad) {
        return desplazarEnDias(new Date(), cantidad);
    }

    public static Date desplazarEnDias(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(5, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnSemanas(int cantidad) {
        return desplazarEnSemanas(new Date(), cantidad);
    }

    public static Date desplazarEnSemanas(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(3, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnMeses(int cantidad) {
        return desplazarEnMeses(new Date(), cantidad);
    }

    public static Date desplazarEnMeses(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(2, cantidad);
        return calendario.getTime();
    }

    public static Date desplazarFechaActualEnAnios(int cantidad) {
        return desplazarEnAnios(new Date(), cantidad);
    }

    public static Date desplazarEnAnios(Date fecha, int cantidad) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        calendario.add(1, cantidad);
        return calendario.getTime();
    }

    public static int getDiaSemana(Date fecha) {
        Calendar calendario = Calendar.getInstance(Locale.getDefault());
        calendario.setTime(fecha);
        return calendario.get(7);
    }

    public static Date restarDiasAFecha(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        return new Date(cal.getTimeInMillis());
    }

    public static Date restarUnAnioAFecha(Date fch) {

        Calendar cal = new GregorianCalendar();
        int dias = 365;
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        return new Date(cal.getTimeInMillis());
    }

    public static Date agregaDiasHorasMinutosAFecha(Date fch, int dias, int hora, int minuto) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        cal.add(Calendar.HOUR, hora);
        cal.add(Calendar.MINUTE, minuto);
        return new Date(cal.getTimeInMillis());
    }

    public static Date agregaHorasMinutosAFecha(Date fch, int hora, int minuto) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fch);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        return cal.getTime();
    }
    
    public static Date sumarMinutosAFecha(Date fch, int minuto) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fch);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+ minuto);
        return cal.getTime();
    }
    
    public static Date restarMinutosAFecha(Date fch, int minuto) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fch);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)- minuto);
        return cal.getTime();
    }

    public static Date finDia(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        return calendario.getTime();
    }

    public static String dateStringConBarra(Date fecha) {
        return getDiaString(fecha) + "/" + getMesString(fecha) + "/" + getAnioString(fecha);
    }

    public static String dateStringConGuion(Date fecha) {
        return getAnioString(fecha) + "-" + getMesString(fecha) + "-" + getDiaString(fecha);
    }

    public static String dateStringAnioMesDia(Date fecha) {
        return getAnioString(fecha) + getMesString(fecha) + getDiaString(fecha);
    }
    
    public static Long anioLongDeDate(Date fecha) {
        return Long.valueOf(getAnioString(fecha));
    }

    public static String getDiaString(Date fecha) {
        String fec = fecha.getDate() + "";
        if (fec.length() < 2) {
            return 0 + fec;
        }
        return fec;
    }

    public static String getMesString(Date fecha) {
        if ((fecha.getMonth() + 1 + "").length() < 2) {
            return "0" + (fecha.getMonth() + 1 + "");
        } else {
            return fecha.getMonth() + 1 + "";
        }
    }
    
    public static String getMesAnteriorString(Date fecha) {
        if ((fecha.getMonth() + 1 -1 + "").length() < 2) {
            return "0" + (fecha.getMonth() + 1-1  + "");
        } else {
            return fecha.getMonth() + 1 -1 + "";
        }
    }

    public static String getAnioString(Date fecha) {
        return (fecha.getYear() + 1900) + "";
    }
    
    public static String getAnioAnteriorString(Date fecha) {
        return (fecha.getYear() + 1900) - 1 + "";
    }

    public static java.sql.Time horaParseToTime(String hora) {
        return (new java.sql.Time(obtenerHoraInt(hora), obtenerMinutoInt(hora), obtenerSegundoInt(hora)));
    }

    public static String timeParseToString(java.sql.Time hora) {
        return (hora.toString().charAt(0) + "" + hora.toString().charAt(1) + ":" + hora.toString().charAt(3) + "" + hora.toString().charAt(4));
    }

    public static boolean horaMinutosSegundoStringEstaEntre(String horaEvaluar, String horaInicio, String horaFin) {

        boolean retorno = false;

        if (horaMinutosSegundoStringEsMenorIgual(horaEvaluar, horaFin)) {
            if (horaMinutosSegundoStringEsMayorIgual(horaEvaluar, horaInicio)) {
                return true;
            }
        }

        return retorno;
    }

    public static boolean horaMinutosSegundoStringEsMenor(String horaInicio, String horaFin) {
        if (horaStringEsMenor(obtenerHoraString(horaInicio), obtenerHoraString(horaFin)) && minutoStringEsMenor(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin)) && segundoStringEsMenor(obtenerSegundoString(horaInicio), obtenerSegundoString(horaFin))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean horaMinutosSegundoStringEsMenorIgual(String horaInicio, String horaFin) {
        boolean retorno = false;

        if (horaStringEsMenor(obtenerHoraString(horaInicio), obtenerHoraString(horaFin))) {
            retorno = true;
        } else {

            if (horaStringEsIgual(obtenerHoraString(horaInicio), obtenerHoraString(horaFin))) {
                if (minutoStringEsMenor(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin))) {
                    retorno = true;
                } else {

                    if (minutoStringEsIgual(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin))) {
                        if (segundoStringEsMenor(obtenerSegundoString(horaInicio), obtenerSegundoString(horaFin))) {
                            retorno = true;
                        }
                    }
                }
            }
        }
        return retorno;
    }

    public static boolean horaMinutosSegundoStringEsMayor(String horaInicio, String horaFin) {

        boolean retorno = false;

        if (horaStringEsMayor(obtenerHoraString(horaInicio), obtenerHoraString(horaFin))) {
            retorno = true;
        } else {

            if (horaStringEsIgual(obtenerHoraString(horaInicio), obtenerHoraString(horaFin))) {
                if (minutoStringEsMayor(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin))) {
                    retorno = true;
                } else {

                    if (minutoStringEsIgual(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin))) {
                        if (segundoStringEsMayor(obtenerSegundoString(horaInicio), obtenerSegundoString(horaFin))) {
                            retorno = true;
                        }
                    }
                }
            }
        }
        return retorno;

    }

    public static boolean horaMinutosSegundoStringEsMayorIgual(String horaInicio, String horaFin) {
        boolean retorno = false;

        if (horaMinutosSegundoStringEsIgual(horaInicio, horaFin)) {
            return true;
        } else {
            if (!horaMinutosSegundoStringEsMenor(horaInicio, horaFin)) {
                return true;
            }
        }

        return retorno;
    }

    public static boolean horaMinutosSegundoStringEsIgual(String horaInicio, String horaFin) {
        if (horaStringEsIgual(obtenerHoraString(horaInicio), obtenerHoraString(horaFin)) && minutoStringEsIgual(obtenerMinutoString(horaInicio), obtenerMinutoString(horaFin)) && segundoStringEsIgual(obtenerSegundoString(horaInicio), obtenerSegundoString(horaFin))) {
            return true;
        } else {
            return false;
        }
    }

    public static int obtenerHoraInt(String hora) {
        try {
            return Integer.parseInt(hora.charAt(0) + "" + hora.charAt(1));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int obtenerMinutoInt(String hora) {
        try {
            return Integer.parseInt(hora.charAt(3) + "" + hora.charAt(4));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int obtenerSegundoInt(String hora) {

        try {
            return Integer.parseInt(hora.charAt(6) + "" + hora.charAt(7));
        } catch (Exception e) {
            return 0;
        }
    }

    public static String obtenerHoraString(String hora) {
        try {
            return hora.charAt(0) + "" + hora.charAt(1);
        } catch (Exception e) {
            return "00";
        }
    }

    public static String obtenerMinutoString(String hora) {
        try {
            return hora.charAt(3) + "" + hora.charAt(4);
        } catch (Exception e) {
            return "00";
        }
    }

    public static String obtenerSegundoString(String hora) {

        try {
            return hora.charAt(6) + "" + hora.charAt(7);
        } catch (Exception e) {
            return "00";
        }
    }

    public static boolean horaStringEsMenor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) < Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean horaStringEsMenorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) <= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean horaStringEsMayor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) > Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean horaStringEsMayorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) >= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean horaStringEsIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean minutoStringEsMenor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) < Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean minutoStringEsMenorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) <= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean minutoStringEsMayor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) > Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean minutoStringEsMayorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) >= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean minutoStringEsIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean segundoStringEsMenor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) < Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean segundoStringEsMenorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) <= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean segundoStringEsMayor(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) > Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean segundoStringEsMayorIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) >= Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean segundoStringEsIgual(String horaInicio, String horaFin) {
        if (Integer.parseInt(horaInicio) == Integer.parseInt(horaFin)) {
            return true;
        } else {
            return false;
        }
    }

    public static int obtenerAnioDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR);
    }

    public static int obtenerMesDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.MONTH);
    }

    public static int obtenerDiaDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int obtenerDiaDeLaSemanaDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int obtenerHoraDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.HOUR_OF_DAY) - 1;
    }

    public static int obtenerMinutoDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.MINUTE);
    }

    public static int obtenerSegundoDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.SECOND);
    }

    public static String obtenerDiaMes(Date fecha){
      int dia = obtenerDiaDate(fecha);
      int mes = obtenerMesDate(fecha);
      
      return String.valueOf(dia) + String.valueOf(mes);
    }
    
    public static int obtenerCantidadDiasDelMes(Mes mes, long anio) {

        int retorno = 0;
        if (Mes.ENERO == mes) {
            retorno = 31;
        }
        if (Mes.FEBRERO == mes) {
            if (esViciesto(anio)) {
                retorno = 29;
            } else {
                retorno = 28;
            }
        }
        if (Mes.MARZO == mes) {
            retorno = 31;
        }
        if (Mes.ABRIL == mes) {
            retorno = 30;
        }
        if (Mes.MAYO == mes) {
            retorno = 31;
        }
        if (Mes.JUNIO == mes) {
            retorno = 30;
        }
        if (Mes.JULIO == mes) {
            retorno = 31;
        }
        if (Mes.AGOSTO == mes) {
            retorno = 31;
        }
        if (Mes.SEPTIEMBRE == mes) {
            retorno = 30;
        }
        if (Mes.OCTUBRE == mes) {
            retorno = 31;
        }
        if (Mes.NOVIEMBRE == mes) {
            retorno = 30;
        }
        if (Mes.DICIEMBRE == mes) {
            retorno = 31;
        }

        return retorno;

    }

    public static String obtenerNumeroMes(Mes mes) {

        String retorno = null;
        if (Mes.ENERO == mes) {
            retorno = "01";
        }
        if (Mes.FEBRERO == mes) {
            retorno = "02";
        }
        if (Mes.MARZO == mes) {
            retorno = "03";
        }
        if (Mes.ABRIL == mes) {
            retorno = "04";
        }
        if (Mes.MAYO == mes) {
            retorno = "05";
        }
        if (Mes.JUNIO == mes) {
            retorno = "06";
        }
        if (Mes.JULIO == mes) {
            retorno = "07";
        }
        if (Mes.AGOSTO == mes) {
            retorno = "08";
        }
        if (Mes.SEPTIEMBRE == mes) {
            retorno = "09";
        }
        if (Mes.OCTUBRE == mes) {
            retorno = "10";
        }
        if (Mes.NOVIEMBRE == mes) {
            retorno = "11";
        }
        if (Mes.DICIEMBRE == mes) {
            retorno = "12";
        }

        return retorno;
    }

    public static Mes obtenerMes(int mes) {

        Mes retorno = null;
        if (1 == mes) {
            retorno = Mes.ENERO;
        }
        if (2 == mes) {
            retorno = Mes.FEBRERO;
        }
        if (3 == mes) {
            retorno = Mes.MARZO;
        }
        if (4 == mes) {
            retorno = Mes.ABRIL;
        }
        if (5 == mes) {
            retorno = Mes.MAYO;
        }
        if (6 == mes) {
            retorno = Mes.JUNIO;
        }
        if (7 == mes) {
            retorno = Mes.JULIO;
        }
        if (8 == mes) {
            retorno = Mes.AGOSTO;
        }
        if (9 == mes) {
            retorno = Mes.SEPTIEMBRE;
        }
        if (10 == mes) {
            retorno = Mes.OCTUBRE;
        }
        if (11 == mes) {
            retorno = Mes.NOVIEMBRE;
        }
        if (12 == mes) {
            retorno = Mes.DICIEMBRE;
        }

        return retorno;
    }

    public static String obtenerHoraMinutoSegundoDate(Date fecha) {

        int hora, minuto, segundo = 0;
        String retorno = null;

        //Obtengo la hora
        hora = obtenerHoraDate(fecha);
        if (hora > 9) {
            retorno = String.valueOf(hora) + ":";
        } else {
            retorno = "0" + String.valueOf(hora) + ":";
        }

        //Obtengo los minutos
        minuto = obtenerMinutoDate(fecha);
        if (minuto > 9) {
            retorno = retorno + String.valueOf(minuto) + ":";
        } else {
            retorno = retorno + "0" + String.valueOf(minuto) + ":";
        }

        //Obtengo los segundos
        segundo = obtenerSegundoDate(fecha);
        if (segundo > 9) {
            retorno = retorno + String.valueOf(segundo);
        } else {
            retorno = retorno + "0" + String.valueOf(segundo);
        }

        return retorno;
    }

    public static String obtenerHoraMinutoSegundoInt(int hora, int minuto, int segundo) {

        String retorno = null;

        //Obtengo la hora
        if (hora > 9) {
            retorno = String.valueOf(hora) + ":";
        } else {
            retorno = "0" + String.valueOf(hora) + ":";
        }

        //Obtengo los minutos
        if (minuto > 9) {
            retorno = retorno + String.valueOf(minuto) + ":";
        } else {
            retorno = retorno + "0" + String.valueOf(minuto) + ":";
        }

        //Obtengo los segundos
        if (segundo > 9) {
            retorno = retorno + String.valueOf(segundo);
        } else {
            retorno = retorno + "0" + String.valueOf(segundo);
        }

        return retorno;
    }

    public Date calcularQuincenaMasProxima() throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(new Date());
            int mesActual = obtenerMesDate(new Date());
            int diaActual = obtenerDiaDate(new Date());

            //Si el dia es menor o igual a 15
            if (diaActual <= 15) {
                if (mesActual > 9) {
                    retorno = getDateString(String.valueOf(anoActual) + "-" + String.valueOf(mesActual) + "-" + String.valueOf(15));
                } else {
                    retorno = getDateString(String.valueOf(anoActual) + "-0" + String.valueOf(mesActual) + "-" + String.valueOf(15));
                }
            } else {
                //Si el mes 12 incremento el anio
                if (mesActual == 12) {
                    retorno = getDateString(String.valueOf(anoActual + 1) + "-0" + String.valueOf(1) + "-" + String.valueOf(15));
                } else {
                    if (mesActual > 9) {
                        retorno = getDateString(String.valueOf(anoActual) + "-" + String.valueOf(mesActual + 1) + "-" + String.valueOf(15));
                    } else {
                        retorno = getDateString(String.valueOf(anoActual) + "-0" + String.valueOf(mesActual + 1) + "-" + String.valueOf(15));
                    }
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularSiguienteMes(Date fecha) throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(fecha);
            int mesActual = obtenerMesDate(fecha);
            int diaActual = obtenerDiaDate(new Date());

            //Si el mes 12 incremento el anio
            if (mesActual == 12) {
                retorno = getDateString(String.valueOf(anoActual + 1) + "-0" + String.valueOf(1) + "-01");
            } else {
                if (mesActual > 9) {
                    retorno = getDateString(String.valueOf(anoActual) + "-" + String.valueOf(mesActual + 1) + "-01");
                } else {
                    retorno = getDateString(String.valueOf(anoActual) + "-0" + String.valueOf(mesActual + 1) + "-01");
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularMesSegunDesplazamiento(Date fecha, int cantidadDeMeses) throws ErrorServiceException {

        try {

            return desplazarEnMeses(fecha, cantidadDeMeses);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularPrincipioDeMesMasProximo() throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(new Date());
            int mesActual = obtenerMesDate(new Date());

            //Si el mes 12 incremento el anio
            if (mesActual == 12) {
                retorno = getDateString(String.valueOf(anoActual + 1) + "-0" + String.valueOf(1) + "-01");
            } else {
                if (mesActual > 9) {
                    retorno = getDateString(String.valueOf(anoActual) + "-" + String.valueOf(mesActual + 1) + "-01");
                } else {
                    retorno = getDateString(String.valueOf(anoActual) + "-0" + String.valueOf(mesActual + 1) + "-01");
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularPrincipioDeAnioMasProximo() throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(new Date());

            retorno = getDateString(String.valueOf(anoActual + 1) + "-01-01");

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularFinDeAnioMasProximo() throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(new Date());

            retorno = getDateString(String.valueOf(anoActual) + "-12-31");

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Date calcularFinDeMesMasProximo() throws ErrorServiceException {

        try {

            Date retorno = null;

            int anoActual = obtenerAnioDate(new Date());
            int mesActual = obtenerMesDate(new Date());
            int diaActual = obtenerDiaDate(new Date());

            if (mesActual > 9) {
                retorno = getDateString(String.valueOf(anoActual) + "-" + String.valueOf(mesActual) + "-" + obtenerCantidadDiasDelMes(obtenerMes(mesActual), anoActual));
            } else {
                retorno = getDateString(String.valueOf(anoActual) + "-0" + String.valueOf(mesActual) + "-" + obtenerCantidadDiasDelMes(obtenerMes(mesActual), anoActual));
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public static Mes obtenerMesAnterior(Mes mes) throws ErrorServiceException {

        try {
            Mes retorno = null;

            if (mes == null) {
                throw new ErrorServiceException("Debe indicar el mes");
            }

            if (Mes.ENERO == mes) {
                retorno = Mes.DICIEMBRE;
            }
            if (Mes.FEBRERO == mes) {
                retorno = Mes.ENERO;
            }
            if (Mes.MARZO == mes) {
                retorno = Mes.FEBRERO;
            }
            if (Mes.ABRIL == mes) {
                retorno = Mes.MARZO;
            }
            if (Mes.MAYO == mes) {
                retorno = Mes.ABRIL;
            }
            if (Mes.JUNIO == mes) {
                retorno = Mes.MAYO;
            }
            if (Mes.JULIO == mes) {
                retorno = Mes.JUNIO;
            }
            if (Mes.AGOSTO == mes) {
                retorno = Mes.JULIO;
            }
            if (Mes.SEPTIEMBRE == mes) {
                retorno = Mes.AGOSTO;
            }
            if (Mes.OCTUBRE == mes) {
                retorno = Mes.SEPTIEMBRE;
            }
            if (Mes.NOVIEMBRE == mes) {
                retorno = Mes.OCTUBRE;
            }
            if (Mes.DICIEMBRE == mes) {
                retorno = Mes.NOVIEMBRE;
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Mes obtenerMesPosterior(Mes mes) throws ErrorServiceException {

        try {

            Mes retorno = null;

            if (mes == null) {
                throw new ErrorServiceException("Debe indicar el mes");
            }

            if (Mes.ENERO == mes) {
                retorno = Mes.FEBRERO;
            }
            if (Mes.FEBRERO == mes) {
                retorno = Mes.MARZO;
            }
            if (Mes.MARZO == mes) {
                retorno = Mes.ABRIL;
            }
            if (Mes.ABRIL == mes) {
                retorno = Mes.MAYO;
            }
            if (Mes.MAYO == mes) {
                retorno = Mes.JUNIO;
            }
            if (Mes.JUNIO == mes) {
                retorno = Mes.JULIO;
            }
            if (Mes.JULIO == mes) {
                retorno = Mes.AGOSTO;
            }
            if (Mes.AGOSTO == mes) {
                retorno = Mes.SEPTIEMBRE;
            }
            if (Mes.SEPTIEMBRE == mes) {
                retorno = Mes.OCTUBRE;
            }
            if (Mes.OCTUBRE == mes) {
                retorno = Mes.NOVIEMBRE;
            }
            if (Mes.NOVIEMBRE == mes) {
                retorno = Mes.DICIEMBRE;
            }
            if (Mes.DICIEMBRE == mes) {
                retorno = Mes.ENERO;
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public static synchronized Calendar getCalendar(java.util.Date fecha) {
        Calendar calendario = Calendar.getInstance(locale);
        calendario.setTime(fecha);
        return calendario;
    }

    public static synchronized int getHora(java.util.Date fecha) {
        return Integer.parseInt(sdfHora.format(fecha));
    }

    public static synchronized int getMes(java.util.Date fecha) {
        return Integer.parseInt(sdfMes.format(fecha));
    }
    
    public static Mes obtenerMesDesdeFecha(Date fecha) throws ErrorServiceException {

        try {


            if (fecha == null) {
                throw new ErrorServiceException("Debe indicar una fecha");
            }

            return obtenerMes(getMes(fecha));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public String obtenerAnioMes(Date fecha) throws ErrorServiceException {

        try {

            String fechaString = dateStringConGuion(fecha);
            String retorno = fechaString.substring(0, 4) + fechaString.substring(5, 7);

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public static String dateStringDiaMesAnioHoraMinuto(Date fecha) {
        return getDiaString(fecha).concat(getMesString(fecha)).concat(getAnioString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha)));
    }

    public static String dateStringDiaMesAnioHoraMinutoSegundoMiliSegundo(Date fecha) {
        return getDiaString(fecha).concat(getMesString(fecha)).concat(getAnioString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha))).concat(String.valueOf(obtenerSegundoDate(fecha))).concat(String.valueOf(obtenerMiliSegundosDate(fecha)));
    }
    
    public static String dateStringDiaMesAnioHoraMinutoSegundo(Date fecha) {
        return getDiaString(fecha).concat(getMesString(fecha)).concat(getAnioString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha))).concat(String.valueOf(obtenerSegundoDate(fecha)));
    }

    public static String dateStringAnioMesDiaHoraMinutoSegundoMiliSegundo(Date fecha) {
        return (getAnioString(fecha).concat(getMesString(fecha)).concat(getDiaString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha))).concat(String.valueOf(obtenerSegundoDate(fecha))).concat(String.valueOf(obtenerMiliSegundosDate(fecha)))).replaceAll("-", "");
    }
    
    public static String dateStringDiaMesAnio(Date fecha) {
        return getDiaString(fecha).concat(getMesString(fecha)).concat(getAnioString(fecha));
    }
    
    public static String dateStringDiaMesAnioConBarra(Date fecha) {
        return getDiaString(fecha).concat("/").concat(getMesString(fecha)).concat("/").concat(getAnioString(fecha));
    }
    
    public static String dateStringMesAnioConBarra(Date fecha) {
        return getMesString(fecha).concat("/").concat(getAnioString(fecha));
    }
    
    public static int obtenerMiliSegundosDate(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        return cal.get(Calendar.MILLISECOND);
    }
  
    public static Date llevarInicioDia(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(Calendar.MILLISECOND, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        return calendario.getTime();
    }
    
    public static Long convertirAnioMes(Long anio, Mes tipoMes) {
        return new Long(anio + df1.format(tipoMes.ordinal() + 1));
    }
    
    public static java.util.Date convertirDateSQLEnDate(java.sql.Date fechaSql){
       java.util.Date utilDate = new java.util.Date(fechaSql.getTime());
       return utilDate;
    }
    
    public static String dateStringDiaMesAnioConBarraMasDia(Date fecha) {
        return (getDiaDeLaSemana(fecha) + " " + getDiaString(fecha).concat("/").concat(getMesString(fecha)).concat("/").concat(getAnioString(fecha)));
    }
    
    public static String getDiaDeLaSemana(Date d){
        
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(d);
	int dia =  cal.get(Calendar.DAY_OF_WEEK);
        String retorno = "";
        if (dia == 1)
          retorno = "Domingo";
        if (dia == 2)
          retorno = "Lunes";
        if (dia == 3)
          retorno = "Martes";
        if (dia == 4)
          retorno = "Miércoles";
        if (dia == 5)
          retorno = "Jueves";
        if (dia == 6)
          retorno = "Viernes";
        if (dia == 7)
          retorno = "Sábado";
        
        return retorno;
            
    }
    
    public static Date llevarInicioDeAnio (Date fecha) throws ErrorServiceException{
  
        String  anio = String.valueOf(getAnioString(fecha));
        return getDateStringConBarra("01/01/"+anio);     
    }
    
    public static Date llevarInicioDeJulio (Date fecha) throws ErrorServiceException{
  
        String  anio = String.valueOf(getAnioString(fecha));
        return getDateStringConBarra("01/07/"+anio);     
    }
    
    public static java.util.Date convertirDateSQLEnTimeStamp(java.sql.Timestamp fechaSql){
       java.util.Date utilDate = new java.util.Date(fechaSql.getTime());
       return utilDate;
    }
    
    public static synchronized String getStringDate(java.util.Date fecha, String modelo) {
        try {
            return (new SimpleDateFormat(modelo, locale)).format(fecha);
        } catch (NullPointerException e) {
            return "";
        }
    }
    
    public static Collection<String> listarFechasEntreFechas(Date fechaDesde, Date fechaHasta){
        
        Collection<String> retorno = new ArrayList();
        Date fechaAux = fechaDesde;
        
        if (!getStringDate(fechaDesde, "dd/MM/yyyy").equals(getStringDate(fechaHasta, "dd/MM/yyyy"))){
            
         retorno.add(getStringDate(fechaDesde, "dd/MM/yyyy"));
        
         while(true){
            fechaAux = agregaDiasAFecha(fechaAux, 1);
            if (getStringDate(fechaAux, "dd/MM/yyyy").equals(getStringDate(fechaHasta, "dd/MM/yyyy"))){
              break;  
            }
            retorno.add(getStringDate(fechaAux, "dd/MM/yyyy")); 
         }
         retorno.add(getStringDate(fechaHasta, "dd/MM/yyyy"));
         
        }else{
         retorno.add(getStringDate(fechaHasta, "dd/MM/yyyy"));   
        } 
        
        return retorno;
    }
    
    public static String obtenerAnioResumido(Date fecha){
        String retorno=getAnioString(fecha);
        retorno = retorno.substring(retorno.length() -2, retorno.length());
        return retorno;
    }
    
    public static String obtenerMesCadenaResumido(Date fecha){
        
        String retorno="";
        
        if (obtenerMesDate(fecha) == 0){
            retorno="ene";
        }else if(obtenerMesDate(fecha) == 1){
            retorno="feb";
        }else if(obtenerMesDate(fecha) == 2){
            retorno="mar";
        }else if(obtenerMesDate(fecha) == 3){
            retorno="abr";
        }else if(obtenerMesDate(fecha) == 4){
            retorno="may";
        }else if(obtenerMesDate(fecha) == 5){
            retorno="jun";
        }else if(obtenerMesDate(fecha) == 6){
            retorno="jul";
        }else if(obtenerMesDate(fecha) == 7){
            retorno="ago";
        }else if(obtenerMesDate(fecha) == 8){
            retorno="sep";
        }else if(obtenerMesDate(fecha) == 9){
            retorno="oct";
        }else if(obtenerMesDate(fecha) == 10){
            retorno="nov";
        }else if(obtenerMesDate(fecha) == 11){
            retorno="dic";
        }
        
        return retorno;
    }
    
    public static String obtenerMesDeFecha(Date fecha){
        
        String retorno="";
        
        if (obtenerMesDate(fecha) == 0){
            retorno="01";
        }else if(obtenerMesDate(fecha) == 1){
            retorno="02";
        }else if(obtenerMesDate(fecha) == 2){
            retorno="03";
        }else if(obtenerMesDate(fecha) == 3){
            retorno="04";
        }else if(obtenerMesDate(fecha) == 4){
            retorno="05";
        }else if(obtenerMesDate(fecha) == 5){
            retorno="06";
        }else if(obtenerMesDate(fecha) == 6){
            retorno="07";
        }else if(obtenerMesDate(fecha) == 7){
            retorno="08";
        }else if(obtenerMesDate(fecha) == 8){
            retorno="09";
        }else if(obtenerMesDate(fecha) == 9){
            retorno="10";
        }else if(obtenerMesDate(fecha) == 10){
            retorno="11";
        }else if(obtenerMesDate(fecha) == 11){
            retorno="12";
        }
        
        return retorno;
    }
    
    public static Collection<Mes> listarMes (){
        
            ArrayList <Mes> meses = new ArrayList<Mes>();

            meses.add(Mes.ENERO);
            meses.add(Mes.FEBRERO);
            meses.add(Mes.MARZO);
            meses.add(Mes.ABRIL);
            meses.add(Mes.MAYO);
            meses.add(Mes.JUNIO);
            meses.add(Mes.JULIO);
            meses.add(Mes.AGOSTO);
            meses.add(Mes.SEPTIEMBRE);
            meses.add(Mes.OCTUBRE);
            meses.add(Mes.NOVIEMBRE);
            meses.add(Mes.DICIEMBRE);
            
            return meses;
   
    }
    
    public static Integer obtenerEdad(Date fNacimiento){
       
        Date fechaNac=llevarInicioDia(fNacimiento);
        
        Calendar fechaNacimiento = Calendar.getInstance();
        
        //Se crea un objeto con la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        
        //Se asigna la fecha recibida a la fecha de nacimiento.
        fechaNacimiento.setTime(fechaNac);
        
        //Se restan la fecha actual y la fecha de nacimiento
        int ano = fechaActual.get(Calendar.YEAR)- fechaNacimiento.get(Calendar.YEAR);
        int mes =fechaActual.get(Calendar.MONTH)- fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE)- fechaNacimiento.get(Calendar.DATE);
        
        //Se ajusta el año dependiendo el mes y el día
        if(mes<0 || (mes==0 && dia<0)){
            ano--;
        }
        //Regresa la edad en base a la fecha de nacimiento
        return ano;
    }
    
    public static String convertirFechaToString(Date fecha){
        if(fecha==null){
            return null;
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        return (""+ anio + "" + (mes <= 9 ? (String.valueOf("0" + mes)) : mes) + "" + (dia <= 9 ? (String.valueOf("0" + dia)) : dia) );
    }
    
    public static Long convertirFechaToLong(Date fecha){
        if(fecha==null){
            return null;
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        return new Long(anio + "" + df1.format(mes) + df1.format(dia));
    }
    
    public static Long convertirFechaEnHoraMinuto(Date fecha) {
        if(fecha == null){
            return null;
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int min = calendario.get(Calendar.MINUTE);

        return new Long(hora + df1.format(min));
    }
    
    public static Long convertirFechaEnHoraMinutoSegundo(Date fecha) {
        if(fecha == null){
            return null;
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int min = calendario.get(Calendar.MINUTE);
        int seg = calendario.get(Calendar.SECOND);
        String horaSTR = hora + df1.format(min) + df1.format(seg);
        return Long.parseLong(horaSTR);
    }
    
    public static int cantidadDeDiasSegunCantidadDePeriodos(int cantidadDePeriodos, Date fecha){
 
        
        int cantidadDeDias = 0;
        int contador=0;
        Mes mes = null;
        
        fecha = UtilFechaBean.llevarInicioMes(fecha);
        
        do{
            
          try{
           mes = obtenerMesDesdeFecha(fecha);
          }catch(Exception e){}
          
          if (mes == Mes.ENERO || mes == Mes.MARZO || mes == Mes.MAYO || mes == Mes.JULIO || mes == Mes.AGOSTO || mes == Mes.OCTUBRE || mes == Mes.DICIEMBRE)   
           cantidadDeDias = cantidadDeDias + 31;
          else if (mes == Mes.FEBRERO)
            cantidadDeDias = cantidadDeDias + (esViciesto(UtilFechaBean.obtenerAnioDate(fecha)) ? 29: 28);
          else
            cantidadDeDias = cantidadDeDias + 30;
          
          fecha = UtilFechaBean.llevarInicioMes(UtilFechaBean.restarDiasAFecha(fecha, 1));
          
          contador++;
          
        }while(contador < cantidadDePeriodos);
      
        return cantidadDeDias;
    }
    
    /**
     * Calcula la diferencia entre dos fechas. Devuelve el resultado en días,
     * meses o años según sea el valor del parámetro 'tipo'
     *
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @param tipo 0=TotalAños; 1=TotalMeses; 2=TotalDías; 3=MesesDelAnio;
     * 4=DiasDelMes
     * @return numero de días, meses o años de diferencia
     */
    public long getDiffDates(Date fechaInicio, Date fechaFin, int tipo) {
        // Fecha inicio
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        int anioInicio = calendarInicio.get(Calendar.YEAR);

        // Fecha fin
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fechaFin);
        int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
        int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        int anioFin = calendarFin.get(Calendar.YEAR);

        int anios = 0;
        int mesesPorAnio = 0;
        int diasPorMes = 0;
        int diasTipoMes = 0;

        //
        // Calculo de días del mes
        //
        if (mesInicio == 2) {
            // Febrero
            if ((anioFin % 4 == 0) && ((anioFin % 100 != 0) || (anioFin % 400 == 0))) {
                // Bisiesto
                diasTipoMes = 29;
            } else {
                // No bisiesto
                diasTipoMes = 28;
            }
        } else if (mesInicio <= 7) {
            // De Enero a Julio los meses pares tienen 30 y los impares 31
            if (mesInicio % 2 == 0) {
                diasTipoMes = 30;
            } else {
                diasTipoMes = 31;
            }
        } else if (mesInicio > 7) {
            // De Julio a Diciembre los meses pares tienen 31 y los impares 30
            if (mesInicio % 2 == 0) {
                diasTipoMes = 31;
            } else {
                diasTipoMes = 30;
            }
        }

        //
        // Calculo de diferencia de año, mes y dia
        //
        if ((anioInicio > anioFin) || (anioInicio == anioFin && mesInicio > mesFin)
                || (anioInicio == anioFin && mesInicio == mesFin && diaInicio > diaFin)) {
            // La fecha de inicio es posterior a la fecha fin
            // System.out.println("La fecha de inicio ha de ser anterior a la fecha fin");
            return -1;
        } else {
            if (mesInicio <= mesFin) {
                anios = anioFin - anioInicio;
                if (diaInicio <= diaFin) {
                    mesesPorAnio = mesFin - mesInicio;
                    diasPorMes = diaFin - diaInicio;
                } else {
                    if (mesFin == mesInicio) {
                        anios = anios - 1;
                    }
                    mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
                    diasPorMes = diasTipoMes - (diaInicio - diaFin);
                }
            } else {
                anios = anioFin - anioInicio - 1;
                System.out.println(anios);
                if (diaInicio > diaFin) {
                    mesesPorAnio = mesFin - mesInicio - 1 + 12;
                    diasPorMes = diasTipoMes - (diaInicio - diaFin);
                } else {
                    mesesPorAnio = mesFin - mesInicio + 12;
                    diasPorMes = diaFin - diaInicio;
                }
            }
        }
        //System.out.println("Han transcurrido " + anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.");		

        //
        // Totales
        //
        long returnValue = -1;

        switch (tipo) {
            case 0:
                // Total Años
                returnValue = anios;
                // System.out.println("Total años: " + returnValue + " Años.");
                break;

            case 1:
                // Total Meses
                returnValue = anios * 12 + mesesPorAnio;
                // System.out.println("Total meses: " + returnValue + " Meses.");
                break;

            case 2:
                // Total Dias (se calcula a partir de los milisegundos por día)
                long millsecsPerDay = 86400000; // Milisegundos al día
                returnValue = (fechaFin.getTime() - fechaInicio.getTime()) / millsecsPerDay;
                // System.out.println("Total días: " + returnValue + " Días.");
                break;

            case 3:
                // Meses del año
                returnValue = mesesPorAnio;
                // System.out.println("Meses del año: " + returnValue);
                break;

            case 4:
                // Dias del mes
                returnValue = diasPorMes;
                // System.out.println("Dias del mes: " + returnValue);
                break;

            default:
                break;
        }

        return returnValue;
    }
    
    public static Date actualizarHoraAFecha(Date fecha, Date tiempo){
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        Calendar time = Calendar.getInstance();
        time.setTime(tiempo);
        calendario.set(Calendar.MILLISECOND, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        calendario.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        
        return calendario.getTime();
    }
    
    public static Date convertirLongToDate(Long fecha) {
        Calendar calendario = Calendar.getInstance();
        int anioMes = fecha.intValue() / 100;
        int dia = fecha.intValue() - new Long(anioMes + "00").intValue();
        int anio = anioMes / 100;
        int mes = anioMes - new Long(anio + "00").intValue();

        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.YEAR, anio);
        calendario.set(Calendar.MONTH, mes - 1);
        calendario.set(Calendar.DAY_OF_MONTH, dia);

        return llevarInicioDia(calendario.getTime());
    }

    public static String generarIdentificadorUnicoConFecha() {

        String identificador = dateStringAnioMesDiaHoraMinutoSegundoMiliSegundo(new Date());

        return identificador;

    }
    
    public static Long convertirDateToLongYYYYMMDDhhmi(Date hora){
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(hora);
        String h = df1.format(calendario.get(Calendar.HOUR_OF_DAY)) + 
                   df1.format(calendario.get(Calendar.MINUTE));
        return new Long(convertirFechaToLong(hora) + h);
    }

    public static String dateStringAnioMesDiaHoraMinutoSegundo(Date fecha) {
        return (getAnioString(fecha).concat(getMesString(fecha)).concat(getDiaString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha))).concat(String.valueOf(obtenerSegundoDate(fecha)))).replaceAll("-", "");
    }
    
    public static String dateStringMesDiaHoraMinutoSegundo(Date fecha) {
        return (getMesString(fecha).concat(getDiaString(fecha)).concat(String.valueOf(obtenerHoraDate(fecha))).concat(String.valueOf(obtenerMinutoDate(fecha))).concat(String.valueOf(obtenerSegundoDate(fecha)))).replaceAll("-", "");
    }
    
    
    public static Date obtenerPrimerDiaDeLaSemana(Date dia){
       
        int diaDeLaSemana = obtenerDiaDeLaSemanaDate(dia);
        
        Date retorno = null;
        if(diaDeLaSemana == 2){
         retorno = dia;           
        } else if (diaDeLaSemana==3){
         retorno = restarDiasAFecha(dia, 1);
        } else if (diaDeLaSemana==4){
         retorno = restarDiasAFecha(dia, 2);
        } else if (diaDeLaSemana==5){
         retorno = restarDiasAFecha(dia, 3);
        } else if (diaDeLaSemana==6){
         retorno = restarDiasAFecha(dia, 4);
        } else if (diaDeLaSemana==7){
         retorno = restarDiasAFecha(dia, 5);
        } else if (diaDeLaSemana==1){
         retorno = restarDiasAFecha(dia, 6);
        }
        
        return UtilFechaBean.llevarInicioDia(retorno);
    }
    
    public static String dateStringMesAnio(Date fecha) {
        return getMesString(fecha).concat(getAnioString(fecha));
    }
    
    public static String dateStringAnioMes(Date fecha) {
        return getAnioString(fecha) + getMesString(fecha);
    }
    
    public static Integer edad(Date fecha){
       
        Date fechaNac=llevarInicioDia(fecha);
        
        Calendar fechaNacimiento = Calendar.getInstance();
        
        //Se crea un objeto con la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        
        //Se asigna la fecha recibida a la fecha de nacimiento.
        fechaNacimiento.setTime(fechaNac);
        
        //Se restan la fecha actual y la fecha de nacimiento
        int anio = fechaActual.get(Calendar.YEAR)- fechaNacimiento.get(Calendar.YEAR);
        int mes =fechaActual.get(Calendar.MONTH)- fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE)- fechaNacimiento.get(Calendar.DATE);
        
        //Edad
        int edad= anio;
        
        //Se ajusta el año dependiendo el mes y el día
        if(mes<0 || (mes==0 && dia<0)){
            edad--;
        }
        
        //Regresa la edad en base a la fecha de nacimiento
        return edad;
    }
    
    public static Date convertirAnioMesEnFecha(Long anioMes) {
        int anio = (int) (anioMes / 100);
        int mes = (int) (anioMes - (anio * 100));
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.MONTH, mes - 1);
        calendario.set(Calendar.YEAR, anio);
        return calendario.getTime();
    }
    
    public static Date convertirAnioMesEnFecha(Long anioMes, Integer dia) {
        int anio = (int) (anioMes / 100);
        int mes = (int) (anioMes - (anio * 100));
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        calendario.set(Calendar.MONTH, mes - 1);
        calendario.set(Calendar.YEAR, anio);
        return calendario.getTime();
    }
   
 public enum Mes {
ENERO, FEBRERO, MARZO, ABRIL, MAYO, 
JUNIO, JULIO, AGOSTO, SEPTIEMBRE, OCTUBRE,
NOVIEMBRE, DICIEMBRE;
}

}

