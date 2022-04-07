/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package podsistem1;

import entitetiSistem1.Filijala;
import entitetiSistem1.Komitent;
import entitetiSistem1.Mesto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import promene.*;


/**
 *
 * @author Gavrilo
 */
public class MenadzerPodataka {
    static EntityManagerFactory efm = Persistence.createEntityManagerFactory("podsistem1PU");
    static ArrayList<Promena> promene=new ArrayList<>();
    
    
    static Mesto kreirajMesto(String naziv, String postanskiBroj){
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Mesto mesto = new Mesto();
        mesto.setNaziv(naziv);
        mesto.setPostanskiBroj(postanskiBroj);
        promene.add(new Create(mesto));
        return (Mesto) sacuvajEntitet(mesto);
        
    }
    static Filijala kreirajFilijaju(String naziv,String adresa,int idMesto){
        Filijala filijala=new Filijala();
        filijala.setNaziv(naziv);
        filijala.setAdresa(adresa);
        EntityManager em = efm.createEntityManager();
        Mesto m = em.getReference(Mesto.class, idMesto);
        filijala.setIdMesto(m);
        promene.add(new Create(filijala));
        return (Filijala) sacuvajEntitet(filijala);
        
    }
    static boolean postojiFilijala(int idFil){
        EntityManager em = efm.createEntityManager();
        Filijala f = em.find(Filijala.class, idFil);
        return f!=null;
        
    }
    static Komitent kreirajKomitenta(String naziv,String adresa,int idMesto){
        Komitent komitent = new Komitent();
        komitent.setNaziv(naziv);
        komitent.setAdresa(adresa);
        EntityManager em = efm.createEntityManager();
        komitent.setIdMesto(em.getReference(Mesto.class, idMesto));
        //promene.add(new Create(komitent));
        return (Komitent) sacuvajEntitet(komitent);
    
    }
    static Object sacuvajEntitet(Object entity){
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            
            transaction.begin();
            em.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }finally{
            if(transaction.isActive())transaction.rollback();
            em.close();
        }
    }
    static ArrayList<Mesto> dohvatiSvaMesta(){
        EntityManager em = efm.createEntityManager();
        TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> list = query.getResultList();
        ArrayList<Mesto> aList = new ArrayList<>();
        list.forEach((elem)->{
            //elem.getFilijalaList();
            //elem.getKomitentList();
            //elem.getIdmesto();
            //elem.getNaziv();
            //elem.getPostanskiBroj();
            aList.add(elem);});
        return aList;
    }
    static ArrayList<Filijala> dohvatiSveFilijale(){
        EntityManager em = efm.createEntityManager();
        TypedQuery<Filijala> query = em.createNamedQuery("Filijala.findAll", Filijala.class);
        List<Filijala> list = query.getResultList();
        ArrayList<Filijala> aList = new ArrayList<>();
        list.forEach((elem)->{
            aList.add(elem);
        });
        return aList;
    }
    static ArrayList<Komitent> dohvatiSveKomitente(){
        EntityManager em = efm.createEntityManager();
        TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findAll", Komitent.class);
        List<Komitent> list = query.getResultList();
        ArrayList<Komitent> aList = new ArrayList<>();
        list.forEach((elem)->{aList.add(elem);});
        return aList;
    }
    static String promenaSedistaKomitenta(int idKomitent,int idMesto){
        EntityManager em=efm.createEntityManager();
        Komitent komitent = em.find(Komitent.class, idKomitent);
        Mesto mesto = em.find(Mesto.class, idMesto);
        
        //TODO: if(komitent==null ||mesto==null)throw Exception
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        komitent.setIdMesto(mesto);
        transaction.commit();
        promene.add(new Update(komitent));
        return "Uspeh";
    }
    static String promene(){
        StringBuilder sb = new StringBuilder();
        promene.forEach(p->{
            sb.append(p.toString()+"\n");
        });
        return sb.toString();
    }
   
}
