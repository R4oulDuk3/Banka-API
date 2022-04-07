package entitetiSistem2;

import entitetiSistem2.Komitent;
import entitetiSistem2.Prenos;
import entitetiSistem2.Transakcija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-02-03T02:35:27")
@StaticMetamodel(Racun.class)
public class Racun_ { 

    public static volatile SingularAttribute<Racun, Double> dozvoljenMinus;
    public static volatile SingularAttribute<Racun, Integer> idracun;
    public static volatile SingularAttribute<Racun, Double> stanje;
    public static volatile SingularAttribute<Racun, Date> vreme;
    public static volatile SingularAttribute<Racun, String> blokiranost;
    public static volatile SingularAttribute<Racun, Integer> brTransakcija;
    public static volatile ListAttribute<Racun, Transakcija> transakcijaList;
    public static volatile ListAttribute<Racun, Prenos> prenosList;
    public static volatile SingularAttribute<Racun, Komitent> idKomitenta;

}