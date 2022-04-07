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
public class GreskaZatvoren extends Exception{
    public GreskaZatvoren(){
        super("ERR:Nije moguc rad sa racunom jer je zatvoren");
    }
}
