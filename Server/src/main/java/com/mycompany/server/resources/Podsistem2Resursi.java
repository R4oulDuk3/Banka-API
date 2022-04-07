/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server.resources;

import alati.KonverterEntiteta;
import entitetiSistem2.*;
import static com.mycompany.server.resources.Podsistem1Resursi.kreirajPoruku;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gavrilo
 */
@Path("podsistem2")
public class Podsistem2Resursi {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
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
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
    @POST
    @Path("racun/otvori/{idK}/{dozvoljenMinus}")
     public Response otvoriRacun(@PathParam("idK") int idKomitenta, @PathParam("dozvoljenMinus") double dozvoljenMinus){
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"5");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setIntProperty("idKomitenta", idKomitenta);
            txtMsg.setDoubleProperty( "dozvoljenMinus",dozvoljenMinus);
            producer.send(queuePodsistem2, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
     }
     
    @PUT
    @Path("racun/zatvori/{idR}")
     public Response zatvoriRacun(@PathParam("idR") int idRacuna){
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"6");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setIntProperty("idRacun", idRacuna);
            producer.send(queuePodsistem2, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
     }
     @POST
    @Path("transakcije/prenos/{idRSa}/{idRNa}/{iznos}/{svrha}")
     public Response kreirajPrenos(@PathParam("idRSa") int idRSa,@PathParam("idRNa") int idRNa, @PathParam("iznos") double iznos,@PathParam("svrha") String svrha){
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            TextMessage txtMsg = kreirajPoruku(context,"7");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setIntProperty("idRacunSa",idRSa);
            txtMsg.setIntProperty("idRacunNa",idRNa);
            txtMsg.setDoubleProperty("iznos",iznos);
            txtMsg.setStringProperty("svrha",svrha);
            producer.send(queuePodsistem2, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
     }
     public boolean postojiFilijala(int idFil) throws JMSException{
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        TextMessage txtMsg = kreirajPoruku(context,"Postoji filijala");
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
        txtMsg.setIntProperty("idFil", idFil);
        producer.send(queuePodsistem1, txtMsg);
        TextMessage ans = (TextMessage)consumer.receive(5000);
        if(ans.getText().equals("postoji"))return true;
        return false;
     }
     @POST
    @Path("transakcije/uplata/{idR}/{idFil}/{iznos}/{svrha}")
     public Response kreirajUplatu(@PathParam("idR") int idRSa, @PathParam("iznos") double iznos,@PathParam("svrha") String svrha,@PathParam("idFil") int idFil){
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            if(!postojiFilijala(idFil))return Response.status(500).entity("Greska filijala ne postoji").build(); 
            TextMessage txtMsg = kreirajPoruku(context,"8");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setIntProperty("idRacun",idRSa);
            txtMsg.setIntProperty("idFilijalije",idFil);
            txtMsg.setDoubleProperty("iznos",iznos);
            txtMsg.setStringProperty("svrha",svrha);
            producer.send(queuePodsistem2, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
     }
     @POST
    @Path("transakcije/isplata/{idR}/{idFil}/{iznos}/{svrha}")
     public Response kreirajIsplatu(@PathParam("idR") int idRSa, @PathParam("iznos") double iznos,@PathParam("svrha") String svrha,@PathParam("idFil") int idFil){
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        try {
            if(!postojiFilijala(idFil))return Response.status(500).entity("Greska filijala ne postoji").build(); 
            TextMessage txtMsg = kreirajPoruku(context,"9");
            JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
            txtMsg.setIntProperty("idRacun",idRSa);
            txtMsg.setIntProperty("idFilijalije",idFil);
            txtMsg.setDoubleProperty("iznos",iznos);
            txtMsg.setStringProperty("svrha",svrha);
            producer.send(queuePodsistem2, txtMsg);
            txtMsg=(TextMessage)consumer.receive(5000);
            if(txtMsg==null)return Response.status(Response.Status.CREATED).entity("Greska").build();
            return Response.status(Response.Status.CREATED).entity(txtMsg.getText()).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();        }
     }
     
     @GET
     @Path("racun/dohvatiRacune/{idK}")
      public Response dohvatiRacune(@PathParam("idK") int idK) throws JMSException{
          JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"13");
        txtMsg.setIntProperty("idKomitent",idK);
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
        producer.send(queuePodsistem2, txtMsg);
        Message msg = consumer.receive(5000);
        if(msg==null) return Response.status(200).entity("Isteklo vreme").build();
        //if (!(msg instanceof ObjectMessage))return Response.status(500).entity("Pogresan odgovor").build();
        ObjectMessage objMsg = (ObjectMessage)msg;
        try {
            System.out.println("CHECKKKKK");
            ArrayList<Racun> lista=(ArrayList<Racun>) objMsg.getObject();
            lista.forEach((racun)->{
                System.out.println(racun.toString());});
              String tekst = KonverterEntiteta.racuniUTekst(lista);
            //return Response.status(Response.Status.CREATED).entity(new GenericEntity<ArrayList<Racun>>(lista){}).build();
            return Response.status(200).entity(tekst).build();
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).entity("Greska").build();
        }
      }
      
      
      
     
     @GET
     @Path("transakcije/dohvatiTransakcije/{idR}")
      public Response dohvatiTransakcije(@PathParam("idR") int idR) {
        try {
          JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage txtMsg = kreirajPoruku(context,"14");
        txtMsg.setIntProperty("idRacun",idR);
        JMSConsumer consumer = context.createConsumer(queueServer, "idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
        producer.send(queuePodsistem2, txtMsg);
        Message msg = consumer.receive(5000);
        if(msg==null) return Response.status(200).entity("Isteklo vreme").build();
        //if (!(msg instanceof ObjectMessage))return Response.status(500).entity("Pogresan odgovor").build();
        ObjectMessage objMsg = (ObjectMessage)msg;
         System.out.println("CHECKKKKK");
        
            ArrayList<Transakcija> lista=(ArrayList<Transakcija>) objMsg.getObject();
            lista.forEach((transakcija)->{
                System.out.println(transakcija.toString()+" "+ transakcija.getSvrha()+" "+transakcija.getIznos()+" "+ transakcija.getIdRacunaSa());
            });
            String tekst = KonverterEntiteta.transakcijeUTekst(lista);
            //return Response.status(Response.Status.FOUND).entity(new GenericEntity<ArrayList<Transakcija>>(lista){}).build();
            return Response.status(200).entity(tekst).build();
        } catch (JMSException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(Podsistem1Resursi.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(200).entity("Greska").build();
        }
      }
    
     
}



