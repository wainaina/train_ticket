/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.AbstractRepository;
import com.tenacle.sgr.entities.Customer;
import com.tenacle.sgr.entities.Location;
import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.Tarriff;
import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.entities.Train;
import com.tenacle.sgr.entities.TrainClass;
import com.tenacle.sgr.entities.Trip;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.persistence.tools.DatabaseController;
import com.tenacle.sgr.utils.DateUtils;
import com.tenacle.sgr.utils.TenacleTimeController;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import tm.kod.widgets.numberfield.NumberField;

/**
 *
 * @author samuel
 */
public class TicketComponent extends MVerticalLayout {

    AbstractRepository<Train> trainDS = new AbstractRepository(Train.class);

    AbstractRepository<Customer> customerDS = new AbstractRepository(Customer.class);

    AbstractRepository<Trip> tripDS = new AbstractRepository(Trip.class);

    List<Location> locations = new AbstractRepository(Location.class).findAll();

    HashMap<Seat, Ticket> seatsSelected = new HashMap();

    ComboBox<Location> from = new ComboBox<>("From", locations);

    ComboBox<Location> to = new ComboBox<>("To", locations);

    DateField selectDate = new DateField("Select Date of Travel");
    DateField returnDate = new DateField("Select Date of Return");

    HorizontalSplitPanel split = new HorizontalSplitPanel();

    ListSelect<Ticket> seatSelector = new ListSelect("Selected Seats", seatsSelected.values());

    Label amount = new Label();

    CheckBox selectReturnTicket = new CheckBox("Is Return Ticket?", false);

    CheckBox below18 = new CheckBox("Is a Child?", false);

    List<Trip> trips = null;

    List<TrainClass> classes = new AbstractRepository(TrainClass.class).findAll();

    ComboBox<Trip> tripSelector = new ComboBox("Select Trip");

    ComboBox<Trip> returnTripSelector = new ComboBox("Select Return Trip");

    double ticket_amount = 0.0;

    Ticket ticket;

    TextField firstname = new TextField("First Name");
    TextField lastname = new TextField("Last Name");
    NumberField phoneNumber = new NumberField("Phone eg. 254715574335");
    TextField email = new TextField("Email");

    Button selectSeat = new Button("Select Travel Seat");
    Button selectReturnSeat = new Button("Select Return Seat");

    // used to render the ticket in html before printing.
    Button saveTicket = new Button("Save Ticket");

    Button ticketEditing = new Button("Edit Ticket", VaadinIcons.EDIT);

    MGridLayout grid = null;

    MVerticalLayout layout;

    DatabaseController<Tarriff> tarriffDS = DB.getInstance().get(Tarriff.class);

    Tarriff tarriff = null;

    Tarriff returnTarriff = null;

    Trip selectedTrip = null;

    Trip returnTrip = null;

    //save the customer right before creating the ticket.
    Customer cust = new Customer();

    MHorizontalLayout customerInformation = new MHorizontalLayout(firstname, lastname).withCaption("Customer Information");
    MHorizontalLayout contactInformation = new MHorizontalLayout(phoneNumber, email).withCaption("Contact Information");
    MHorizontalLayout travelInformation = new MHorizontalLayout(from, to).withCaption("Travel Information");
    MHorizontalLayout travelDates = new MHorizontalLayout(selectDate, returnDate).withCaption("Select Travel Dates");
    MHorizontalLayout tripSelection = new MHorizontalLayout(tripSelector, returnTripSelector).withCaption("Trip Selection");
    MHorizontalLayout seatSelection = new MHorizontalLayout(selectSeat, selectReturnSeat).withCaption("Seat Selection");

    //PAYMENT INFORMATION COMPONENTS
    Label ticketInfo = new Label("", ContentMode.HTML);
    Label amountToPay = new Label("Pay KES <b>1,400.00</b> with", ContentMode.HTML);
    Label or = new Label(" - or - ");

    Button payWithMpesa = new Button("Pay with Mpesa Mobile Money");
    Button payWithAirtel = new Button("Pay with Airtel Mobile Money");

    MHorizontalLayout paymentButtons = new MHorizontalLayout(payWithMpesa, or, payWithAirtel);

    Button edit = new Button("Edit Ticket Details", VaadinIcons.EDIT);

    boolean isEditMode = true;

    MVerticalLayout paymentComponent = new MVerticalLayout()
            .add(ticketInfo, Alignment.MIDDLE_CENTER)
            .add(amountToPay, Alignment.MIDDLE_CENTER)
            .add(paymentButtons, Alignment.MIDDLE_CENTER)
            .add(edit, Alignment.MIDDLE_CENTER)
            .add();

    MVerticalLayout editor = new MVerticalLayout().add(new MFormLayout(
            customerInformation,
            below18,
            contactInformation,
            travelInformation,
            travelDates,
            tripSelection,
            selectReturnTicket,
            seatSelection,
            saveTicket
    ), Alignment.MIDDLE_CENTER)
            .withMargin(true);

    public TicketComponent(Ticket ticket) {

        this.ticket = ticket;

        if (this.ticket == null) {
            this.ticket = new Ticket();
            this.ticket.setCustomer(cust);
        }

        add(editor, Alignment.MIDDLE_CENTER)
                .add(paymentComponent);

        this.setSizeFull();

        init();
    }

    private void init() {

        paymentComponent.setVisible(!isEditMode);

        firstname.setRequiredIndicatorVisible(true);
        firstname.setDescription("Enter your First Name");
        firstname.addValueChangeListener(f -> {
            cust.setFirstName(firstname.getValue());
        });

        lastname.setRequiredIndicatorVisible(true);
        firstname.setRequiredIndicatorVisible(true);
        lastname.addValueChangeListener(f -> {
            cust.setSecondName(lastname.getValue());
        });

        email.addValueChangeListener(f -> {
            cust.setEmail(email.getValue());
        });

        phoneNumber.addValueChangeListener(f -> {
            cust.setPhoneNumber(phoneNumber.getValue());
        });

        below18.addValueChangeListener(l -> {
            contactInformation.setVisible(!below18.getValue());
        });

        phoneNumber.setSigned(false);
        phoneNumber.setUseGrouping(true);
        phoneNumber.setGroupingSeparator(' ');
        phoneNumber.setDecimalLength(0);

        to.addValueChangeListener(t -> {
            getTrips();
            getTariff();
            if (to != null) {
                this.ticket.setToLocation(to.getValue());
            }
        });

        from.addValueChangeListener(f -> {
            getTrips();
            getTariff();

            if (from != null) {
                this.ticket.setFromLocation(from.getValue());
            }
        });

        selectDate.addValueChangeListener(date -> {
            getTrips();
            getTariff();
        });

        returnDate.addValueChangeListener(date -> {
            getReturnTrips();
            getReturnTariff();
        });

        returnDate.setVisible(false);
        returnTripSelector.setVisible(false);
        selectReturnSeat.setVisible(false);

        selectReturnTicket.addValueChangeListener(l -> {
            returnDate.setVisible(selectReturnTicket.getValue());
            returnTripSelector.setVisible(selectReturnTicket.getValue());
            selectReturnSeat.setVisible(selectReturnTicket.getValue());
        });

        this.selectSeat.setWidth(returnDate.getWidth(), Unit.PIXELS);
        this.selectReturnSeat.setWidth(returnDate.getWidth(), Unit.PIXELS);

        this.selectSeat.addClickListener(l -> {
            selectSeat(this.ticket, false);
        });

        this.saveTicket.addClickListener(l -> {
            isEditMode = !isEditMode;
            paymentComponent.setVisible(isEditMode);
            editor.setVisible(!isEditMode);
            displayHTML();
        });

        this.edit.addClickListener(l -> {
            isEditMode = !isEditMode;
            paymentComponent.setVisible(isEditMode);
            editor.setVisible(!isEditMode);
        });

        this.selectReturnSeat.addClickListener(l -> {
            selectSeat(this.ticket, true);
        });

        tripSelector.addValueChangeListener(trip -> {
            selectedTrip = trip.getValue();
            this.ticket.setTrip(selectedTrip);
            System.out.println(selectedTrip.getTripStart().getName());
        });

        returnTripSelector.addValueChangeListener(trip -> {
            returnTrip = returnTripSelector.getValue();
            this.ticket.setReturnTrip(returnTrip);
        });

        //PAYMENT COMPONENT INFORMATION
        amountToPay.addStyleName(ValoTheme.LABEL_HUGE);
        amountToPay.addStyleName(ValoTheme.LABEL_BOLD);
        amountToPay.addStyleName(ValoTheme.LABEL_COLORED);

        payWithMpesa.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        payWithAirtel.addStyleName(ValoTheme.BUTTON_DANGER);

        or.addStyleName(ValoTheme.LABEL_HUGE);
        or.addStyleName(ValoTheme.LABEL_BOLD);
        or.addStyleName(ValoTheme.LABEL_COLORED);

        edit.addStyleName(ValoTheme.BUTTON_PRIMARY);

    }

    List<Trip> getTrips() {

        if (trips == null) {
            trips = new ArrayList();
        }
        if (selectDate.getValue() != null) {

            trips = tripDS.getEntityManager().createNamedQuery("Trip.findByFROMandTOandDate")
                    .setParameter("fromLocation", (from.getValue() != null) ? from.getValue().getId() : 0)
                    .setParameter("toLocation", (to.getValue() != null) ? to.getValue().getId() : 0)
                    .setParameter("dateStart", TenacleTimeController._24HourDateStart((selectDate.getValue() != null) ? DateUtils.asDate(selectDate.getValue()) : new Date()))
                    .setParameter("dateStop", TenacleTimeController._24HourDateEnd((selectDate.getValue() != null) ? DateUtils.asDate(selectDate.getValue()) : new Date()))
                    .getResultList();
        }

        //t.date = :date between :dateStart and :dateStop")
        System.out.println("Trips found: " + trips.size());

        tripSelector.setItems(trips);

        return trips;
    }

    List<Trip> getReturnTrips() {

        if (trips == null) {
            trips = new ArrayList();
        }

        if (selectDate.getValue() != null) {
            trips = tripDS.getEntityManager().createNamedQuery("Trip.findByFROMandTOandDate")
                    .setParameter("fromLocation", (to.getValue() != null) ? to.getValue().getId() : 0)
                    .setParameter("toLocation", (from.getValue() != null) ? from.getValue().getId() : 0)
                    .setParameter("dateStart", TenacleTimeController._24HourDateStart((selectDate.getValue() != null) ? DateUtils.asDate(selectDate.getValue()) : new Date()))
                    .setParameter("dateStop", TenacleTimeController._24HourDateEnd((selectDate.getValue() != null) ? DateUtils.asDate(selectDate.getValue()) : new Date()))
                    .getResultList();
        }

        returnTripSelector.setItems(trips);

        //t.date = :date between :dateStart and :dateStop")
        System.out.println("Trips found: " + trips.size());

        return trips;
    }

    private Tarriff getTariff() {
        tarriff = null;
        try {
            tarriff = (Tarriff) tarriffDS.getEntityManager().createNamedQuery("Tarriff.findByLocations")
                    .setParameter("from", (from.getValue() != null) ? from.getValue().getId() : 0)
                    .setParameter("to", (to.getValue() != null) ? to.getValue().getId() : 0)
                    .getSingleResult();
        } catch (Exception e) {

        }
        return tarriff;
    }

    private Tarriff getReturnTariff() {
        returnTarriff = null;
        try {
            returnTarriff = tarriffDS.getEntityManager().createNamedQuery("Tarriff.findByLocations", Tarriff.class)
                    .setParameter("to", (from.getValue() != null) ? from.getValue().getId() : 0)
                    .setParameter("from", (to.getValue() != null) ? to.getValue().getId() : 0)
                    .getSingleResult();
        } catch (Exception e) {

        }
        return tarriff;
    }

    private void selectSeat(Ticket ticket, boolean isReturnSelection) {
        MessageBox
                .create()
                .withMessage(new SeatSelectionComponent(ticket, tarriff, isReturnSelection, true))
                .withOkButton(ButtonOption.caption("Select Seat"), ButtonOption.icon(VaadinIcons.CHECK))
                .withCancelButton(ButtonOption.caption("Cancel"), ButtonOption.icon(VaadinIcons.CLOSE), ButtonOption.closeOnClick(true))
                .open();

    }

    private void displayHTML() {
        ticketInfo.setCaption(this.ticket.toHtml());
        ticketInfo.setCaptionAsHtml(true);
    }
}
