/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server.resources;

/**
 *
 * @author Gavrilo
 */



import alati.KonverterEntiteta;
import entitetiSistem1.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("podsistem1")
public class Podsistem1Resursi {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="QueuePodsistem1")
    private Queue queuePodsistem1;
    
    @Resource(lookup="QueuePodsistem2")
    private Queue queuePodsistem2;
    
    @Resource(lookup="QueueServer")
    private Queue queueServer;
    
    @GET
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
    //static int idOpCnt=0;
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
    
    
    
    @POST
    @Path("mesto/{naziv}/{postanskiBroj}")
    public Response kreirajMesto(@PathParam("naziv") String naziv, @PathParam("postanskiBroj") String postanskiBroj){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"1");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setStringProperty("postanskiBroj", postanskiBroj);
            
            producer.send(queuePodsistem1, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    }
    
    @POST
    @Path("filijala/{naziv}/{adresa}/{idMesta}")
    public Response kreirajFilijalu(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa,@PathParam("idMesta") int idMesta){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"2");
            JMSConsumer consumer =  context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setStringProperty("adresa", adresa);
            txtMsg.setIntProperty("idMesta", idMesta);
            producer.send(queuePodsistem1, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    
    }
   
    
    @POST
    @Path("komitent/{naziv}/{adresa}/{idMesta}")
    public Response kreirajKlijenta(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa,@PathParam("idMesta") int idMesta){
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"3");
            JMSConsumer consumer1 =  context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
            JMSConsumer consumer2 =  context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setStringProperty("naziv", naziv);
            txtMsg.setStringProperty("adresa", adresa);
            txtMsg.setIntProperty("idMesta", idMesta);
            producer.send(queuePodsistem1, txtMsg);
            TextMessage ans=(TextMessage)consumer1.receive();
            if(!txtMsg.getBooleanProperty("desilaSeGreska")){
                producer.send(queuePodsistem2, txtMsg);
                consumer2.receive();
            }
            return Response.status(Response.Status.CREATED).entity(ans.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    }
    
    @PUT
    @Path("komitent/promeniMesto/{idK}/{idMesta}")
    public Response promeniMestoKomitenta(@PathParam("idK") int idK, @PathParam("idMesta") int idMesta){
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"4");
            JMSConsumer consumer =  context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
            txtMsg.setIntProperty("idK", idK);
            
            txtMsg.setIntProperty("idMesta", idMesta);
            producer.send(queuePodsistem1, txtMsg);
            txtMsg=(TextMessage)consumer.receive();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    }
    
    
    @GET
    @Path("mesto/svaMesta")
    public Response svaMesta() throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"10");
        JMSConsumer consumer =  context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
        producer.send(queuePodsistem1, txtMsg);
        Message msg = consumer.receive(5000);
        if(msg==null) return Response.status(500).entity("Isteklo vreme").build();
        if (!(msg instanceof ObjectMessage))return Response.status(500).entity("Pogresan odgovor").build();
        ObjectMessage objMsg = (ObjectMessage)msg;
        String odgovor="";
        try {
            ArrayList<Mesto> lista=(ArrayList<Mesto>) objMsg.getObject();
            for(Mesto m: lista){
            odgovor+=m.toString()+" ";
            }
            String tekst = KonverterEntiteta.mestaUTekst(lista);
            //return Response.status(Response.Status.CREATED).entity(new GenericEntity<ArrayList<Mesto>>(lista){}).build();
            return Response.status(200).entity(tekst).build();
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    }
    
    @GET
    @Path("filijala/sveFilijale")
    public Response sveFilijale() throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"11");
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
        producer.send(queuePodsistem1, txtMsg);
        Message msg = consumer.receive(5000);
        if(msg==null) return Response.status(200).entity("Isteklo vreme").build();
        //if (!(msg instanceof ObjectMessage))return Response.status(500).entity("Pogresan odgovor").build();
        ObjectMessage objMsg = (ObjectMessage)msg;
        String odgovor="";
        try {
            ArrayList<Filijala> lista=(ArrayList<Filijala>) objMsg.getObject();
            String tekst = KonverterEntiteta.filijaleUTekst(lista);
            //return Response.status(Response.Status.CREATED).entity(new GenericEntity<ArrayList<Filijala>>(lista){}).build();
            return Response.status(200).entity(tekst).build();
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
    }
    @GET
    @Path("komitent/sviKomitenti")
    public Response sviKomitenti() throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"12");
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
        producer.send(queuePodsistem1, txtMsg);
        Message msg = consumer.receive(5000);
        if(msg==null) return Response.status(200).entity("Isteklo vreme").build();
        //if (!(msg instanceof ObjectMessage))return Response.status(500).entity("Pogresan odgovor").build();
        ObjectMessage objMsg = (ObjectMessage)msg;
        String odgovor="";
        try {
            ArrayList<Komitent> lista=(ArrayList<Komitent>) objMsg.getObject();
            String tekst = KonverterEntiteta.komitentiUTekst(lista);
            //return Response.status(Response.Status.CREATED).entity(new GenericEntity<ArrayList<Komitent>>(lista){}).build();
            return Response.status(200).entity(tekst).build();
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(200).entity("Greska").build();
        }
    }
    
    
}
