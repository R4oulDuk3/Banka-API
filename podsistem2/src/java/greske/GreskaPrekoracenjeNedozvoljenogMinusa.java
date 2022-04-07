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
public class GreskaPrekoracenjeNedozvoljenogMinusa extends Exception{
    
    public GreskaPrekoracenjeNedozvoljenogMinusa(){
        super("Greska: Nije moguca isplata zeljene sume, jer prelazi nedozovljeni minus");
    }
    
}
