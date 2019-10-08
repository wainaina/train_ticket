/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.358113065148404
 */
package com.tenacle.sgr.trips;

import com.github.appreciated.material.MaterialTheme;
//import static com.tenacle.sgr.entities.Train_.label;
import com.tenacle.sgr.entities.Trip;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.persistence.tools.DatabaseController;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.List;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author samuel
 */
public class TripSchedule extends MVerticalLayout implements View {

    final TripDataProvider tripDataprovider = new TripDataProvider();
    final Calendar calendar = new Calendar("Trip Schedule", tripDataprovider);
    final DatabaseController<Trip> tripDS = DB.getInstance().get(Trip.class);
    final List<Trip> trips = tripDS.findAll();
    Trip trip = new Trip();

    public TripSchedule() {
        
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event); //To change body of generated methods, choose Tools | Templates.   
        trips.forEach(e -> {
            tripDataprovider.addItem(new TripItem(e));
        });

        calendar.setHeightUndefined();
        calendar.setWidth(100, Unit.PERCENTAGE);
        calendar.withVisibleHours(6, 21);

        Label label = new Label("Train Schedule", ContentMode.TEXT);
        label.addStyleName(ValoTheme.LABEL_H1);

        Button addTrip = new Button("Add Trip", VaadinIcons.PLUS);
        addTrip.addStyleName(ValoTheme.BUTTON_PRIMARY);

        addTrip.addClickListener(c -> {

            MessageBox.create()
                    .withCancelButton(
                            () -> {
                                trip = null;
                                //nullify the trip to avoid any persistence
                            },
                            ButtonOption.caption("Cancel"),
                            ButtonOption.closeOnClick(true)
                    )
                    .withOkButton(
                            () -> {
                                //create the trip
                                trip = tripDS.create(trip);
                                //add the trip to the calendar
                                tripDataprovider.addItem(new TripItem(trip));
                            },
                            ButtonOption.caption("Add Trip"),
                            ButtonOption.icon(VaadinIcons.PLUS)
                    )
                    .withMessage(new TripComponent(trip))
                    .asModal(true)
                    .open();

        });

        add(new MHorizontalLayout().add(label, 1).add(addTrip).withUndefinedHeight().withWidth(100, Unit.PERCENTAGE))
                .add(calendar, 1).withFullSize();
    }
    
    
}
