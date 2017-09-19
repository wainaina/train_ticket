/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.Ticket;
import java.util.List;

/**
 * Registers Seat Selection Events.
 *
 * @author samuel
 */
public interface OnSeatSelectedListener {
    /**
     * 
     * @param selectedTickets - seats selected from the car.
     */
    void onSeatSelectionChanged(List<Seat> selectedTickets);
}
