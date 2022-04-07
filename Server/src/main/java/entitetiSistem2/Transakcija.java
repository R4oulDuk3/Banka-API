/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entitetiSistem2;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gavrilo
 */
@Entity
@Table(name = "transakcija", catalog = "sistem2bazapodataka", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdtransakcija", query = "SELECT t FROM Transakcija t WHERE t.idtransakcija = :idtransakcija"),
    @NamedQuery(name = "Transakcija.findByVreme", query = "SELECT t FROM Transakcija t WHERE t.vreme = :vreme"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findByRdBroj", query = "SELECT t FROM Transakcija t WHERE t.rdBroj = :rdBroj"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtransakcija")
    private Integer idtransakcija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iznos")
    private double iznos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rdBroj")
    private int rdBroj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "svrha")
    private String svrha;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Isplata isplata;
    @JoinColumn(name = "idRacunaSa", referencedColumnName = "idracun")
    @ManyToOne
    private Racun idRacunaSa;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Prenos prenos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Uplata uplata;

    public Transakcija() {
    }

    public Transakcija(Integer idtransakcija) {
        this.idtransakcija = idtransakcija;
    }

    public Transakcija(Integer idtransakcija, Date vreme, double iznos, int rdBroj, String svrha) {
        this.idtransakcija = idtransakcija;
        this.vreme = vreme;
        this.iznos = iznos;
        this.rdBroj = rdBroj;
        this.svrha = svrha;
    }

    public Integer getIdtransakcija() {
        return idtransakcija;
    }

    public void setIdtransakcija(Integer idtransakcija) {
        this.idtransakcija = idtransakcija;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public int getRdBroj() {
        return rdBroj;
    }

    public void setRdBroj(int rdBroj) {
        this.rdBroj = rdBroj;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Isplata getIsplata() {
        return isplata;
    }

    public void setIsplata(Isplata isplata) {
        this.isplata = isplata;
    }

    public Racun getIdRacunaSa() {
        return idRacunaSa;
    }

    public void setIdRacunaSa(Racun idRacunaSa) {
        this.idRacunaSa = idRacunaSa;
    }

    public Prenos getPrenos() {
        return prenos;
    }

    public void setPrenos(Prenos prenos) {
        this.prenos = prenos;
    }

    public Uplata getUplata() {
        return uplata;
    }

    public void setUplata(Uplata uplata) {
        this.uplata = uplata;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransakcija != null ? idtransakcija.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idtransakcija == null && other.idtransakcija != null) || (this.idtransakcija != null && !this.idtransakcija.equals(other.idtransakcija))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idtransakcija=" + idtransakcija + " ]";
    }
    
}
