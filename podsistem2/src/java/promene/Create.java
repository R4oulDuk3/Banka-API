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
public class Create extends Promena {

    public Create(Serializable promenjenObjekat) {
        super(promenjenObjekat);
    }

    @Override
    public void izvrsiPromenu(EntityManagerFactory efm ) {
        EntityManager em = efm.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            
            transaction.begin();
            em.persist(promenjenObjekat);
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.toString());
        }finally{
            if(transaction.isActive())transaction.rollback();
            em.close();
        }
    }

    @Override
    public String toString() {
        return "Create " + promenjenObjekat.toString();
    }
    
    
}
