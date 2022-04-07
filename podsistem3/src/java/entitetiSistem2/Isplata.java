/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitetiSistem2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gavrilo
 */
@Entity
@Table(name = "isplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Isplata.findAll", query = "SELECT i FROM Isplata i"),
    @NamedQuery(name = "Isplata.findByIdisplata", query = "SELECT i FROM Isplata i WHERE i.idisplata = :idisplata"),
    @NamedQuery(name = "Isplata.findByIdFilijala", query = "SELECT i FROM Isplata i WHERE i.idFilijala = :idFilijala")})
public class Isplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idisplata")
    private Integer idisplata;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFilijala")
    private int idFilijala;
    @JoinColumn(name = "idisplata", referencedColumnName = "idtransakcija", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Isplata() {
    }

    public Isplata(Integer idisplata) {
        this.idisplata = idisplata;
    }

    public Isplata(Integer idisplata, int idFilijala) {
        this.idisplata = idisplata;
        this.idFilijala = idFilijala;
    }

    public Integer getIdisplata() {
        return idisplata;
    }

    public void setIdisplata(Integer idisplata) {
        this.idisplata = idisplata;
    }

    public int getIdFilijala() {
        return idFilijala;
    }

    public void setIdFilijala(int idFilijala) {
        this.idFilijala = idFilijala;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idisplata != null ? idisplata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Isplata)) {
            return false;
        }
        Isplata other = (Isplata) object;
        if ((this.idisplata == null && other.idisplata != null) || (this.idisplata != null && !this.idisplata.equals(other.idisplata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Isplata[ idisplata=" + idisplata + " ]";
    }
    
}
