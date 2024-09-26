package com.interisys.business.logic;

import com.interisys.business.domain.entity.CuentaCorreo;
import com.interisys.business.domain.entity.DetalleRecibo;
import com.interisys.business.domain.entity.Inquilino;
import com.interisys.business.domain.entity.Propietario;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author spaul
 */
@Stateless
@LocalBean
public class GestionMailServiceBean {
    
    private Session session; //configuración de la sesión de correo
    private String servidorSmtp;
    private String puertoSmtp;
    private Message mensaje;
    private Collection contenidoRelacionado; //imagenes embebidas o archivos adjuntos
    private Collection adjuntos;
    private CuentaCorreo correoOrigen;
    private Propietario propietario;
    private Inquilino inquilino;
    private String tituloCorreo;
    private String contenidoMensaje;
    private String destinatario;
    
    
    
    private DetalleReciboServiceBean serviceDetalle;
    private CuentaCorreoServiceBean serviceCuentaCorreo;
    
    public void enviarMail (String idRecibo, String path)throws ErrorServiceException {
        /*
        contenidoMensaje: cuerpo del mensaje en HTML.
        pathArchivoAdjunto: ruta al archivo que se desea adjuntar (puede ser null si no hay adjunto).
        */
        try{
            
            Collection<DetalleRecibo> detalles = serviceDetalle.listarDetalleReciboActivo(idRecibo);
            //obtengo el primer detalle
            if (!detalles.isEmpty()) {
                DetalleRecibo detalle = detalles.iterator().next(); 
           
                correoOrigen = serviceCuentaCorreo.buscarCuentaCorreoActiva();
                propietario = detalle.getExpensaInmueble().getInmueble().getPropietario();
                inquilino = detalle.getExpensaInmueble().getInmueble().getInquilino();
                tituloCorreo = "Envío de recibo expensa";
                contenidoMensaje = "A continuación dejamos registro del recibo en un archivo PDF. Gracias!";
            }
   
            //NO SE ENVIAN IMAGENES //String imagen = "<img src=\"cid:image\">";  
            /*el CID se utiliza para incrustar imágenes dentro del contenido HTML del correo de manera 
            que se muestren directamente en el cuerpo del mensaje en lugar de ser enlaces externos*/
            StringBuilder html = new StringBuilder(); //crear estructura básica de HTML
            //tabla con estilos CSS 
            html.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=us-ascii\"></head><body>");
            html.append("<table style=\"font-family: Verdana,sans-serif; font-size: 13px; color: #0C1C29; width: 750px\"> <tbody>");
            html.append("<tr>");
            html.append("<td align=\"left\" style=\"background-color: #0011fb; color: #FFFFFF; font-size:1.2em; font-weight: bold; padding: 0\"> " + "OSPELSYM." + "  </td>");
            html.append("</tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr>");
            html.append("<td align=\"left\">");
            html.append(contenidoMensaje + "</td>"); //mensaje
            html.append("</tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            html.append("<tr><td>&nbsp;</td></tr>");
            
            
            if (inquilino == null) {
                destinatario = propietario.getCorreoElectronico();
            } else {
                destinatario = inquilino.getCorreoElectronico();
            }
               //,pathArchivoAdjunto
            enviarEmailHTML(destinatario, html.toString(), correoOrigen.getSmtp(), correoOrigen.getPuerto(), correoOrigen.getCorreo(), correoOrigen.getClave(), correoOrigen.isTls(), "CONSORCIO", tituloCorreo, path);
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    @Asynchronous //el método se ejecuta de manera asíncrona, permitiendo que el método llamante continúe sin esperar a que finalice.
    public void enviarEmailHTML(String destinatario, String html, String servidorIPAux, String servidorPuertoAux, String correoConfiguracionAux, String claveConfiguracionAux, boolean protocoloTLSAux, String bandejaEntrada, String asunto, String pathArchivo) throws ErrorServiceException {

        try {

            String servidorIP = servidorIPAux;
            String servidorPuerto = servidorPuertoAux;
            final String correoConfiguracion = correoConfiguracionAux; //final: su valor es constante después de la asignación inicial
            final String claveConfiguracion = claveConfiguracionAux;
            final boolean protocoloTLS = protocoloTLSAux;
            

            armarConfiguracion(servidorIP, servidorPuerto, protocoloTLS);
            crearMail(session); //inicializa la sesión de correo

            session.getProperties().put("mail.smtp.auth", "true"); //se habilita la autenticación SMTP 
            Transport transporte = session.getTransport("smtp"); //maneja la conexión con el servidor de correo para enviar mensajes. 
            transporte.connect(correoConfiguracion, claveConfiguracion); //se conecta al servidor SMTP

            MimeMessage message = new MimeMessage(session); 
            /*representa un mensaje de correo electrónico con contenido MIME (Multipurpose Internet Mail Extensions).
              sirve para aquellos correos que pueden incluir texto, HTML, imágenes, archivos adjuntos y otros tipos de contenido*/
            // Quien envia el correo
            message.setFrom(new InternetAddress(correoConfiguracion, bandejaEntrada));

            // A quien va dirigido

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));

            message.setSubject(asunto);
           
            //armar partes
            MimeMultipart multipart = new MimeMultipart("related"); //permite incluir múltiples partes como texto, imágenes y archivos adjuntos.
            
            
            //Parte HTML
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(html, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Parte Archivo Adjunto
            if (pathArchivo != null && !pathArchivo.trim().isEmpty()){
                File archivo = new File(pathArchivo);
                if (archivo.exists()){
                    MimeBodyPart adjunto = new MimeBodyPart();
                    DataSource source = new FileDataSource(pathArchivo);
                    adjunto.setDataHandler(new DataHandler(source));
                    adjunto.setFileName(archivo.getName());
                    multipart.addBodyPart(adjunto);
                }
            }
            
            message.setContent(multipart);
            message.setSentDate(new Date()); //fecha de envio
            message.saveChanges();

            transporte.sendMessage(message, message.getAllRecipients());  //se envia el correo a todos los destinatarios
            transporte.close();


        } catch (UnsupportedEncodingException ex) {
            throw new ErrorServiceException("Error al enviar correo electrónico");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error al enviar correo electrónico");
        }
    }
    
    private void armarConfiguracion(String sSmtp, String pSmtp, boolean protocoloTLS) {

        this.servidorSmtp = sSmtp;
        this.puertoSmtp = pSmtp;

        Properties properties = System.getProperties(); //representa un conjunto de pares clave-valor utilizados para configurar (en este caso) la sesion del correo
        properties.put("mail.smtp.host", servidorSmtp); //dirección del servidor SMTP.
        properties.put("mail.smtp.starttls.enable", protocoloTLS); //habilita TLS si protocoloTLS es true
        properties.put("mail.smtp.port", puertoSmtp);

        this.session = Session.getDefaultInstance(properties, null);  //crea una nueva sesión de correo con las propiedades configuradas
    }
                                                            // Authenticator = null: se utiliza para proporcionar credenciales de autenticación si el servidor SMTP requiere autenticación.
    private void crearMail(Session session) {
        this.mensaje = new MimeMessage(session);
        this.contenidoRelacionado = new ArrayList();
        this.adjuntos = new ArrayList();
    }
    
}
