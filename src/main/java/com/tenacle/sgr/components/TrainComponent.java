/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.SeatAvailability;
import com.tenacle.sgr.entities.Train;
import com.tenacle.sgr.entities.TrainCar;
import com.tenacle.sgr.entities.TrainClass;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.persistence.tools.DatabaseController;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import tm.kod.widgets.numberfield.NumberField;

/**
 * Manages the creation of trains cars and seats.
 *
 * @author samuel
 */
public class TrainComponent extends MHorizontalLayout implements View {

    DatabaseController<Train> trains = DB.getInstance().get(Train.class);
    DatabaseController<TrainCar> trainCars = DB.getInstance().get(TrainCar.class);
    DatabaseController<Seat> seats = DB.getInstance().get(Seat.class);

    List<Train> allTrains = trains.findAll();

    Train selectedTrain = null;

    Seat selectedSeat = null;

    TrainCar selectedTrainCar = null;

    List<TrainClass> trainClasses = DB.getInstance().get(TrainClass.class).findAll();

    //display a list of all the trains.
    ListSelect<Train> trainList = new ListSelect("Train Fleet", allTrains);

    ListSelect<TrainCar> carList = new ListSelect("Coaches");

    ListSelect<Seat> seatList = new ListSelect("Seats");

    private void createTrain() {
        TextField numberPlate = new TextField("Train Number");
        MessageBox
                .createInfo()
                .withMessage(numberPlate)
                .withCancelButton()
                .withCustomButton(
                        () -> {
                            if (numberPlate.getValue() != null) {
                                Train train = new Train();
                                train.setLabel(numberPlate.getValue());

                                selectedTrain = trains.create(train);

                                //add train to the list
                                trainList.setItems(allTrains = trains.findAll());
                            }
                        },
                        ButtonOption.caption("Add Train"),
                        ButtonOption.focus(),
                        ButtonOption.closeOnClick(true),
                        ButtonOption.icon(VaadinIcons.PLUS)
                )
                .asModal(true)
                .open();
    }

    private void createTrainCar(Train train) {
        NumberField carCount = new NumberField("Train Cars to be Added");
        carCount.setValue(1 + "");

        ComboBox<TrainClass> trainClass = new ComboBox<>("Select Class", trainClasses);

        MessageBox
                .createInfo()
                .withMessage(new MFormLayout(carCount, trainClass))
                .withCancelButton()
                .withCustomButton(
                        () -> {

                            int count = Integer.valueOf(carCount.getValue());

                            if (carCount.getValue() != null && count > 0) {
                                for (int i = 0; i < count; i++) {
                                    TrainCar car = new TrainCar();
                                    car.setTrain(train);
                                    car.setTrainClass(trainClass.getValue());

                                    selectedTrainCar = trainCars.create(car);

                                    train.getTrainCarList().add(car);

                                    //add a car to the carlist
                                    carList.setItems(train.getTrainCarList());

                                    //display the seats in this car
                                    seatList.setItems(selectedTrainCar.getSeatList());

                                }
                            }
                        },
                        ButtonOption.caption("Add Train Cars"),
                        ButtonOption.focus(),
                        ButtonOption.closeOnClick(true),
                        ButtonOption.icon(VaadinIcons.PLUS)
                )
                .asModal(true)
                .open();
    }

    private void createTrainCarSeats(TrainCar car) {
        NumberField seatCount = new NumberField("Enter Seats to be Added");
        seatCount.setValue(((car.getTrainClass().getId() == 1) ? 40 : 60) + "");

        MessageBox
                .createInfo()
                .withMessage(seatCount)
                .withCancelButton()
                .withCustomButton(
                        () -> {

                            int count = Integer.valueOf(seatCount.getValue());

                            if (seatCount.getValue() != null && count > 0) {
                                for (int i = 0; i < count; i++) {
                                    Seat seat = new Seat();
                                    seat.setTrainCar(car);
                                    //trainId/carID/SeatID
                                    seat.setLabel(car.getTrain() + "/" + car.getId() + "/" + i);
                                    seat.setAvailability(new SeatAvailability(1));//Available                                  

                                    seat = seats.create(seat);

                                    car.getSeatList().add(seat);

                                    seatList.setItems(car.getSeatList());
                                }
                            }
                        },
                        ButtonOption.caption("Add Seats to Car"),
                        ButtonOption.focus(),
                        ButtonOption.closeOnClick(true),
                        ButtonOption.icon(VaadinIcons.PLUS)
                )
                .asModal(true)
                .open();
    }

    public TrainComponent() {

        trainList.addValueChangeListener(l -> {
            selectedTrains = l.getValue();

            if (selectedTrains.size() == 1) {
                //display the seats

                List<Train> trains_ = new ArrayList<Train>(selectedTrains);
                carList.setItems((selectedTrain = trains_.get(0)).getTrainCarList());
                trains_ = null;

            }
        });
        trainList.setSizeFull();

        carList.addValueChangeListener(l -> {
            selectedCars = l.getValue();

            if (selectedCars.size() == 1) {
                //display the seats                                
                List<TrainCar> trainCars_ = new ArrayList<TrainCar>(selectedCars);
                seatList.setItems((selectedTrainCar = trainCars_.get(0)).getSeatList());
                trainCars_ = null;
            }
        });
        carList.setSizeFull();

        seatList.addValueChangeListener(l -> {
            selectedSeats = l.getValue();

            if (selectedSeats.size() > 0) {
                List<Seat> seats_ = new ArrayList<Seat>(selectedSeats);
                selectedSeat = seats_.get(0);
                seats_ = null;
            }
        });

        seatList.setSizeFull();

        add(getTrainsComponent(), getTrainCarsComponent(), getSeatsComponent()).withFullSize().withMargin(true);
    }

    Component getTrainsComponent() {
        Button addTrain = new Button("Add Train", VaadinIcons.PLUS);
        addTrain.setSizeFull();
        addTrain.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addTrain.addClickListener(l -> {
            createTrain();
        });

        return new MVerticalLayout()
                .add(trainList, 1)
                .add(addTrain)
                .withMargin(false);
    }

    Component getTrainCarsComponent() {
        Button addTrainCar = new Button("Add Car", VaadinIcons.PLUS);
        addTrainCar.setSizeFull();
        addTrainCar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addTrainCar.addClickListener(l -> {
            createTrainCar(selectedTrain);
        });

        return new MVerticalLayout()
                .add(carList, 1)
                .add(addTrainCar)
                .withMargin(false);
    }

    Component getSeatsComponent() {
        Button addSeats = new Button("Add Seats", VaadinIcons.PLUS);
        addSeats.setSizeFull();
        addSeats.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        addSeats.addClickListener(l -> {
            createTrainCarSeats(selectedTrainCar);
        });

        return new MVerticalLayout()
                .add(seatList, 1)
                .add(addSeats)
                .withMargin(false);
    }

    Set<TrainCar> selectedCars;
    Set<Seat> selectedSeats;
    Set<Train> selectedTrains;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event); //To change body of generated methods, choose Tools | Templates.
    }

}
