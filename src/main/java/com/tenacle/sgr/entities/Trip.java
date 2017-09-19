/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.entities;

import com.tenacle.sgr.utils.DateUtils;
import com.vaadin.icons.VaadinIcons;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.vaadin.addon.calendar.item.BasicItem;
import org.vaadin.addon.calendar.item.BasicItemProvider;

/**
 *
 * @author samuel
 */
@Entity
@Table(name = "trip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trip.findAll", query = "SELECT t FROM Trip t")
    , @NamedQuery(name = "Trip.findById", query = "SELECT t FROM Trip t WHERE t.id = :id")
    , @NamedQuery(name = "Trip.findByDate", query = "SELECT t FROM Trip t WHERE t.date = :date")
    , @NamedQuery(name = "Trip.findByFROMandTOandDate", query = "SELECT t FROM Trip t WHERE t.tripStart.id = :fromLocation AND t.tripStop.id = :toLocation and t.date between :dateStart and :dateStop")
})
public class Trip extends BasicItem implements Serializable, TenacleEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "trip_start", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location tripStart;
    @JoinColumn(name = "trip_stop", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location tripStop;
    @JoinColumn(name = "train", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Train train;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    private List<Ticket> ticketList;

    public Trip() {
    }

    public Trip(Integer id) {
        this.id = id;
    }

    public Trip(Integer id, Date date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getTripStart() {
        return tripStart;
    }

    public void setTripStart(Location tripStart) {
        this.tripStart = tripStart;
    }

    public Location getTripStop() {
        return tripStop;
    }

    public void setTripStop(Location tripStop) {
        this.tripStop = tripStop;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @XmlTransient
    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
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
        if (!(object instanceof Trip)) {
            return false;
        }
        Trip other = (Trip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.tripStart + " to " + this.tripStop + " on " + this.date + " [" + this.train + "]";
    }   
}
