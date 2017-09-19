/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.entities;

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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author samuel
 */
@Entity
@Table(name = "tarriff")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarriff.findAll", query = "SELECT t FROM Tarriff t")
    , @NamedQuery(name = "Tarriff.findById", query = "SELECT t FROM Tarriff t WHERE t.id = :id")
    , @NamedQuery(name = "Tarriff.findByFirstClassAmount", query = "SELECT t FROM Tarriff t WHERE t.firstClassAmount = :firstClassAmount")
    , @NamedQuery(name = "Tarriff.findByLocations", query = "SELECT t FROM Tarriff t WHERE t.fromDest.id = :from AND t.toDest.id = :to ")
    , @NamedQuery(name = "Tarriff.findByEconClassAmount", query = "SELECT t FROM Tarriff t WHERE t.econClassAmount = :econClassAmount")})
public class Tarriff implements Serializable, TenacleEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "first_class_amount")
    private double firstClassAmount;
    @Basic(optional = false)
    @Column(name = "econ_class_amount")
    private double econClassAmount;
    @JoinColumn(name = "from_dest", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location fromDest;
    @JoinColumn(name = "to_dest", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location toDest;

    public Tarriff() {
    }

    public Tarriff(Integer id) {
        this.id = id;
    }

    public Tarriff(Integer id, double firstClassAmount, double econClassAmount) {
        this.id = id;
        this.firstClassAmount = firstClassAmount;
        this.econClassAmount = econClassAmount;
    }

    public Tarriff(Location fromDest, Location toDest) {
        this.fromDest = fromDest;
        this.toDest = toDest;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public double getFirstClassAmount() {
        return firstClassAmount;
    }

    public void setFirstClassAmount(double firstClassAmount) {
        this.firstClassAmount = firstClassAmount;
    }

    public double getEconClassAmount() {
        return econClassAmount;
    }

    public void setEconClassAmount(double econClassAmount) {
        this.econClassAmount = econClassAmount;
    }

    public Location getFromDest() {
        return fromDest;
    }

    public void setFromDest(Location fromDest) {
        this.fromDest = fromDest;
    }

    public Location getToDest() {
        return toDest;
    }

    public void setToDest(Location toDest) {
        this.toDest = toDest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarriff)) {
            return false;
        }
        Tarriff other = (Tarriff) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tenacle.sgr.entities.Tarriff[ id=" + id + " ]";
    }

}
