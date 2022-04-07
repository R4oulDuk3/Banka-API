/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entitetiSistem2.Isplata;
import entitetiSistem2.Komitent;
import entitetiSistem2.Prenos;
import entitetiSistem2.Racun;
import entitetiSistem2.Transakcija;
import entitetiSistem2.Uplata;
import greske.GreskaPrekoracenjeNedozvoljenogMinusa;
import greske.GreskaRacunBlokiran;
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

/**
 *
 * @author Gavrilo
 */
public class Main {

     @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    
    
    @Resource(lookup = "QueueServer")
    private static Queue queueServer;
    
    @Resource(lookup = "QueuePodsistem2")
    private static Queue queuePodsistem2;
    
    @Resource(lookup = "QueuePodsistem1")
    private static Queue queuePodsistem1;
    
     @Resource(lookup = "QueuePodsistem3")
    private static Queue queuePodsistem3;
    
    
    public static TextMessage kreirajPoruku( JMSContext context) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "podsistem2");
        txtMsg.setIntProperty("idOperacije", random());
        return txtMsg;
    }
    public static TextMessage kreirajPoruku( JMSContext context,int idOperacije) throws JMSException{
        TextMessage txtMsg = context.createTextMessage();
        txtMsg.setStringProperty("izvor", "podsistem2");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static TextMessage kreirajPoruku(JMSContext context,int idOperacije,String text) throws JMSException{
        TextMessage txtMsg = context.createTextMessage(text);
        txtMsg.setStringProperty("izvor", "podsistem2");
        txtMsg.setIntProperty("idOperacije", idOperacije);
        return txtMsg;
    }
    public static ObjectMessage kreirajObjektnuPoruku(JMSContext context,int idOperacije,Serializable object) throws JMSException{
         ObjectMessage objMsg = context.createObjectMessage(object);
        objMsg.setStringProperty("izvor", "podsistem2");
        objMsg.setIntProperty("idOperacije", idOperacije);
        return objMsg;
    }
    public static int random(){
    
        return (int)(Math.random()*1000000);
    }
    
    
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(queuePodsistem2);
        
        System.out.println("Rad podsistema 2 zapocet");
        //MenadzerPodataka.dohvatiSveTransakcijeZaRacun(2);
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
                            case "3":
                                String naziv = txtMsg.getStringProperty("naziv");
                                String adresa =txtMsg.getStringProperty("adresa");
                                int idMesta = txtMsg.getIntProperty("idMesta");
                                TextMessage returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    Komitent komitent = MenadzerPodataka.kreirajKomitenta(naziv, adresa, idMesta);
                                    returnMsg.setText("Uspesno kreiran komitent id: "+komitent.getIdkomitent()+" naziv: "+komitent.getNaziv()+" adresa: "+komitent.getAdresa());
                                }catch(Exception e){
                                    returnMsg.setText(e.toString());
                                }
                                //returnMsg.setStringProperty("izvor", "podsistem2");
                                producer.send(queueServer, returnMsg);
                                System.out.println("Odgovor Poslat");
                                break;
                                
                            case "5":
                                int idKomitenta = txtMsg.getIntProperty("idKomitenta");
                                double dozvoljenMinus =txtMsg.getDoubleProperty("dozvoljenMinus");
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    Racun racun = MenadzerPodataka.otvoriRacun(idKomitenta, dozvoljenMinus);
                                    returnMsg.setText("Kreiran racun id: " + racun.getIdracun()+" komitent: "+racun.getIdKomitenta().toString()+" dozvMinus: "+racun.getDozvoljenMinus());
                                }catch(Exception e){
                                    returnMsg.setText("Greska!");
                                }
                                producer.send(queueServer, returnMsg);
                                System.out.println("Odgovor Poslat");
                            break;
                            case "6":
                                int idRacun = txtMsg.getIntProperty("idRacun");
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    MenadzerPodataka.zatvoriRacun(idRacun);
                                    returnMsg.setText("Uspesno obrisan racun");
                                }catch(Exception e){
                                    returnMsg.setText(e.toString());
                                }
                                producer.send(queueServer, returnMsg);
                                System.out.println("Odgovor Poslat");
                            break;
                            case "7":
                                
                                int idRacunSa = txtMsg.getIntProperty("idRacunSa");
                                int idRacunNa = txtMsg.getIntProperty("idRacunNa");
                                double iznos =txtMsg.getDoubleProperty("iznos");
                                String svrha = txtMsg.getStringProperty("svrha");
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    Prenos prenos = MenadzerPodataka.kreirajPrenos(idRacunSa, idRacunNa, iznos, svrha);
                                    returnMsg.setText("Prenos sa "+prenos.getTransakcija().getIdRacunaSa()+" na "+prenos.getIdRacunaNa()+" uspesno kreiran");
                                    
                                }
                                catch(Exception e){
                                    returnMsg.setText(e.toString());
                                }
                                
                                producer.send(queueServer, returnMsg);
                            System.out.println("Odgovor Poslat");
                            break;
                            case "8":
                                
                                idRacun = txtMsg.getIntProperty("idRacun");
                                int idFilijalije = txtMsg.getIntProperty("idFilijalije");
                                //int idRacunNa = txtMsg.getIntProperty("idRacunNa");
                                iznos =txtMsg.getDoubleProperty("iznos");
                                svrha = txtMsg.getStringProperty("svrha");
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    Uplata uplata = MenadzerPodataka.kreirajUplatu(idRacun, iznos, svrha, idFilijalije);
                                    returnMsg.setText("Uplata na "+uplata.getTransakcija().getIdRacunaSa()+" iznosa: "+uplata.getTransakcija().getIznos()+" uspesno kreiran");
                                }
                                catch(Exception e){
                                    returnMsg.setText(e.toString());
                                }
                                
                                producer.send(queueServer, returnMsg);
                            System.out.println("Odgovor Poslat");
                            break;
                            case "9":
                                idRacun = txtMsg.getIntProperty("idRacun");
                                idFilijalije = txtMsg.getIntProperty("idFilijalije");
                                //int idRacunNa = txtMsg.getIntProperty("idRacunNa");
                                iznos =txtMsg.getDoubleProperty("iznos");
                                svrha = txtMsg.getStringProperty("svrha");
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"));
                                try{
                                    Isplata isplata = MenadzerPodataka.kreirajIsplatu(idRacun, iznos, svrha, idFilijalije);
                                    returnMsg.setText("Isplata sa "+isplata.getTransakcija().getIdRacunaSa()+" iznosa: "+isplata.getTransakcija().getIznos()+" uspesno kreirana");
                                }catch(Exception e){
                                    returnMsg.setText(e.toString());
                                }
                                
                                producer.send(queueServer, returnMsg);
                            System.out.println("Odgovor Poslat");
                            break;
                            case "13":
                                idKomitenta = txtMsg.getIntProperty("idKomitent");
                                ArrayList<Racun> racuni = MenadzerPodataka.dohvatiSveRacuneZaKomitenta(idKomitenta);
                                ObjectMessage objMsg =kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),racuni);
                                producer.send(queueServer, objMsg);
                                System.out.println("Odgovor Poslat");
                            break;
                            case "14":
                                idRacun = txtMsg.getIntProperty("idRacun");
                                ArrayList<Transakcija> transakcije = MenadzerPodataka.dohvatiSveTransakcijeZaRacun(idRacun);
                                objMsg =kreirajObjektnuPoruku(context,txtMsg.getIntProperty("idOperacije"),transakcije);
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
                            //JMSConsumer consumer_podsistem3 = context.createConsumer(queuePodsistem2,"idOperacije= "+txtMsg.getIntProperty("idOperacije")+" AND izvor= 'podsistem3'");
                            //TextMessage odgovor = (TextMessage)consumer_podsistem3.receive();
                            //System.out.println(odgovor.getText());
                            MenadzerPodataka.promene=new ArrayList<>();
                            System.out.println("Kreiranje Rezerve gotovo");                    
                            break;
                            case "16":
                                
                                returnMsg = kreirajPoruku(context,txtMsg.getIntProperty("idOperacije"),MenadzerPodataka.promene());
                                producer.send(queueServer, returnMsg);
                                System.out.println("Odgovor Poslat");
                                break;


                            
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
