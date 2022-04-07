/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greske;

/**
 *
 * @author Gavrilo
 */
public class GreskaRacunBlokiran extends Exception{
    public GreskaRacunBlokiran(){
        super("Nije moguca isplata jer je ovaj racun blokiran");
    }
}
