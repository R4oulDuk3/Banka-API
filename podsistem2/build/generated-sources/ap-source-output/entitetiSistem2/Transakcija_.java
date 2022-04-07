package entitetiSistem2;

import entitetiSistem2.Isplata;
import entitetiSistem2.Prenos;
import entitetiSistem2.Racun;
import entitetiSistem2.Uplata;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-02-02T17:10:35")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, Isplata> isplata;
    public static volatile SingularAttribute<Transakcija, Double> iznos;
    public static volatile SingularAttribute<Transakcija, String> svrha;
    public static volatile SingularAttribute<Transakcija, Integer> rdBroj;
    public static volatile SingularAttribute<Transakcija, Date> vreme;
    public static volatile SingularAttribute<Transakcija, Racun> idRacunaSa;
    public static volatile SingularAttribute<Transakcija, Integer> idtransakcija;
    public static volatile SingularAttribute<Transakcija, Prenos> prenos;
    public static volatile SingularAttribute<Transakcija, Uplata> uplata;

}