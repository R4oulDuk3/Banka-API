/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server.resources;

import static com.mycompany.server.resources.Podsistem2Resursi.kreirajPoruku;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gavrilo
 */
@Path("podsistem3")
public class Podsistem3Resursi {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="QueuePodsistem3")
    private Queue queuePodsistem3;
    
    @Resource(lookup="QueuePodsistem2")
    private Queue queuePodsistem2;
    
    @Resource(lookup="QueuePodsistem1")
    private Queue queuePodsistem1;
    
    @Resource(lookup="QueueServer")
    private Queue queueServer;
    
    public static int random(){
    
        return (int)(Math.random()*1000000);
    }
    public static TextMessage kreirajPoruku( JMSContext context) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "server");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static TextMessage kreirajPoruku( JMSContext context,int idOperacije) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "server");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,int idOperacije,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "server");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku( JMSContext context,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "server");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    @GET
     @Path("rezervnaBaza")
      public Response dohvRezervnuBazu() throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context," ");
        producer.send(queuePodsistem3, txtMsg);
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem3'");
        
        try {
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
      
      }
    @GET
     @Path("razlike")
      public Response dohvatiRazlike() throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"16");
        producer.send(queuePodsistem1, txtMsg);
        producer.send(queuePodsistem2, txtMsg);
        JMSConsumer consumer1 = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
        JMSConsumer consumer2 = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
        TextMessage txtMsg1=(TextMessage)consumer1.receive(5000);
        TextMessage txtMsg2=(TextMessage)consumer2.receive(5000);
        try {
            
            if(txtMsg1==null || txtMsg2==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg1.getText()+txtMsg2.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
      
      }
    
}
