/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entitetiSistem2.Racun;
import entitetiSistem2.Uplata;
import entitetiSistem2.Isplata;
import entitetiSistem2.Transakcija;
import entitetiSistem2.Prenos;
import entitetiSistem2.Komitent;
import greske.GreskaPrekoracenjeNedozvoljenogMinusa;
import greske.GreskaRacunBlokiran;
import greske.GreskaZatvoren;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import promene.*;

/**
 *
 * @author Gavrilo
 */
public class MenadzerPodataka {
    static EntityManagerFactory efm = Persistence.createEntityManagerFactory("podsistem2PU");
    static ArrayList<Promena> promene=new ArrayList<>();
    
    static Komitent kreirajKomitenta(String naziv,String adresa,int idMesta){
        Komitent komitent = new Komitent();
        komitent.setNaziv(naziv);
        komitent.setAdresa(adresa);
        EntityManager em = efm.createEntityManager();
        komitent.setIdMesto(idMesta);
        promene.add(new Create(komitent));
        return (Komitent) sacuvajEntitet(komitent);
    }
    static Racun otvoriRacun(int idKomitenta,double dozvoljenMinus) throws GreskaRacunBlokiran{
        Racun racun = new Racun();
        EntityManager em = efm.createEntityManager();
        Komitent kom = em.find(Komitent.class, idKomitenta);
        
        List<Racun> racunList = kom.getRacunList();
        boolean blokiran=false;
        for(Racun r:racunList){
            if(r.getBlokiranost().equals("blokiran"))throw new GreskaRacunBlokiran();
        }
        
        racun.setIdKomitenta(kom);
        
        racun.setDozvoljenMinus(dozvoljenMinus);
        racun.setBlokiranost("neblokiran");
        racun.setVreme(new Date());
        promene.add(new Create(racun));
        return (Racun) sacuvajEntitet(racun);
    }
    static void zatvoriRacun(int idRacuna){
        EntityManager em = efm.createEntityManager();
        
        Racun racun = em.find(Racun.class, idRacuna);
        //TODO: if result==null throw new Exception
        /*List<Transakcija> transakcijaList = racun.getTransakcijaList();
        
        for(Transakcija t: transakcijaList){
            obrisiEntitet(t.getIsplata());
            obrisiEntitet(t.getUplata());
            obrisiEntitet(t.getPrenos());
            obrisiEntitet(t);
        }*/
        
        em.getTransaction().begin();
        racun.setBlokiranost("zatvoren");
        em.getTransaction().commit();
        promene.add(new Update(racun));
    }
    static void obrisiEntitet(Serializable entitet){
        if(entitet==null)return;
        EntityManager em = efm.createEntityManager();
        em.getTransaction().begin();
        em.remove(entitet);
        em.getTransaction().commit();
        promene.add(new Delete(entitet));
        System.out.println("Obrisan "+ entitet.toString());
    }
    /*public static void main(String[] args){
        try {
            kreirajUplatu(10,2000.0,"mora se",3);
            try {
                kreirajIsplatu(10,2000.0,"mora se",3);
                
            } catch (GreskaRacunBlokiran ex) {
                Logger.getLogger(MenadzerPodataka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GreskaPrekoracenjeNedozvoljenogMinusa ex) {
                Logger.getLogger(MenadzerPodataka.class.getName()).log(Level.SEVERE, null, ex);
            }
            kreirajPrenos(10, 10,2000, "mora see");
            
            
        } catch (GreskaRacunBlokiran ex) {
            Logger.getLogger(MenadzerPodataka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GreskaPrekoracenjeNedozvoljenogMinusa ex) {
            Logger.getLogger(MenadzerPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }*/
    static Uplata kreirajUplatu(int idRacuna,double iznos,String svrha,int idFilijale) throws GreskaZatvoren{
        EntityManager em = efm.createEntityManager();
        Racun racun = em.find(Racun.class, idRacuna);
        if(racun.getBlokiranost().equals("zatvoren") ) throw new GreskaZatvoren();
        
        
        Transakcija transakcija = new Transakcija();
        transakcija.setIznos(iznos);
        transakcija.setSvrha(svrha);
        transakcija.setRdBroj(racun.getBrTransakcija()+1);
        transakcija.setIdRacunaSa(racun);
        transakcija.setVreme(new Date());
        transakcija  = (Transakcija) sacuvajEntitet(transakcija);
        
        
        System.out.println("transakcija id "+transakcija.getIdtransakcija());
      
        Uplata uplata = new Uplata();
        uplata.setIdFilijale(idFilijale);
        uplata.setIduplata(transakcija.getIdtransakcija());
        uplata.setTransakcija(transakcija);
      
        uplata=(Uplata) sacuvajEntitet(uplata);
        
        if(transakcija!=null && uplata !=null){
            promeniStanjeRacuna(racun,iznos,em);
            promene.add(new Update(racun));
            /*em.getTransaction().begin();
            System.out.println("racun stanje pre: "+racun.getStanje());
            racun.setStanje(racun.getStanje()+iznos);
            System.out.println("racun stanje posle: "+racun.getStanje());
            if(racun.getStanje()>-racun.getDozvoljenMinus())racun.setBlokiranost("neblokiran");
            racun.setBrTransakcija(racun.getBrTransakcija()+1);
            em.getTransaction().commit();*/
        }
        promene.add(new Create(transakcija));
        promene.add(new Create(uplata));
        return uplata;
    }
    static Isplata kreirajIsplatu(int idRacuna,double iznos,String svrha,int idFilijale) throws GreskaRacunBlokiran, GreskaPrekoracenjeNedozvoljenogMinusa, GreskaZatvoren{
        EntityManager em = efm.createEntityManager();
        Racun racun = em.find(Racun.class, idRacuna);
        if(racun.getBlokiranost().equals("blokiran") ) throw new GreskaRacunBlokiran();
        if(racun.getBlokiranost().equals("zatvoren") ) throw new GreskaZatvoren();
        if(racun.getStanje()-iznos<-racun.getDozvoljenMinus()*2) throw new GreskaPrekoracenjeNedozvoljenogMinusa();
        
        Transakcija transakcija = new Transakcija();
        //isplata.setTransakcija(transakcija);
        transakcija.setIznos(iznos);
        transakcija.setSvrha(svrha);
        transakcija.setRdBroj(racun.getBrTransakcija()+1);
        
        transakcija.setIdRacunaSa(racun);
        transakcija.setVreme(new Date());
        
        transakcija  = (Transakcija) sacuvajEntitet(transakcija);
        
        Isplata isplata = new Isplata();
        isplata.setIdFilijala(idFilijale);
        isplata.setIdisplata(transakcija.getIdtransakcija());
        isplata.setTransakcija(transakcija);
        isplata=(Isplata) sacuvajEntitet(isplata);
        
        if(transakcija!=null && isplata !=null){
            promeniStanjeRacuna(racun,-iznos,em);
            promene.add(new Update(racun));
            /*em.getTransaction().begin();
            System.out.println("racun stanje pre: "+racun.getStanje());
            racun.setStanje(racun.getStanje()-iznos);
            System.out.println("racun stanje posle: "+racun.getStanje());
            if(racun.getStanje()<=-racun.getDozvoljenMinus())racun.setBlokiranost("blokiran");
            racun.setBrTransakcija(racun.getBrTransakcija()+1);
            em.getTransaction().commit();*/
        }
        promene.add(new Create(transakcija));
        promene.add(new Create(isplata));
        System.out.println("check greska 1");
        return isplata;
    }
    static Prenos kreirajPrenos(int idRacunaSa, int idRacunaNa,double iznos, String svrha) throws GreskaRacunBlokiran, GreskaPrekoracenjeNedozvoljenogMinusa, GreskaZatvoren{
        EntityManager em = efm.createEntityManager();
        Racun racunSa = em.find(Racun.class, idRacunaSa);
        Racun racunNa = em.find(Racun.class, idRacunaNa);
        if(racunSa.getBlokiranost().equals("blokiran") ) throw new GreskaRacunBlokiran();
        if(racunSa.getBlokiranost().equals("zatvoren") ) throw new GreskaZatvoren();
        if(racunNa.getBlokiranost().equals("zatvoren") ) throw new GreskaZatvoren();
        if(racunSa.getStanje()-iznos<-racunSa.getDozvoljenMinus()*2) throw new GreskaPrekoracenjeNedozvoljenogMinusa();
        
        
        Transakcija transakcija = new Transakcija();
        //prenos.setTransakcija(transakcija);
        transakcija.setIznos(iznos);
        transakcija.setSvrha(svrha);
        transakcija.setRdBroj(racunSa.getBrTransakcija()+1);
        
        transakcija.setIdRacunaSa(racunSa);
        transakcija.setVreme(new Date());
        transakcija  = (Transakcija) sacuvajEntitet(transakcija);
        
        Prenos prenos = new Prenos();
        prenos.setIdRacunaNa(racunNa);
        prenos.setIdprenos(transakcija.getIdtransakcija());
        prenos.setTransakcija(transakcija);
        
        prenos = (Prenos) sacuvajEntitet(prenos);
        if(transakcija!=null && prenos!=null){
            promeniStanjeRacuna(racunSa,-iznos,em);
            promeniStanjeRacuna(racunNa,iznos,em);
            promene.add(new Update(racunSa));
            promene.add(new Update(racunNa));
        }
        promene.add(new Create(transakcija));
        promene.add(new Create(prenos));
        return prenos;
        
    }
    static void promeniStanjeRacuna(Racun racun,double iznos,EntityManager em){
            em.getTransaction().begin();
            System.out.println("racun stanje pre: "+racun.getStanje());
            racun.setStanje(racun.getStanje()+iznos);
            System.out.println("racun stanje posle: "+racun.getStanje());
            if(racun.getStanje()<=-racun.getDozvoljenMinus())racun.setBlokiranost("blokiran");
            if(racun.getStanje()>-racun.getDozvoljenMinus())racun.setBlokiranost("neblokiran");
            racun.setBrTransakcija(racun.getBrTransakcija()+1);
            
            em.getTransaction().commit();
    }
    static ArrayList<Racun> dohvatiSveRacuneZaKomitenta(int idKomitent){
        EntityManager em = efm.createEntityManager();
        Komitent komitent = em.find(Komitent.class, idKomitent);
        ArrayList<Racun> aList = new ArrayList<>();
        List<Racun> list = komitent.getRacunList();
        list.forEach((elem)->{
            //elem.getFilijalaList();
            //elem.getKomitentList();
            //elem.getIdmesto();
            //elem.getNaziv();
            //elem.getPostanskiBroj();
            aList.add(elem);});
        return aList;
    }
    static ArrayList<Transakcija> dohvatiSveTransakcijeZaRacun(int idRacun){
        EntityManager em = efm.createEntityManager();
        Racun racun = em.find(Racun.class, idRacun);
        ArrayList<Transakcija> aList = new ArrayList<>();
        List<Transakcija> list = racun.getTransakcijaList();
        list.forEach((elem)->{
            //elem.getFilijalaList();
            //elem.getKomitentList();
            //elem.getIdmesto();
            //elem.getNaziv();
            System.out.println(elem.toString());
            Isplata isplata = elem.getIsplata();
            if(isplata!=null)System.out.println(isplata.toString());
            Uplata uplata = elem.getUplata();
            if(uplata!=null)System.out.println(uplata.toString());
            Prenos prenos = elem.getPrenos();
            if(prenos!=null)System.out.println(prenos.toString());
            aList.add(elem);});
        return aList;
    }
    static Object sacuvajEntitet(Object entity){
        
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            
            transaction.begin();
            boolean isValid = checkIfValid(entity);
            if(isValid)em.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            System.out.println("Greska: "+ e.toString());
            return null;
        }finally{
            if(transaction.isActive())transaction.rollback();
            em.close();
        }
    }
    static boolean sacuvajEntitete(ArrayList<Object> entities){
        
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            
            transaction.begin();
            entities.forEach(
                    (e)->{
                    boolean isValid = checkIfValid(e);
                    if(isValid)em.persist(e);
                    }
            );
            
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Greska: "+ e.toString());
            return false;
        }finally{
            if(transaction.isActive())transaction.rollback();
            em.close();
        }
    }
    static String promene(){
        StringBuilder sb = new StringBuilder();
        promene.forEach(p->{
            sb.append(p.toString()+"\n");
        });
        return sb.toString();
    }
    
    
    
    
    public static boolean checkIfValid(Object entity) {        
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);
    if (constraintViolations.size() > 0 ) { 
       System.out.println("Constraint Violations occurred.."); 
       for (ConstraintViolation<Object> contraints : constraintViolations) {
            System.out.println(contraints.getRootBeanClass().getSimpleName()+
            "." + contraints.getPropertyPath() + " " + contraints.getMessage());
        }        
        //getEntityManager().persist(entity);
        return false;
    }
    return true;
}
    
}
