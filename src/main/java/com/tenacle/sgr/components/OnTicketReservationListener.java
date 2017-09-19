/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Ticket;
import java.util.List;

/**
 * Registers Seat Selection Events.
 *
 * @author samuel
 */
public interface OnTicketReservationListener {

    /**
     * 
     * 
     * @param selectedTickets - list of the selected tickets.
     * @param amount 
     */
    void onTicketSelectionChanged(List<Ticket> selectedTickets, double amount);
}
