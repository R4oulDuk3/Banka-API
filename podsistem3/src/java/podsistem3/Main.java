/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;



import java.io.Serializable;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import promene.Promena;

/**
 *
 * @author Gavrilo
 */
public class Main {
    
    public static int random(){
    
        return (int)(Math.random()*1000000);
    }
    
    public static TextMessage kreirajPoruku( JMSContext context) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "podsistem3");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static TextMessage kreirajPoruku( JMSContext context,int idOperacije) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "podsistem3");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,int idOperacije,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "podsistem3");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "podsistem3");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static ObjectMessage kreirajObjektnuPoruku(JMSContext context,int idOperacije,Serializable object) throws JMSException{
         ObjectMessage objMsg = context.createObjectMessage(object);
        objMsg.setStringProperty("izvor", "podsistem3");
        objMsg.setIntProperty("idOperacije",idOperacije);
        return objMsg;
    }

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    
    
    @Resource(lookup = "QueueServer")
    private static Queue queueServer;
    
    @Resource(lookup = "QueuePodsistem3")
    private static Queue queuePodsistem3;
    
    @Resource(lookup = "QueuePodsistem2")
    private static Queue queuePodsistem2;
    
    @Resource(lookup = "QueuePodsistem1")
    private static Queue queuePodsistem1;
    static EntityManagerFactory efm = Persistence.createEntityManagerFactory("podsistem3PU");

    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        Thread nitZaDohvatanjeRezerve = new Thread(() -> {
           JMSConsumer consumer_server = context.createConsumer(queuePodsistem3, "izvor= 'server'");
           JMSProducer producer = context.createProducer();
           while(true){
               try {
                   TextMessage msg =(TextMessage) consumer_server.receive();
                   String baza=MenadzerPodataka.kopijaBaze();
                   TextMessage odgovor = kreirajPoruku(context,msg.getIntProperty("idOperacije"),baza);
                   producer.send(queueServer, odgovor);
               } catch (JMSException ex) {
                   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        });
        
        
        
        Thread nitZaKreiranjeRezerve = new Thread(() -> {
            while(true){
            try {
                for(int i=0;i<6;i++){
                    Thread.sleep(20*1000);
                    System.out.println("Proslo je "+(i+1)*20+" sekundi");
                }
                System.out.println("Pocetak kreiranja Rezerve");
                
                JMSProducer producer = context.createProducer();
                
                
                
                TextMessage zapocni = kreirajPoruku(context,"Posalji promene");
                System.out.println("ID 1. naredbe: "+zapocni.getStringProperty("idOperacije"));
                producer.send(queuePodsistem1, zapocni);
                producer.send(queuePodsistem2, zapocni);
                JMSConsumer consumer_podsistem1 = context.createConsumer(queuePodsistem3, "idOperacije= "+zapocni.getIntProperty("idOperacije")+" AND izvor= 'podsistem1'");
                JMSConsumer consumer_podsistem2 = context.createConsumer(queuePodsistem3, "idOperacije= "+zapocni.getIntProperty("idOperacije")+" AND izvor= 'podsistem2'");
                
                ObjectMessage msg1 = (ObjectMessage) consumer_podsistem1.receive();
                ObjectMessage msg2 = (ObjectMessage) consumer_podsistem2.receive();
                System.out.println("ID sis1 odgovora: "+msg1.getStringProperty("idOperacije"));
                System.out.println("ID sis2 odgovora: "+msg2.getStringProperty("idOperacije"));
                try {
                    ArrayList<Promena> promene_podsistem1 = (ArrayList<Promena>)msg1.getObject();
                    ArrayList<Promena> promene_podsistem2 = (ArrayList<Promena>)msg2.getObject();
                    promene_podsistem1.forEach((promena)->{
                        System.out.println(promena);
                        promena.izvrsiPromenu(efm);});
                    promene_podsistem2.forEach((promena)->{
                        System.out.println(promena);
                        promena.izvrsiPromenu(efm);});
                } catch (JMSException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //TextMessage zavrsi = kreirajPoruku(context,zapocni.getIntProperty("idOperacije"),"Promene uspesno primljene");
                //zavrsi.setStringProperty("izvor", "podsistem3");
                //producer.send(queuePodsistem1, zavrsi);
                //producer.send(queuePodsistem2, zavrsi);
                System.out.println("Kraj kreiranja Rezerve");
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });
        nitZaKreiranjeRezerve.start();
        nitZaDohvatanjeRezerve.start();
        
        
    }
         
    
}
