/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promene;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gavrilo
 */
public class Delete extends Promena {

    public Delete(Serializable promenjenObjekat) {
        super(promenjenObjekat);
    }

    @Override
    public void izvrsiPromenu(EntityManagerFactory efm ) {
        
        EntityManager em = efm.createEntityManager();
        
        
        em.getTransaction().begin();
        em.remove(promenjenObjekat);
        em.getTransaction().commit();
        
    }
    @Override
    public String toString() {
        return "Delete " + promenjenObjekat.toString();
    }
    
}
