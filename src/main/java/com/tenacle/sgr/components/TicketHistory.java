/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.persistence.tools.DatabaseController;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import java.util.List;

/**
 *
 * @author samuel
 */
public class TicketHistory extends TicketComponentList implements View {

    //getting a list of tickets that this user has.
    String phoneNumber = "254715574335";

    DatabaseController<Ticket> ticketDS = DB.getInstance().get(Ticket.class);

    //query the ticket databasen to get the tickets.
    List<Ticket> tickets = null;

    public TicketHistory() {

//        this.phoneNumber = phoneNumber;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event); //To change body of generated methods, choose Tools | Templates.

        //fetch the tickets.
        tickets = ticketDS
                .getEntityManager()
                .createNamedQuery("Ticket.findByPhoneNumber")
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        for (Ticket ticket : tickets) {
            //add a ticket.
            super.addTicketComponent(ticket);
        }
    }

}
