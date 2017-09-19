/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import static com.tenacle.sgr.entities.Payment_.amount;
import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.Tarriff;
import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.entities.TicketStatus;
import com.tenacle.sgr.entities.TicketType;
import com.tenacle.sgr.entities.Train;
import com.tenacle.sgr.entities.TrainCar;
import com.tenacle.sgr.entities.Trip;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.HashMap;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author samuel
 */
public class SeatSelectionComponent extends MVerticalLayout {

    Train train;

    Trip trip;

    Ticket ticket;

    HashMap<Seat, Ticket> seatsSelected = new HashMap();

    boolean seatEditMode;

    boolean ticketBookingMode;

    double ticket_amount = 0.0;

    boolean isSingleSelectionMode;

    /**
     * Displays a seat Selection Component without a ticket Overlay.
     *
     * Used for management operations.
     *
     * @param train
     */
    public SeatSelectionComponent(Train train) {
        this.train = train;
        this.seatEditMode = true;
    }

    /**
     * Displays a seat selection Component with a ticket Overlay.
     *
     * Used for ticket booking.
     *
     * @param ticket - Holds the Ticket Information.
     * @param tarriff
     * @param isReturnSelection
     */
    public SeatSelectionComponent(Ticket ticket, Tarriff tarriff, boolean isReturnSelection, boolean isSingleSelectionMode) {
        this.ticket = ticket;
        this.tarriff = tarriff;
        this.ticketBookingMode = true;
        this.isReturnSelection = isReturnSelection;
        this.isSingleSelectionMode = isSingleSelectionMode;
        init();

        System.out.println("Number of Coaches: " + ticket.getTrip().getTrain().getTrainCarList().size());
    }

    /**
     * Set up the Complete Train Selection Component.
     */
    private void init() {

        carPanel.setHeightUndefined();
        carPanel.setWidthUndefined();
        add(getCoachNavigator())
                .add(carPanel, 1);
    }

    Panel carPanel = new Panel();
    Button previous = new Button(VaadinIcons.ANGLE_LEFT);
    Button next = new Button(VaadinIcons.ANGLE_RIGHT);

    Label trainCar = new Label("Coach 2 - FIRST CLASS");

    int current_coach = 0;
    TrainCar car = null;

    private Component getCoachNavigator() {
        previous.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        next.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        current_coach = 0;
        car = this.ticket.getTrip().getTrain().getTrainCarList().get(current_coach);
        carPanel.setContent(getCar(car));
        trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.toString());

        previous.addClickListener(p -> {

            if ((current_coach > 0)) {
                current_coach--;
                System.out.println("Current Coach: " + current_coach);
                car = this.ticket.getTrip().getTrain().getTrainCarList().get(current_coach);
                carPanel.setContent(getCar(car));
                trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.getTrainClass().toString());
            }
        });

        next.addClickListener(n -> {

            if ((current_coach < this.ticket.getTrip().getTrain().getTrainCarList().size())) {
                current_coach++;
                car = this.ticket.getTrip().getTrain().getTrainCarList().get(current_coach);
                carPanel.setContent(getCar(car));
                System.out.println("Current Coach: " + current_coach);
                trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.getTrainClass().toString());
            }
        });

        return new MHorizontalLayout(previous, trainCar, next).withMargin(false);
    }

    Tarriff tarriff;

    boolean isReturnSelection;

    public Component getCar(TrainCar car) {
        MGridLayout grid = (car.isEconomy()) ? new MGridLayout(10, 6) : new MGridLayout(10, 5);

        for (int i = 0; i < car.getSeatList().size(); i++) {

//            if (car.isEconomy() && i >= 30 && i < 40) {//get seat array from train.
//                ScaleImage image = new ScaleImage();
//                grid.add(image);
//                image.setHeight("40px");
//                image.setWidth("40px");
//                image.setSource(new ThemeResource("img/space.png"));
//                continue;
//
//            } else if (i >= 20 && i < 30) {
//                ScaleImage image = new ScaleImage();
//                grid.add(image);
//                image.setHeight("40px");
//                image.setWidth("40px");
//                image.setSource(new ThemeResource("img/space.png"));
//                continue;
//            }
            SeatComponent seat = new SeatComponent(car.getSeatList().get(i));

            seat.addLayoutClickListener(l -> {

                //enable single selection mode.
                if (isSingleSelectionMode && !seatsSelected.isEmpty() && !seatsSelected.containsKey(seat.getSeat())) {
                    return;
                }
                
                //open pop up to show seat information here
                if (seat.getSeat().getAvailability().getId() == 1) {

                    //set the opposite state.
                    seat.setSelected(!seat.isSelected());

                    if (seat.isSelected()) {

                        double amount_ = 0;

                        //get the amount from the tarrif
                        amount_ = (car.isEconomy()) ? this.tarriff.getEconClassAmount() : this.tarriff.getFirstClassAmount();

                        if (!isReturnSelection) {
                            ticket.setSeat(seat.getSeat());
                            ticket.setTrip(trip);
                            ticket.setTicketType(new TicketType(1));
                            seatsSelected.put(seat.getSeat(), null);
                        } else {

                            //select ticket
                            ticket.setReturnSeat(seat.getSeat());
                            ticket.setReturnTrip(trip);
                            ticket.setTicketType(new TicketType(2));
                            seatsSelected.put(seat.getSeat(), null);
                        }

                        ticket_amount += amount_;

                    } else {

                        Ticket ticket = seatsSelected.get(seat.getSeat());

                        double amount_ = 0;

                        amount_ = (this.ticket.isEconomy()) ? this.tarriff.getEconClassAmount() : this.tarriff.getFirstClassAmount();

                        ticket_amount -= amount_;

                        seatsSelected.remove(seat.getSeat());
                    }
                }
            });
            grid.add(seat);

        }

        return grid;
    }

    public double getTicketAmount() {
        return ticket_amount;
    }

}
