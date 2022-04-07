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
import javax.persistence.ManyToOne;
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
@Table(name = "prenos", catalog = "sistem2bazapodataka", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prenos.findAll", query = "SELECT p FROM Prenos p"),
    @NamedQuery(name = "Prenos.findByIdprenos", query = "SELECT p FROM Prenos p WHERE p.idprenos = :idprenos")})
public class Prenos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idprenos")
    private Integer idprenos;
    @JoinColumn(name = "idRacunaNa", referencedColumnName = "idracun")
    @ManyToOne(optional = false)
    private Racun idRacunaNa;
    @JoinColumn(name = "idprenos", referencedColumnName = "idtransakcija", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Prenos() {
    }

    public Prenos(Integer idprenos) {
        this.idprenos = idprenos;
    }

    public Integer getIdprenos() {
        return idprenos;
    }

    public void setIdprenos(Integer idprenos) {
        this.idprenos = idprenos;
    }

    public Racun getIdRacunaNa() {
        return idRacunaNa;
    }

    public void setIdRacunaNa(Racun idRacunaNa) {
        this.idRacunaNa = idRacunaNa;
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
        hash += (idprenos != null ? idprenos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prenos)) {
            return false;
        }
        Prenos other = (Prenos) object;
        if ((this.idprenos == null && other.idprenos != null) || (this.idprenos != null && !this.idprenos.equals(other.idprenos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Prenos[ idprenos=" + idprenos + " ]";
    }
    
}
