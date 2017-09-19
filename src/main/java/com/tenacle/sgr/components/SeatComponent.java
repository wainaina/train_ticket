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

        if (this.seat == null) {
            this.seat = new Seat();

            int a = r.nextInt(3);
            this.seat.setAvailability(new SeatAvailability((a == 0) ? 1 : a));

            a = r.nextInt(2);

            this.seat.setLabel("SEAT: " + a);            

            this.seat.setId(r.nextInt(1000));
        }

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
