/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entitetiSistem1.Filijala;
import entitetiSistem1.Komitent;
import entitetiSistem1.Mesto;
import entitetiSistem2.Isplata;
import entitetiSistem2.Prenos;
import entitetiSistem2.Racun;
import entitetiSistem2.Transakcija;
import entitetiSistem2.Uplata;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import promene.Promena;

/**
 *
 * @author Gavrilo
 */
public class MenadzerPodataka {
    static EntityManagerFactory efm = Persistence.createEntityManagerFactory("podsistem3PU");
    static ArrayList<Promena> promene=new ArrayList<>();
    
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
    static ArrayList<Racun> dohvatiSveRacuneZaKomitenta(int idKomitent){
        EntityManager em = efm.createEntityManager();
        entitetiSistem2.Komitent komitent = em.find(entitetiSistem2.Komitent.class, idKomitent);
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
            Isplata isplata = elem.getIsplata();
            if(isplata!=null)System.out.println(isplata.toString());
            Uplata uplata = elem.getUplata();
            if(uplata!=null)System.out.println(uplata.toString());
            Prenos prenos = elem.getPrenos();
            if(prenos!=null)System.out.println(prenos.toString());
            aList.add(elem);});
        return aList;
    }
    static String kopijaBaze(){
        
        ArrayList<Mesto> mesta = dohvatiSvaMesta();
        
        
        ArrayList<Filijala> fil = dohvatiSveFilijale();
        
        ArrayList<Komitent> kom = dohvatiSveKomitente();
        
        ArrayList<Racun> racuni= new ArrayList<>();
        ArrayList<Transakcija> tr= new ArrayList<>();
        kom.forEach(k->{
            ArrayList<Racun> rac = dohvatiSveRacuneZaKomitenta(k.getIdkomitent());
            racuni.addAll(rac);
            rac.forEach(r->{
                ArrayList<Transakcija> t = dohvatiSveTransakcijeZaRacun(r.getIdracun());
                tr.addAll(t);
            });
            
        });
        String s1 =KonverterEntiteta.mestaUTekst(mesta);
        //System.out.println(s1);
        String s2 = KonverterEntiteta.filijaleUTekst(fil);
        //System.out.println(s2);
        String s3 = KonverterEntiteta.komitentiUTekst(kom);
        //System.out.println(s3);
        String s4 = KonverterEntiteta.racuniUTekst(racuni);
        //System.out.println(s4);
        String s5 = KonverterEntiteta.transakcijeUTekst(tr);
        //System.out.println(s5);
        
        
        return s1+s2+s3+s4+s5;
    }
    public static void main(String[] args){
        //System.out.println(());
        kopijaBaze();
    }
}
