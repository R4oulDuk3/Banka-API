/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem1;

import entitetiSistem1.Mesto;
import entitetiSistem1.Komitent;
import entitetiSistem1.Filijala;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.JMSContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.jms.*;

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
        txtMsg.setStringProperty("izvor", "podsistem1");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static TextMessage kreirajPoruku( JMSContext context,int idOperacije) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "podsistem1");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,int idOperacije,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "podsistem1");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "podsistem1");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static ObjectMessage kreirajObjektnuPoruku(JMSContext context,int idOperacije,Serializable object) throws JMSException{
         ObjectMessage objMsg = context.createObjectMessage(object);
        objMsg.setStringProperty("izvor", "podsistem1");
        objMsg.setIntProperty("idOperacije",idOperacije);
        return objMsg;
    }

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    
    
    @Resource(lookup = "QueueServer")
    private static Queue queueServer;
    
    @Resource(lookup = "QueuePodsistem1")
    private static Queue queuePodsistem1;
    @Resource(lookup = "QueuePodsistem3")
    private static Queue queuePodsistem3;
    
    public static void main(String[] args) {
        //List<Mesto> lista = MenadzerPodataka.dohvatiSvaMesta();
        //lista.forEach((elem)->{System.out.println(elem.toString());});
        //MenadzerPodataka.kreirajKomitenta("naziv", "adresa", 1);
        
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queuePodsistem1);
        //JMSConsumer consumer_podsistem3 = context.createConsumer(queuePodsistem1,"izvor = 'podsistem3'");
        System.out.println("Check");
        Thread nitZaUslugeKlijenta = new Thread(() -> {
            while (true) {             
                
                Message msg = consumer.receive();
                if(msg instanceof TextMessage){
                    TextMessage txtMsg=(TextMessage)msg;
                    try {
                        System.out.println("primljenaPoruka sa kodom "+txtMsg.getText());
                    } catch (JMSException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        switch(txtMsg.getText()){
                    case "1":
                        String naziv = txtMsg.getStringProperty("naziv");
                        String postanskiBroj = txtMsg.getStringProperty("postanskiBroj");
                        TextMessage returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                        try{
                            Mesto mesto = MenadzerPodataka.kreirajMesto(naziv, postanskiBroj);
                            returnMsg.setText("Upisano mesto id: " + mesto.getIdmesto()+" naziv: "+mesto.getNaziv()+" postanskiBr: "+mesto.getPostanskiBroj());
                        }catch(Exception e){
                            returnMsg.setText(e.toString());
                            
                        }
                        producer.send(queueServer, returnMsg);
                        System.out.println("Odgovor Poslat");
                        break;
                    case "2":
                        naziv = txtMsg.getStringProperty("naziv");
                        String adresa =txtMsg.getStringProperty("adresa");
                        int idMesta = txtMsg.getIntProperty("idMesta");
                        returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                        try{
                        Filijala filijala = MenadzerPodataka.kreirajFilijaju(naziv, adresa, idMesta);
                        returnMsg.setText("Uspesno kreirana filijala id: "+filijala.getIdfilijala()+" naziv: "+filijala.getNaziv()+" adresa: "+filijala.getAdresa());
                        }catch(Exception e){
                        returnMsg.setText(e.toString());
                        }
                        producer.send(queueServer, returnMsg);
                        System.out.println("Odgovor Poslat");
                        
                        break;
                    case "3":
                        naziv = txtMsg.getStringProperty("naziv");
                        adresa =txtMsg.getStringProperty("adresa");
                        idMesta = txtMsg.getIntProperty("idMesta");
                        returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                        
                        try{
                            Komitent komitent = MenadzerPodataka.kreirajKomitenta(naziv, adresa, idMesta);
                            returnMsg.setText("Uspesno kreiran komitent id: "+komitent.getIdkomitent()+" naziv: "+komitent.getNaziv()+" adresa: "+komitent.getAdresa());
                            returnMsg.setBooleanProperty("desilaSeGreska",false);
                        }catch(Exception e){
                            returnMsg.setText(e.toString());
                            returnMsg.setBooleanProperty("desilaSeGreska",true);
                        }
                        producer.send(queueServer, returnMsg);
                        System.out.println("Odgovor Poslat");
                        break;
                    case "4":
                            int idK = txtMsg.getIntProperty("idK");
                            int idM = txtMsg.getIntProperty("idMesta");
                            try{
                                String rezultat = MenadzerPodataka.promenaSedistaKomitenta(idK, idM);
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"),rezultat);
                            }catch(Exception e){
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"),e.toString());
                            }
                            producer.send(queueServer, returnMsg);
                            System.out.println("Odgovor Poslat");
                    break;
                    case "10":
                        ArrayList<Mesto> mesta = MenadzerPodataka.dohvatiSvaMesta();
                        ObjectMessage objMsg = kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),mesta);
                        producer.send(queueServer, objMsg);
                         System.out.println("Odgovor Poslat");
                        
                        break;
                    case "11":
                        ArrayList<Filijala> filijale = MenadzerPodataka.dohvatiSveFilijale();
                        objMsg =kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),filijale);
                        producer.send(queueServer, objMsg);
                         System.out.println("Odgovor Poslat");
                        
                        break;   
                     
                    case "12":
                        ArrayList<Komitent> komitenti = MenadzerPodataka.dohvatiSveKomitente();
                        objMsg =kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),komitenti);
                        producer.send(queueServer, objMsg);
                         System.out.println("Odgovor Poslat");
                        break;
                    case "Posalji promene":
                        System.out.println("Id naredbe: "+txtMsg.getIntProperty("idOperacije"));
                        objMsg= kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),MenadzerPodataka.promene);
                        MenadzerPodataka.promene.forEach(p->{
                            System.out.println(p.toString());
                        });
                        producer.send(queuePodsistem3, objMsg);
                        //JMSConsumer consumer_podsistem3 = context.createConsumer(queuePodsistem1,"idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem3'");
                        //TextMessage odgovor = (TextMessage)consumer_podsistem3.receive();
                        //System.out.println(odgovor.getText());
                        MenadzerPodataka.promene=new ArrayList<>();
                        System.out.println("Kreiranje Rezerve gotovo");
                        break;
                    case "Postoji filijala":
                            boolean postoji = MenadzerPodataka.postojiFilijala(txtMsg.getIntProperty("idFil"));
                            returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                            if(postoji)returnMsg.setText("postoji");
                            else returnMsg.setText("ne postoji");
                            producer.send(queueServer, returnMsg);
                        break;

                    case "16":      
                        returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"),MenadzerPodataka.promene());
                        producer.send(queueServer, returnMsg);
                        System.out.println("Odgovor Poslat");
                    break;
                   /* 
                    
                    
                    
                    case "11":break;
                    case "12":break;*/
                    default: System.out.println("Nepoznata komanda");
                    }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                    
                }
            }
 
        });
        nitZaUslugeKlijenta.start();
        
    }
    
}
