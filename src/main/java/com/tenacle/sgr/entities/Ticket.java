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
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t")
    , @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.id = :id")
    , @NamedQuery(name = "Ticket.findBySerial", query = "SELECT t FROM Ticket t WHERE t.serial = :serial")})
public class Ticket implements Serializable, TenacleEntity {

    @JoinColumn(name = "from_location", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location fromLocation;
    @JoinColumn(name = "to_location", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location toLocation;

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
        return trip;
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

    public String toHtml() {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <title>Ticket</title>\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "    <link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n"
                + "    <header class=\"w3-container w3-light-grey\">   \n"
                + "        <style>\n"
                + "            p {\n"
                + "                font-family: \"Palatino Linotype\",\"Book Antiqua\",Palatino,serif;\n"
                + "                width: 100%;\n"
                + "            }\n"
                + "            table.cinereousTable {\n"
                + "                font-family: \"Palatino Linotype\",\"Book Antiqua\",Palatino,serif;\n"
                + "                width: 100%;\n"
                + "                background-color: #FDFFD5;"
                + "                text-align: center;\n"
                + "            }\n"
                + "            table.cinereousTable td, table.cinereousTable th {\n"
                + "                padding: 4px 4px;\n"
                + "            }\n"
                + "            table.cinereousTable tbody td {\n"
                + "                font-size: 13px;\n"
                + "            }\n"
                + "            table.cinereousTable thead {\n"
                + "                background: #948473;\n"
                + "                background: -moz-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: -webkit-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: linear-gradient(to bottom, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "            }\n"
                + "            table.cinereousTable thead th {\n"
                + "                font-size: 19px;\n"
                + "                font-weight: bold;\n"
                + "                color: #F0F0F0;\n"
                + "                text-align: left;\n"
                + "                border-left: 2px solid #948473;\n"
                + "            }\n"
                + "            table.cinereousTable thead th:first-child {\n"
                + "                border-left: none;\n"
                + "            }\n"
                + "\n"
                + "            table.cinereousTable tfoot {\n"
                + "                font-size: 16px;\n"
                + "                font-weight: bold;\n"
                + "                color: #F0F0F0;\n"
                + "                background: #948473;\n"
                + "                background: -moz-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: -webkit-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: linear-gradient(to bottom, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "            }\n"
                + "            table.cinereousTable tfoot td {\n"
                + "                font-size: 16px;\n"
                + "            }\n"
                + "            w3-light-grey {\n"
                + "            	background-color: #FFFFFF;\n"
                + "            }"
                + "        </style>\n"
                + "\n"
                + "    </header>\n"
                + "\n"
                + "\n"
                + "    <div class=\"w3-container\">\n"
                + "\n"
                + "        <div class=\"w3-card-4\" style=\"width:100%\">\n"
                + "\n"
                + "            <img \n"
                + "                src=\"https://www.appcoda.com/wp-content/uploads/2013/12/qrcode.jpg\" \n"
                + "                alt=\"Avatar\" \n"
                + "                class=\"w3-left  w3-margin-right\" \n"
                + "                style=\"\n"
                + "                width:100px; \n"
                + "                height:100px;\n"
                + "                margin: auto; \n"
                + "                display: block;\">\n"
                + "\n"
                + "            <h3 style=\"font-family: 'Times New Roman',Times,serif;\n"
                + "                font-size: 25px;\n"
                + "                text-align: center;\n"
                + "                letter-spacing: 3px;\n"
                + "                word-spacing: 3.6px;\n"
                + "                color: #000000;\n"
                + "                font-weight: normal;\n"
                + "                text-decoration: none;\n"
                + "                font-style: normal;\n"
                + "                font-variant: small-caps;\n"
                + "                text-transform: none; \">Samuel Wainaina</h3>     \n"
                + "           \n"
                + "\n"
                + "            <div class=\"w3-container\">   	  \n"
                + "                <hr>      \n"
                + "                <p style=\"text-align: center\">Return Trip Details</p>      \n"
                + "\n"
                + "                <table class=\"cinereousTable\">\n"
                + "                    <tbody>\n"
                + "\n"
                + "                        <tr>\n"
                + "                            <td>\n"
                + "                                <table class=\"cinereousTable\">\n"
                + "                                    <tbody>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Journey</b></td>\n"
                + "                                            <td style=\"text-align: left;\">Nairobi - Mombasa</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Date</b></td>\n"
                + "                                            <td style=\"text-align: left;\">12th August, 2017  8:00 am</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Seat</b></td>\n"
                + "                                            <td style=\"text-align: left;\">A3</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Amount</b></td>\n"
                + "                                            <td style=\"text-align: left;\">Kes. 700.00</td>\n"
                + "                                        </tr>\n"
                + "                                    </tbody>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "\n"
                + "                            <td>\n"
                + "                                                <table class=\"cinereousTable\">\n"
                + "                                    <tbody>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Journey</b></td>\n"
                + "                                            <td style=\"text-align: left;\">Nairobi - Mombasa</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Date</b></td>\n"
                + "                                            <td style=\"text-align: left;\">12th August, 2017  8:00 am</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Seat</b></td>\n"
                + "                                            <td style=\"text-align: left;\">B6</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Amount</b></td>\n"
                + "                                            <td style=\"text-align: left;\">Kes. 700.00</td>\n"
                + "                                        </tr>\n"
                + "                                    </tbody>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                    </tbody>\n"
                + "                </table>\n"
                + "                <hr>\n"
                + "                                <p style=\"text-align: center\">Please arrive at the station 40 minutes before depature</p>      \n"
                + "                <hr>\n"
                + "                <img src=\"http://krc.co.ke/wp-content/uploads/2016/01/logo_krc.png\" alt=\"Avatar\" class=\"w3-left w3-circle w3-margin-right\" style=\"width:40px\">    \n"
                + "                <p style=\"text-align: center\">Enjoy your trip</p>  \n"
                + "            </div>        \n"
                + "        </div>\n"
                + "    </div>\n"
                + "\n"
                + "</html>";
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

}
