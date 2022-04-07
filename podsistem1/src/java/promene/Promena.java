/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promene;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gavrilo
 */

public abstract class Promena implements Serializable{
    Serializable promenjenObjekat;
    public Promena(Serializable promenjenObjekat){
        this.promenjenObjekat=promenjenObjekat;
    }
    public abstract void izvrsiPromenu(EntityManagerFactory efm );
    
}
