/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.entities;

import com.google.common.base.Preconditions;
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
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t")
    , @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.id = :id")
    , @NamedQuery(name = "Ticket.findByPhoneNumber", query = "SELECT t FROM Ticket t WHERE t.customer.phoneNumber = :phoneNumber")    
    , @NamedQuery(name = "Ticket.findBySerial", query = "SELECT t FROM Ticket t WHERE t.serial = :serial")})
public class Ticket implements Serializable, TenacleEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private String serial;
    @JoinColumn(name = "trip", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Trip trip;
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "ticket_status", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TicketStatus ticketStatus;
    @JoinColumn(name = "seat", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seat seat;
    @JoinColumn(name = "ticket_type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TicketType ticketType;
    @JoinColumn(name = "return_seat", referencedColumnName = "id")
    @ManyToOne
    private Seat returnSeat;
    @JoinColumn(name = "return_trip", referencedColumnName = "id")
    @ManyToOne
    private Trip returnTrip;
    @JoinColumn(name = "from_location", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location fromLocation;
    @JoinColumn(name = "to_location", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location toLocation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "from_amount")
    private Double fromAmount = 0.0;
    @Column(name = "to_amount")
    private Double toAmount = 0.0;
    @JoinColumn(name = "payment", referencedColumnName = "id")
    @ManyToOne
    private Payment payment;

    public Ticket() {
    }

    public Ticket(Integer id) {
        this.id = id;
    }

    public Ticket(Integer id, String serial, Trip trip, Customer customer, TicketStatus ticketStatus, Seat seat, TicketType ticketType, Seat returnSeat, Trip returnTrip) {
        this.id = id;
        this.serial = serial;
        this.trip = trip;
        this.customer = customer;
        this.ticketStatus = ticketStatus;
        this.seat = seat;
        this.ticketType = ticketType;
        this.returnSeat = returnSeat;
        this.returnTrip = returnTrip;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
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
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return seat + " " + ((isEconomy()) ? "ECONOMY CLASS" : "FIRST CLASS" + " - " + ((isReturn()) ? "RETURN" : "SINGLE"));
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getReturnSeat() {
        return returnSeat;
    }

    public void setReturnSeat(Seat returnSeat) {
        this.returnSeat = returnSeat;
    }

    public Trip getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(Trip returnTrip) {
        this.returnTrip = returnTrip;
    }

    public boolean isReturn() {
        return this.ticketType.getId() == 2;
    }

    public boolean isEconomy() {
        return seat.getTrainCar().getTrainClass().getId() == 2;
    }

    public boolean isReturnEconomy() {
        return returnSeat.getTrainCar().getTrainClass().getId() == 2;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public Double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

}
