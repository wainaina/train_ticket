/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Payment;
import com.tenacle.sgr.entities.PaymentMode;
import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.persistence.tools.DB;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.themes.ValoTheme;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author samuel
 */
public class PaymentComponent extends MVerticalLayout {

    //holds the list of tickets
    List<Ticket> tickets = new ArrayList<Ticket>();

    //get All payment modes
    List<PaymentMode> paymentModes = DB.getInstance().get(PaymentMode.class).findAll();

    //one payment method
    ComboBox<PaymentMode> paymentMethod = new ComboBox<>("Select Payment Method", paymentModes);

    ListSelect<Ticket> selectedTickets = new ListSelect("Tickets");

    PaymentMode paymentMode = null;
    Label amountToPay = new Label("", ContentMode.HTML);

    NumberFormat format = NumberFormat.getInstance();

    double amount = 0.0;

    Payment payment = new Payment();

    public PaymentComponent() {
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        paymentMethod.addStyleName(ValoTheme.COMBOBOX_LARGE);

        paymentMethod.addValueChangeListener(p -> {
            this.paymentMode = paymentMethod.getValue();

            payment.setPaymentMethod(this.paymentMode);
        });

        add(new MHorizontalLayout()
                .add(selectedTickets)
                .add(new MVerticalLayout()
                        .add(amountToPay, paymentMethod)));
    }

    public void addTicket(Ticket ticket) {

        //check if ticket exists and remove it
        //this is to enusre the list contains the updated ticket list
        if (tickets.contains(ticket)) {
            removeTicket(ticket);
        }

        amount += ticket.getFromAmount() + ticket.getToAmount();
        tickets.add(ticket);
        changeAmountLabel(amount);
        payment.setAmount(amount);
        selectedTickets.setItems(tickets);
    }

    public void removeTicket(Ticket ticket) {
        synchronized (this) {
            amount -= ticket.getFromAmount() + ticket.getToAmount();
            tickets.remove(ticket);
            changeAmountLabel(amount);
            payment.setAmount(amount);
            selectedTickets.setItems(tickets);

        }
    }

    private void changeAmountLabel(double amount) {
        amountToPay.setCaption("Pay Kes. " + format.format(amount) + " with");
        amountToPay.setContentMode(ContentMode.HTML);
    }

    //returns the list of tickets.
    public List<Ticket> getTickets() {
        return tickets;
    }

    public Payment getPayment() {
        return payment;
    }

}
