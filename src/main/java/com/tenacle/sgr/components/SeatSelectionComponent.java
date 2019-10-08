/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

//import static com.tenacle.sgr.entities.Payment_.amount;
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

    HashMap<Seat, Ticket> seatsSelected = new HashMap();

    boolean seatEditMode;

    boolean ticketBookingMode;

    double ticket_amount = 0.0;

    boolean isSingleSelectionMode;

    boolean customerIsChild;

    TicketComponent ticketComponent;

    boolean adminMode = false;

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
     * @param tarriff
     * @param isReturnSelection
     * @param ticketComponent
     */
    public SeatSelectionComponent(TicketComponent ticketComponent, Tarriff tarriff, boolean isReturnSelection, boolean isSingleSelectionMode, boolean customerIsChild) {
        ticketComponent.getTicket();
        this.ticketComponent = ticketComponent;
        this.tarriff = tarriff;
        this.ticketBookingMode = true;
        this.isReturnSelection = isReturnSelection;
        this.isSingleSelectionMode = isSingleSelectionMode;
        this.customerIsChild = customerIsChild;
        init();

        System.out.println("Number of Coaches: " + ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().size());
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

    Label trainCar = new Label("");

    int current_coach = 0;
    TrainCar car = null;

    private Component getCoachNavigator() {
        previous.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        next.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        car = (!isReturnSelection)
                ? this.ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().get(current_coach)
                : this.ticketComponent.getTicket().getReturnTrip().getTrain().getTrainCarList().get(current_coach);
        carPanel.setContent(getCar(car));
        trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.toString());

        previous.addClickListener(p -> {

            current_coach--;
            if ((current_coach > 0)) {
                System.out.println("Current Coach: " + current_coach);
                car = this.ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().get(current_coach);
                carPanel.setContent(getCar(car));
                trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.getTrainClass().toString());
            } else {
                current_coach = 0;
            }

        });

        next.addClickListener(n -> {

            current_coach++;
            if ((current_coach < this.ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().size())) {
                car = this.ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().get(current_coach);
                carPanel.setContent(getCar(car));
                System.out.println("Current Coach: " + current_coach);
                trainCar.setValue("Coach " + (current_coach + 1) + " - " + car.getTrainClass().toString());
            } else {
                current_coach = this.ticketComponent.getTicket().getTrip().getTrain().getTrainCarList().size() - 1;
            }
        });

        return new MHorizontalLayout(previous, trainCar, next).withMargin(false);
    }

    Tarriff tarriff;

    boolean isReturnSelection;

    boolean isEconomy, isFirstClass;

    public Component getCar(TrainCar car) {

        //get the trip information and overlay the 
        MGridLayout grid = (car.isEconomy()) ? new MGridLayout(10, 6) : new MGridLayout(10, 5);

        for (int i = 0; i < car.getSeatList().size() + 10; i++) {

            //get seat array from train.
            if (car.isEconomy() && i >= 30 && i < 40) {
                isEconomy = true;
                ScaleImage image = new ScaleImage();
                grid.add(image);
                image.setHeight("40px");
                image.setWidth("40px");
                image.setSource(new ThemeResource("img/space.png"));
                continue;

            } else if (i >= 20 && i < 30) {
                isFirstClass = true;
                ScaleImage image = new ScaleImage();
                grid.add(image);
                image.setHeight("40px");
                image.setWidth("40px");
                image.setSource(new ThemeResource("img/space.png"));
                continue;
            }

            int j = i;

            if (isFirstClass && i > 30 || isEconomy && i > 40) {
                i = i - 10;
            }

            SeatComponent seat = new SeatComponent(car.getSeatList().get(i));

            //check whether the seat is marked 
            for (Ticket t : this.ticketComponent.getTicket().getTrip().getTicketList()) {
                if (t.getSeat().getId() == seat.getSeat().getId()) {
                    //mark the seat as not selectable.
                    seat.setOccupied(true);
                    seat.setBookable(false);

                    break;
                }
            }

            i = j;

            seat.addLayoutClickListener(l -> {

                //enable single selection mode.
                if (isSingleSelectionMode && !seatsSelected.isEmpty() && !seatsSelected.containsKey(seat.getSeat())) {
                    return;
                }

                //open pop up to show seat information here in admin mode.
                if (seat.isBookable() && !seat.isOccupied()) {

                    //set the opposite state.
                    seat.setSelected(!seat.isSelected());

                    if (seat.isSelected()) {

                        double amount_ = 0;

                        //get the amount from the tarrif
                        amount_ = (car.isEconomy()) ? this.tarriff.getEconClassAmount() : this.tarriff.getFirstClassAmount();

                        amount_ = (customerIsChild) ? amount_ / 2 : amount_;

                        if (!isReturnSelection) {
                            ticketComponent.getTicket().setSeat(seat.getSeat());
                            ticketComponent.getTicket().setTicketType(new TicketType(1));
                            seatsSelected.put(seat.getSeat(), null);
                            ticketComponent.getTicket().setToAmount(amount_);
                            ticketComponent.saveTicket.setEnabled(true);
                        } else {

                            //select ticket
                            ticketComponent.getTicket().setReturnSeat(seat.getSeat());
                            ticketComponent.getTicket().setTicketType(new TicketType(2));
                            seatsSelected.put(seat.getSeat(), null);
                            ticketComponent.getTicket().setFromAmount(amount_);
                            ticketComponent.saveTicket.setEnabled(true);
                        }

                        ticket_amount += amount_;

                    } else {

                        Ticket ticket = seatsSelected.get(seat.getSeat());

                        double amount_ = 0;

                        amount_ = (this.ticketComponent.getTicket().isEconomy()) ? this.tarriff.getEconClassAmount() : this.tarriff.getFirstClassAmount();

                        ticket_amount -= amount_;

                        seatsSelected.remove(seat.getSeat());

                        ticketComponent.getTicket().setTicketType(null);
                        if (!isReturnSelection) {
                            ticketComponent.getTicket().setSeat(null);
                            ticketComponent.saveTicket.setEnabled(false);
                        } else {
                            //select ticket                            
                            ticketComponent.getTicket().setReturnSeat(null);
                            ticketComponent.saveTicket.setEnabled(false);

                        }
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
