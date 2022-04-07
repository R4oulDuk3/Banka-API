package entitetiSistem2;

import entitetiSistem2.Racun;
import entitetiSistem2.Transakcija;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-02-02T17:10:35")
@StaticMetamodel(Prenos.class)
public class Prenos_ { 

    public static volatile SingularAttribute<Prenos, Integer> idprenos;
    public static volatile SingularAttribute<Prenos, Transakcija> transakcija;
    public static volatile SingularAttribute<Prenos, Racun> idRacunaNa;

}