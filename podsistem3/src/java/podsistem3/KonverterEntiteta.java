/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entitetiSistem1.*;
import entitetiSistem2.Isplata;
import entitetiSistem2.Prenos;
import entitetiSistem2.Racun;
import entitetiSistem2.Transakcija;
import entitetiSistem2.Uplata;
import java.util.ArrayList;


/**
 *
 * @author Gavrilo
 */
public class KonverterEntiteta {
    public static String mestaUTekst(ArrayList<Mesto> mesta){
        StringBuilder sb= new StringBuilder();
        sb.append("Mesto\nidmesto\tnaziv\tpostanskiBroj\n");
        
        mesta.forEach(mesto->{
            sb.append(mesto.getIdmesto()+"\t"+mesto.getNaziv()+"\t"+mesto.getPostanskiBroj()+"\n");
        });
        return sb.toString();
    }
    public static String filijaleUTekst(ArrayList<Filijala> filijale){
        StringBuilder sb= new StringBuilder();
        sb.append("Filijala\nidfil\tnaziv\t\tadresa\t\tidMesto\n");
        
        filijale.forEach(f->{
            sb.append(f.getIdfilijala()+"\t"+f.getNaziv()+"\t\t"+f.getAdresa()+"\t\t"+f.getIdMesto().getIdmesto()+"\n");
        });
        return sb.toString();
    }
    public static String komitentiUTekst(ArrayList<Komitent> komitenti){
        StringBuilder sb= new StringBuilder();
        sb.append("Komitent\nidkomitent\tnaziv\tadresa\tidMesto\n");
        
        komitenti.forEach(k->{
            sb.append(k.getIdkomitent()+"\t"+k.getNaziv()+"\t"+k.getAdresa()+"\t"+k.getIdMesto().getIdmesto()+"\n");
        });
        return sb.toString();
    }
    public static String racuniUTekst(ArrayList<Racun> racuni){
        StringBuilder sb= new StringBuilder();
        sb.append("Racun\nidracun\tstanje\tblokiranost\tvreme\t\t\t\tbrTr\tidKom\tdozvoljenMinus\n");
        
        racuni.forEach(r->{
            sb.append(r.getIdracun()+"\t"+r.getStanje()+"\t"+r.getBlokiranost()+"\t"+r.getVreme()+"\t\t\t\t"+r.getBrTransakcija()+"\t"+r.getIdKomitenta().getIdkomitent()
                    +"\t"+r.getDozvoljenMinus()+"\n");
        });
        return sb.toString();
    }
    public static String transakcijeUTekst(ArrayList<Transakcija> transakcije){
        StringBuilder sb= new StringBuilder();
        sb.append("Transakcija\nidtr\tvreme\t\t\t\tiznos\trdBroj\tsvrha\tidRacunaSa\n");
        
        transakcije.forEach(t->{
            sb.append(t.getIdtransakcija()+"\t"+t.getVreme()+"\t\t\t"+t.getIznos()+"\t"+t.getRdBroj()+"\t"+t.getSvrha()+"\t"+t.getIdRacunaSa().getIdracun()+"\n");
        });
        sb.append("Uplata\niduplata\tidFilijale\n");
        transakcije.forEach(t->{
            Uplata u=t.getUplata();
            if(u!=null)sb.append(u.getIduplata()+"\t"+u.getIdFilijale()+"\n");
        });
        sb.append("Isplata\nidisplata\tidFilijale\n");
        transakcije.forEach(t->{
            Isplata i=t.getIsplata();
            if(i!=null)sb.append(i.getIdisplata()+"\t"+i.getIdFilijala()+"\n");
        });
        sb.append("Prenos\nidprenos\tidRacunNa\n");
        transakcije.forEach(t->{
            Prenos p=t.getPrenos();
            if(p!=null)sb.append(p.getIdprenos()+"\t"+p.getIdRacunaNa().getIdracun()+"\n");
        });
        
        return sb.toString();
    }
    
    
}
