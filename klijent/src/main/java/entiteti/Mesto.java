/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gavrilo
 */
@XmlRootElement
public class Mesto {
    private Integer idmesto;
    private String naziv;
    private String postanskiBroj;

    public Mesto() {
    }

    @Override
    public String toString() {
        return "Mesto{" + "idmesto=" + idmesto + ", naziv=" + naziv + ", postanskiBroj=" + postanskiBroj + '}';
    }

    public Mesto(Integer idmesto, String naziv, String postanskiBroj) {
        this.idmesto = idmesto;
        this.naziv = naziv;
        this.postanskiBroj = postanskiBroj;
    }

    public Integer getIdmesto() {
        return idmesto;
    }

    public void setIdmesto(Integer idmesto) {
        this.idmesto = idmesto;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }
    
}
