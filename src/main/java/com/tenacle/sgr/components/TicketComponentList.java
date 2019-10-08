/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Ticket;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author samuel
 */
public class TicketComponentList extends MVerticalLayout {

    PaymentComponent pComponent = new PaymentComponent();

    Button button = new Button("New Ticket", VaadinIcons.PLUS);

    public TicketComponentList() {
        button.addStyleName(ValoTheme.BUTTON_HUGE);
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.setWidth("40%");
        button.addClickListener(l -> {
            removeComponent(button);
            TicketComponent t = new TicketComponent(null, this);
            add(t, Alignment.MIDDLE_CENTER)
                    .add(button, Alignment.MIDDLE_CENTER);
        });
        add(button, Alignment.MIDDLE_CENTER).withMargin(false);
    }

    public void addTicketComponent(Ticket ticket) {
        removeComponent(button);
        TicketComponent t = new TicketComponent(ticket, this);
        add(t, Alignment.MIDDLE_CENTER)
                .add(button, Alignment.MIDDLE_CENTER);
    }

}
