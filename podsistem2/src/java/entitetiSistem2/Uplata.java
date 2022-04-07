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
@Table(name = "uplata", catalog = "sistem2bazapodataka", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uplata.findAll", query = "SELECT u FROM Uplata u"),
    @NamedQuery(name = "Uplata.findByIduplata", query = "SELECT u FROM Uplata u WHERE u.iduplata = :iduplata"),
    @NamedQuery(name = "Uplata.findByIdFilijale", query = "SELECT u FROM Uplata u WHERE u.idFilijale = :idFilijale")})
public class Uplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduplata")
    private Integer iduplata;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFilijale")
    private int idFilijale;
    @JoinColumn(name = "iduplata", referencedColumnName = "idtransakcija", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Uplata() {
    }

    public Uplata(Integer iduplata) {
        this.iduplata = iduplata;
    }

    public Uplata(Integer iduplata, int idFilijale) {
        this.iduplata = iduplata;
        this.idFilijale = idFilijale;
    }

    public Integer getIduplata() {
        return iduplata;
    }

    public void setIduplata(Integer iduplata) {
        this.iduplata = iduplata;
    }

    public int getIdFilijale() {
        return idFilijale;
    }

    public void setIdFilijale(int idFilijale) {
        this.idFilijale = idFilijale;
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
        hash += (iduplata != null ? iduplata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uplata)) {
            return false;
        }
        Uplata other = (Uplata) object;
        if ((this.iduplata == null && other.iduplata != null) || (this.iduplata != null && !this.iduplata.equals(other.iduplata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Uplata[ iduplata=" + iduplata + " ]";
    }
    
}
