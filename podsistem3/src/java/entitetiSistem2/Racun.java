/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitetiSistem2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gavrilo
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdracun", query = "SELECT r FROM Racun r WHERE r.idracun = :idracun"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje"),
    @NamedQuery(name = "Racun.findByBlokiranost", query = "SELECT r FROM Racun r WHERE r.blokiranost = :blokiranost"),
    @NamedQuery(name = "Racun.findByVreme", query = "SELECT r FROM Racun r WHERE r.vreme = :vreme"),
    @NamedQuery(name = "Racun.findByBrTransakcija", query = "SELECT r FROM Racun r WHERE r.brTransakcija = :brTransakcija"),
    @NamedQuery(name = "Racun.findByDozvoljenMinus", query = "SELECT r FROM Racun r WHERE r.dozvoljenMinus = :dozvoljenMinus")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idracun")
    private Integer idracun;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stanje")
    private double stanje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "blokiranost")
    private String blokiranost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "brTransakcija")
    private int brTransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dozvoljenMinus")
    private double dozvoljenMinus;
    @OneToMany(mappedBy = "idRacunaSa")
    private List<Transakcija> transakcijaList;
    @JoinColumn(name = "idKomitenta", referencedColumnName = "idkomitent")
    @ManyToOne(optional = false)
    private Komitent idKomitenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRacunaNa")
    private List<Prenos> prenosList;

    public Racun() {
    }

    public Racun(Integer idracun) {
        this.idracun = idracun;
    }

    public Racun(Integer idracun, double stanje, String blokiranost, Date vreme, int brTransakcija, double dozvoljenMinus) {
        this.idracun = idracun;
        this.stanje = stanje;
        this.blokiranost = blokiranost;
        this.vreme = vreme;
        this.brTransakcija = brTransakcija;
        this.dozvoljenMinus = dozvoljenMinus;
    }

    public Integer getIdracun() {
        return idracun;
    }

    public void setIdracun(Integer idracun) {
        this.idracun = idracun;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public String getBlokiranost() {
        return blokiranost;
    }

    public void setBlokiranost(String blokiranost) {
        this.blokiranost = blokiranost;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public int getBrTransakcija() {
        return brTransakcija;
    }

    public void setBrTransakcija(int brTransakcija) {
        this.brTransakcija = brTransakcija;
    }

    public double getDozvoljenMinus() {
        return dozvoljenMinus;
    }

    public void setDozvoljenMinus(double dozvoljenMinus) {
        this.dozvoljenMinus = dozvoljenMinus;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList() {
        return transakcijaList;
    }

    public void setTransakcijaList(List<Transakcija> transakcijaList) {
        this.transakcijaList = transakcijaList;
    }

    public Komitent getIdKomitenta() {
        return idKomitenta;
    }

    public void setIdKomitenta(Komitent idKomitenta) {
        this.idKomitenta = idKomitenta;
    }

    @XmlTransient
    public List<Prenos> getPrenosList() {
        return prenosList;
    }

    public void setPrenosList(List<Prenos> prenosList) {
        this.prenosList = prenosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idracun != null ? idracun.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idracun == null && other.idracun != null) || (this.idracun != null && !this.idracun.equals(other.idracun))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Racun[ idracun=" + idracun + " ]";
    }
    
}
