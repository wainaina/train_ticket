/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.trips;

import com.tenacle.sgr.entities.Location;
import com.tenacle.sgr.entities.Train;
import com.tenacle.sgr.entities.Trip;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.utils.DateUtils;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Used to create trips.
 *
 * @author samuel
 */
public class TripComponent extends MVerticalLayout {

    //init trains and points.
    List<Location> points = DB.getInstance().get(Location.class).findAll();
    List<Train> trains = DB.getInstance().get(Train.class).findAll();

    //locations from and to
    ComboBox<Location> from = new ComboBox<Location>("From", points);
    ComboBox<Location> to = new ComboBox<Location>("To", points);
    ComboBox<Train> train = new ComboBox<Train>("Train", trains);

    //date field
    DateTimeField date = new DateTimeField("Select Date");

    Trip trip;
    Label label = new Label("Enter Trip Details", ContentMode.HTML);

    public TripComponent(Trip trip) {
        this.trip = trip;

        if (this.trip == null) {
            this.trip = new Trip();
        }

        label.addStyleName(ValoTheme.LABEL_H3);

        add(
                new MFormLayout()
                        .with(
                                label,
                                train,
                                new MHorizontalLayout(from, to).withCaption("Select Route"),
                                date)
        );

        train.setRequiredIndicatorVisible(true);
        train.addValueChangeListener(f -> {
            trip.setTrain(train.getValue());
        });

        from.setRequiredIndicatorVisible(true);
        from.addValueChangeListener(f -> {
            trip.setTripStart(from.getValue());
        });

        to.setRequiredIndicatorVisible(true);
        to.addValueChangeListener(f -> {
            trip.setTripStop(to.getValue());
        });

        date.setRequiredIndicatorVisible(true);
        date.addValueChangeListener(f -> {
            trip.setDate(DateUtils.asDate(date.getValue()));
        });

    }

    public Trip getTrip() {
        return trip;
    }

}
