/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.trips;

import com.tenacle.sgr.entities.Trip;
import com.tenacle.sgr.utils.DateUtils;
import com.vaadin.icons.VaadinIcons;
import java.time.ZonedDateTime;
import java.util.Date;
import org.vaadin.addon.calendar.item.BasicItem;

public class TripItem extends BasicItem {

    Trip trip;

    public TripItem(Trip trip) {
        super(trip.getTrain().toString(), trip.toString(), DateUtils.asZonedDateTime(trip.getDate()), DateUtils.asZonedDateTime(trip.getDate()));
        this.trip = trip;
        
        Date end = this.trip.getDate();
        end.setHours(end.getHours() + 5);
        super.setEnd(DateUtils.asZonedDateTime(end));

    }

    public Trip getTrip() {
        return this.trip;
    }

    @Override
    public int hashCode() {
        return trip.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripItem)) {
            return false;
        }
        TripItem that = (TripItem) o;
        return getTrip().equals(that.getTrip());
    }

    @Override
    public String getDateCaptionFormat() {
        //return CalendarItem.RANGE_TIME;
        return VaadinIcons.CLOCK.getHtml() + " %s<br>"
                + VaadinIcons.ARROW_CIRCLE_RIGHT_O.getHtml() + " %s";
    }

}
