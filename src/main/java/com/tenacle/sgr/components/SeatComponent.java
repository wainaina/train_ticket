/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.SeatAvailability;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Label;
import java.util.Random;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author samuel
 */
public class SeatComponent extends MVerticalLayout {

    Seat seat;

    ScaleImage image = new ScaleImage();
    Label seatNumber = new Label();

    Random r = new Random();

    public SeatComponent(Seat seat) {
        this.seat = seat;

        image.setWidth("40px");
        image.setHeight("40px");
        image.addStyleName("v-image");

        setImage();

        add(image);

        withMargin(false);
    }

    public Seat getSeat() {
        return seat;
    }
    boolean isSelected = false;
    boolean isBookable = true;
    boolean isOccupied = false;

    public void setSelected(boolean isSelected) {

        this.isSelected = isSelected;

        if (isSelected) {
            image.setSource(new ThemeResource("img/seat-selected.png"));
        } else {
            image.setSource(new ThemeResource("img/seat-unoccupied.png"));
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isBookable() {
        return isBookable;
    }

    public void setBookable(boolean bookable) {
        this.isBookable = bookable;
//        if (isBookable) {
//            image.setSource(new ThemeResource("img/seat-unoccupied.png"));
//        } else {
//            image.setSource(new ThemeResource("img/seat-unoccupied.png"));
//        }
    }

    public void setOccupied(boolean occupied) {
        this.isBookable = occupied;
        if (occupied) {
            image.setSource(new ThemeResource("img/seat-occupied.png"));
        } else {
            image.setSource(new ThemeResource("img/seat-unoccupied.png"));
        }
    }

    private void setImage() {
        if (null != seat.getAvailability().getId()) {
            switch (seat.getAvailability().getId()) {
                case 1://unreserved
                    image.setSource(new ThemeResource("img/seat-unoccupied.png"));
                    break;
                case 2://reserved
                    image.setSource(new ThemeResource("img/seat-occupied.png"));
                    break;
                case 3://unbookable
                    image.setSource(new ThemeResource("img/seat-occupied.png"));
                    break;
                default:
                    break;
            }
        }
    }

}
