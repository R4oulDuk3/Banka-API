/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package promene;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Gavrilo
 */
public class Update extends Promena {

    public Update(Serializable promenjenObjekat) {
        super(promenjenObjekat);
    }

    @Override
    public void izvrsiPromenu(EntityManagerFactory efm ) {
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(promenjenObjekat);
        transaction.commit();
        
    }
    @Override
    public String toString() {
        return "Update " + promenjenObjekat.toString();
    }
    
}
