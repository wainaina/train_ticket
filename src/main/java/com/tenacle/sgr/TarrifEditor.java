/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr;

import com.tenacle.sgr.entities.AbstractRepository;
import com.tenacle.sgr.entities.Location;
import com.tenacle.sgr.entities.Tarriff;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import org.vaadin.viritin.layouts.MGridLayout;
import tm.kod.widgets.numberfield.NumberField;

/**
 *
 * @author samuel
 */
public class TarrifEditor extends Panel {

    List<Location> loc = new AbstractRepository<Location>(Location.class).findAll();

    public TarrifEditor() {

        this.setSizeFull();
        
        Location[] arr = new Location[loc.size()];

        for (int i = 0; i < loc.size(); i++) {
            arr[i] = loc.get(i);
        }

        for (int i = 0; i < loc.size(); i++) {

            for (int j = 0; j < loc.size(); j++) {
                System.out.println("From: " + loc.get(i) + " To: " + loc.get(j));
                getTariffEditorComponent(new Tarriff(loc.get(i), loc.get(j)));
            }
        }
        
        setContent(g);
        
    }

    AbstractRepository<Tarriff> tarriffRepo = new AbstractRepository<Tarriff>(Tarriff.class);
    MGridLayout g = new MGridLayout(5, 1).withFullSize();

    void getTariffEditorComponent(Tarriff tarriff) {

        Tarriff t = tarriff;

        NumberField economy_amount = new NumberField();
        economy_amount.setDescription("Economy Class Ticket Price");
        NumberField first_amount = new NumberField();
        first_amount.setDescription("First Class Ticket Price");

        Button approve = new Button("Save");
        approve.addStyleName(ValoTheme.BUTTON_PRIMARY);
        approve.addClickListener(l -> {
            t.setEconClassAmount(Double.parseDouble(economy_amount.getValue()));
            t.setFirstClassAmount(Double.parseDouble(first_amount.getValue()));
            tarriffRepo.create(t);
        });

        Label from = new Label(tarriff.getFromDest() + "");
        from.addStyleName(ValoTheme.LABEL_H3);

        Label to = new Label(tarriff.getToDest() + "");
        to.addStyleName(ValoTheme.LABEL_H3);

        g.add(from, to, economy_amount, first_amount, approve);
    }
}
