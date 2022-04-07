/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitetiSistem2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gavrilo
 */
@Entity(name = "Komitent2")
@Table(name = "komitent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Komitent2.findAll", query = "SELECT k FROM Komitent2 k"),
    @NamedQuery(name = "Komitent2.findByIdkomitent", query = "SELECT k FROM Komitent2 k WHERE k.idkomitent = :idkomitent"),
    @NamedQuery(name = "Komitent2.findByNaziv", query = "SELECT k FROM Komitent2 k WHERE k.naziv = :naziv"),
    @NamedQuery(name = "Komitent2.findByAdresa", query = "SELECT k FROM Komitent2 k WHERE k.adresa = :adresa"),
    @NamedQuery(name = "Komitent2.findByIdMesto", query = "SELECT k FROM Komitent2 k WHERE k.idMesto = :idMesto")})
public class Komitent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idkomitent")
    private Integer idkomitent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "adresa")
    private String adresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idMesto")
    private int idMesto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idKomitenta")
    private List<Racun> racunList;

    public Komitent() {
    }

    public Komitent(Integer idkomitent) {
        this.idkomitent = idkomitent;
    }

    public Komitent(Integer idkomitent, String naziv, String adresa, int idMesto) {
        this.idkomitent = idkomitent;
        this.naziv = naziv;
        this.adresa = adresa;
        this.idMesto = idMesto;
    }

    public Integer getIdkomitent() {
        return idkomitent;
    }

    public void setIdkomitent(Integer idkomitent) {
        this.idkomitent = idkomitent;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getIdMesto() {
        return idMesto;
    }

    public void setIdMesto(int idMesto) {
        this.idMesto = idMesto;
    }

    @XmlTransient
    public List<Racun> getRacunList() {
        return racunList;
    }

    public void setRacunList(List<Racun> racunList) {
        this.racunList = racunList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idkomitent != null ? idkomitent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Komitent)) {
            return false;
        }
        Komitent other = (Komitent) object;
        if ((this.idkomitent == null && other.idkomitent != null) || (this.idkomitent != null && !this.idkomitent.equals(other.idkomitent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Komitent[ idkomitent=" + idkomitent + " ]";
    }
    
}
