/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitetiSistem1;

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
@Entity
@Table(name = "mesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mesto.findAll", query = "SELECT m FROM Mesto m"),
    @NamedQuery(name = "Mesto.findByIdmesto", query = "SELECT m FROM Mesto m WHERE m.idmesto = :idmesto"),
    @NamedQuery(name = "Mesto.findByNaziv", query = "SELECT m FROM Mesto m WHERE m.naziv = :naziv"),
    @NamedQuery(name = "Mesto.findByPostanskiBroj", query = "SELECT m FROM Mesto m WHERE m.postanskiBroj = :postanskiBroj")})
public class Mesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmesto")
    private Integer idmesto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "postanskiBroj")
    private String postanskiBroj;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMesto")
    private List<Filijala> filijalaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMesto")
    private List<Komitent> komitentList;

    public Mesto() {
    }

    public Mesto(Integer idmesto) {
        this.idmesto = idmesto;
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

    @XmlTransient
    public List<Filijala> getFilijalaList() {
        return filijalaList;
    }

    public void setFilijalaList(List<Filijala> filijalaList) {
        this.filijalaList = filijalaList;
    }

    @XmlTransient
    public List<Komitent> getKomitentList() {
        return komitentList;
    }

    public void setKomitentList(List<Komitent> komitentList) {
        this.komitentList = komitentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmesto != null ? idmesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesto)) {
            return false;
        }
        Mesto other = (Mesto) object;
        if ((this.idmesto == null && other.idmesto != null) || (this.idmesto != null && !this.idmesto.equals(other.idmesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Mesto[ idmesto=" + idmesto + " ]";
    }
    
}
