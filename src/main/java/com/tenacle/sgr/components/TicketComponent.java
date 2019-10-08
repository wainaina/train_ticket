/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.components;

import com.tenacle.sgr.entities.AbstractRepository;
import com.tenacle.sgr.entities.Customer;
import com.tenacle.sgr.entities.Location;
import com.tenacle.sgr.entities.Payment;
import com.tenacle.sgr.entities.Seat;
import com.tenacle.sgr.entities.Tarriff;
import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.entities.TicketStatus;
import com.tenacle.sgr.entities.TicketType;
import com.tenacle.sgr.entities.Train;
import com.tenacle.sgr.entities.TrainClass;
import com.tenacle.sgr.entities.Trip;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.persistence.tools.DatabaseController;
import com.tenacle.sgr.utils.DateUtils;
import com.tenacle.sgr.utils.MailUtils;
import com.tenacle.sgr.utils.PDFUtils;
import com.tenacle.sgr.utils.QRUtils;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
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
import pl.pdfviewer.PdfViewer;
import tm.kod.widgets.numberfield.NumberField;

/**
 *
 * @author samuel
 */
public class TicketComponent extends MVerticalLayout {

    AbstractRepository<Train> trainDS = new AbstractRepository(Train.class);

    DatabaseController<Customer> customerDS = DB.getInstance().get(Customer.class);

    DatabaseController<Ticket> ticketDS = DB.getInstance().get(Ticket.class);

    DatabaseController<Payment> paymentDS = DB.getInstance().get(Payment.class);

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

    CheckBox below18 = new CheckBox("Is between 3 and 11 years?", false);

    List<Trip> trips = null;

    List<TrainClass> classes = new AbstractRepository(TrainClass.class).findAll();

    ComboBox<Trip> tripSelector = new ComboBox("Select Trip");

    ComboBox<Trip> returnTripSelector = new ComboBox("Select Return Trip");

    double ticket_amount = 0.0;

    Ticket ticket;

    TextField firstname = new TextField("First Name");
    TextField lastname = new TextField("Last Name");
    NumberField idNumber = new NumberField("Identification Number");

    NumberField phoneNumber = new NumberField("Phone eg. 254715574335");
    TextField email = new TextField("Email");

    Button selectSeat = new Button("Select Travel Seat");
    Button selectReturnSeat = new Button("Select Return Seat");

    // used to render the ticket in html before printing.
    Button saveTicket = new Button("Save Ticket");
    Button removeTicket = new Button("Remove Ticket");

    Button ticketEditing = new Button("Edit Ticket", VaadinIcons.EDIT);

    MGridLayout grid = null;

    MVerticalLayout layout;

    DatabaseController<Tarriff> tarriffDS = DB.getInstance().get(Tarriff.class);

    Tarriff tarriff = null;

    Tarriff returnTarriff = null;

    //save the customer right before creating the ticket.
    Customer cust = new Customer();

    MHorizontalLayout customerInformation = new MHorizontalLayout(firstname, lastname, idNumber).withCaption("Customer Information").withMargin(false);
    MHorizontalLayout contactInformation = new MHorizontalLayout(phoneNumber, email).withCaption("Contact Information").withMargin(false);
    MHorizontalLayout travelInformation = new MHorizontalLayout(from, to).withCaption("Travel Information").withMargin(false);
    MHorizontalLayout travelDates = new MHorizontalLayout(selectDate, returnDate).withCaption("Select Travel Dates").withMargin(false);
    MHorizontalLayout tripSelection = new MHorizontalLayout(tripSelector, returnTripSelector).withCaption("Trip Selection").withMargin(false);
    MHorizontalLayout seatSelection = new MHorizontalLayout(selectSeat, selectReturnSeat).withCaption("Seat Selection").withMargin(false);
    MHorizontalLayout ticketOptions = new MHorizontalLayout(saveTicket, removeTicket).withCaption("Seat Selection").withMargin(false);

    //PAYMENT INFORMATION COMPONENTS
    Label ticketInfo = new Label("", ContentMode.HTML);
    Label amountToPay = new Label("Pay KES <b>1,400.00</b> with", ContentMode.HTML);
    Label or = new Label(" - or - ");

    Button payWithMpesa = new Button("Pay with Mpesa Mobile Money");
    Button payWithAirtel = new Button("Pay with Airtel Mobile Money");

    Button cancelTicket = new Button("Cancel Ticket", VaadinIcons.CLOSE);
    Button emailTicket = new Button("Email Ticket", VaadinIcons.ENVELOPE);
    Button printTicket = new Button("Print Ticket", VaadinIcons.PRINT);

    MHorizontalLayout optionsAfterPurchase = new MHorizontalLayout(cancelTicket, emailTicket, printTicket).withMargin(false);

    MHorizontalLayout paymentButtons = new MHorizontalLayout(payWithMpesa, or, payWithAirtel);

    Button edit = new Button("Edit Ticket Details", VaadinIcons.EDIT);

    boolean isEditMode = true;
    Button makePayment = new Button("Pay For Ticket");
    MVerticalLayout paymentComponent = new MVerticalLayout()
            .add(ticketInfo, Alignment.MIDDLE_CENTER)
            .add(makePayment, Alignment.MIDDLE_CENTER)
            .add(optionsAfterPurchase, Alignment.MIDDLE_CENTER)
            .add(edit, Alignment.MIDDLE_CENTER)
            .add();

    MVerticalLayout editor = new MVerticalLayout().add(
            new MFormLayout()
                    .with(
                            customerInformation,
                            below18,
                            contactInformation,
                            travelInformation,
                            travelDates,
                            tripSelection,
                            selectReturnTicket,
                            seatSelection,
                            ticketOptions
                    )
                    .withComponentAlignment(customerInformation, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(below18, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(contactInformation, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(travelInformation, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(travelDates, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(tripSelection, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(selectReturnTicket, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(seatSelection, Alignment.MIDDLE_CENTER)
                    .withComponentAlignment(ticketOptions, Alignment.MIDDLE_CENTER),
            Alignment.MIDDLE_CENTER)
            .withMargin(true);

    TicketComponentList ticketComponentList = null;

    public TicketComponent(Ticket ticket, TicketComponentList ticketComponentList) {

        this.ticket = ticket;
        this.ticketComponentList = ticketComponentList;

        if (this.ticket == null) {
            this.ticket = new Ticket();
            this.ticket.setCustomer(cust);
            this.setPrintMode(false);
        } else {
            if (this.ticket.getTicketStatus().getId() == 1) {
                isEditMode = !isEditMode;
                paymentComponent.setVisible(true);
                editor.setVisible(false);
                displayHTML();
                this.setPrintMode(true);
            } else {
                this.setPrintMode(false);
            }
        }

        add(editor, Alignment.MIDDLE_CENTER)
                .add(paymentComponent)
                .withMargin(false);

        this.setSizeFull();

        init();

    }

    private void init() {

        paymentComponent.setVisible(!isEditMode);

        if (this.ticket.getCustomer().getFirstName() != null) {
            firstname.setValue(this.ticket.getCustomer().getFirstName());
        }
        firstname.setRequiredIndicatorVisible(true);
        firstname.setDescription("Enter your First Name");
        firstname.addValueChangeListener(f -> {
            cust.setFirstName(firstname.getValue());
        });

        if (this.ticket.getCustomer().getSecondName() != null) {
            lastname.setValue(this.ticket.getCustomer().getSecondName());
        }
        lastname.setRequiredIndicatorVisible(true);
        lastname.setDescription("Enter your Second Name");
        lastname.addValueChangeListener(f -> {
            cust.setSecondName(lastname.getValue());
        });

        if (this.ticket.getCustomer().getIdNumber() != null) {
            idNumber.setValue(this.ticket.getCustomer().getIdNumber());
        }
        idNumber.setRequiredIndicatorVisible(true);
        idNumber.setDescription("Enter your ID Number or Passport Number");
        idNumber.addValueChangeListener(f -> {
            cust.setIdNumber(idNumber.getValue());
        });

        if (this.ticket.getCustomer().getEmail() != null) {
            email.setValue(this.ticket.getCustomer().getEmail());
        }
        email.addValueChangeListener(f -> {
            cust.setEmail(email.getValue());
        });

        if (this.ticket.getCustomer().getPhoneNumber() != null) {
            phoneNumber.setValue(this.ticket.getCustomer().getPhoneNumber());
        }
        phoneNumber.addValueChangeListener(f -> {
            cust.setPhoneNumber(phoneNumber.getValue().replaceAll(" ", ""));
        });

        below18.addValueChangeListener(l -> {
            contactInformation.setVisible(!below18.getValue());
        });

        phoneNumber.setSigned(false);
        phoneNumber.setUseGrouping(true);
        phoneNumber.setGroupingSeparator(' ');
        phoneNumber.setDecimalLength(0);

        if (this.ticket.getToLocation() != null) {
            to.setValue(this.ticket.getToLocation());
            to.setEnabled(true);
        }
        to.setValue(this.ticket.getToLocation());
        to.setRequiredIndicatorVisible(true);
        to.addValueChangeListener(t -> {
            getTrips();
            getTariff();
            if (to != null) {
                this.ticket.setToLocation(to.getValue());
            }

            selectDate.setEnabled(from.getValue() != null && to.getValue() != null);
        });

        if (this.ticket.getFromLocation() != null) {
            from.setValue(this.ticket.getFromLocation());
            from.setEnabled(true);
        }
        from.setRequiredIndicatorVisible(true);
        from.addValueChangeListener(f -> {
            getTrips();
            getTariff();

            if (from.getValue() != null) {
                this.ticket.setFromLocation(from.getValue());
            }

            selectDate.setEnabled(from.getValue() != null && to.getValue() != null);

        });

        tripSelector.setEnabled(selectDate.getValue() != null);
        returnTripSelector.setEnabled(returnDate.getValue() != null);

        if (this.ticket.getTrip() != null) {
            selectDate.setValue(DateUtils.asLocalDate(this.ticket.getTrip().getDate()));
            selectDate.setEnabled(true);
        }
        selectDate.setEnabled(false);
        selectDate.setRequiredIndicatorVisible(true);
        selectDate.addValueChangeListener(date -> {
            getTrips();
            getTariff();

            tripSelector.setEnabled(selectDate.getValue() != null);

        });

        if (this.ticket.getReturnTrip() != null) {
            returnDate.setValue(DateUtils.asLocalDate(this.ticket.getReturnTrip().getDate()));
            returnDate.setEnabled(true);
        }
        returnDate.setRequiredIndicatorVisible(true);
        returnDate.addValueChangeListener(date -> {
            getReturnTrips();
            getReturnTariff();

            returnTripSelector.setEnabled(returnDate.getValue() != null);
        });

        returnDate.setVisible(false);
        returnTripSelector.setVisible(false);

        selectReturnSeat.setWidth(returnDate.getWidth(), Unit.PIXELS);
        selectReturnSeat.setVisible(false);
        selectReturnSeat.setEnabled(returnTripSelector.getValue() != null);

        selectReturnTicket.addValueChangeListener(l -> {
            returnDate.setVisible(selectReturnTicket.getValue());
            returnTripSelector.setVisible(selectReturnTicket.getValue());
            selectReturnSeat.setVisible(selectReturnTicket.getValue());

            if (!selectReturnTicket.getValue()) {
                returnDate.setValue(null);
                returnTripSelector.setValue(null);
            }
        });

        if (this.ticket.getSeat() != null) {
            selectSeat.setEnabled(true);
        }
        this.selectSeat.setEnabled(tripSelector.getValue() != null);
        this.selectSeat.addClickListener(l -> {
            selectSeat(false);

            if (this.ticket.getSeat() != null) {
                this.saveTicket.setEnabled(true);
            }
        });

        this.saveTicket.setEnabled(false);
        this.saveTicket.addStyleName(ValoTheme.BUTTON_PRIMARY);
        this.saveTicket.addClickListener(l -> {

            //save ticket information here.
            if (this.ticket.getId() == null) {

                //create the ticket here
                this.ticket.setCustomer(customerDS.create(cust));

                //set it as a draft
                this.ticket.setTicketStatus(new TicketStatus(4));

                //return or single way ticket
                this.ticket.setTicketType(new TicketType((!this.selectReturnTicket.getValue()) ? 1 : 2));

                this.ticket = ticketDS.create(this.ticket);

                //make the necessary payment
                this.ticketComponentList.pComponent.addTicket(getTicket());

                //create the image here, will be read by the html code and rendered.
                QRUtils.createQR("" + this.ticket.getId());
            } else {
                //edit the ticket information.
                this.ticket = ticketDS.edit(this.ticket, this.ticket);
            }

            isEditMode = !isEditMode;
            paymentComponent.setVisible(isEditMode);
            editor.setVisible(!isEditMode);
            displayHTML();
        });

        this.removeTicket.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeTicket.addClickListener(l -> {

            MessageBox
                    .createQuestion()
                    .withMessage("Are you sure you want to remove this ticket? Operation cannot be reversed.")
                    .withOkButton(() -> {
                        this.ticketComponentList.removeComponent(this);

                        //remove from database
                        if (this.ticket.getId() != null) {
                            ticketDS.remove(this.ticket, this.ticket.getId());
                        }

                    }, ButtonOption.caption("Remove"), ButtonOption.icon(VaadinIcons.CHECK))
                    .withCancelButton(ButtonOption.caption("Cancel"), ButtonOption.icon(VaadinIcons.CLOSE), ButtonOption.closeOnClick(true))
                    .asModal(true)
                    .open();

        });
        this.makePayment.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.makePayment.addStyleName(ValoTheme.BUTTON_HUGE);
        this.makePayment.setWidth("30%");
        this.makePayment.addClickListener(l -> {
            makePayment();
        });

        this.edit.addClickListener(l -> {
            isEditMode = !isEditMode;
            paymentComponent.setVisible(isEditMode);
            editor.setVisible(!isEditMode);
        });

        if (this.ticket.getReturnTrip() != null) {
            returnTripSelector.setValue(this.ticket.getTrip());
            returnTripSelector.setEnabled(true);
        }
        this.selectReturnSeat.addClickListener(l -> {
            selectSeat(true);
        });

        if (this.ticket.getTrip() != null) {
            tripSelector.setValue(this.ticket.getTrip());
            selectSeat.setEnabled(true);
        }
        tripSelector.addValueChangeListener(trip -> {
            this.ticket.setTrip(tripSelector.getValue());
            System.out.println("Trip Selected: " + this.ticket.getTrip());
            selectSeat.setEnabled(tripSelector.getValue() != null);
        });

        if (this.ticket.getReturnTrip() != null) {
            returnTripSelector.setValue(this.ticket.getReturnTrip());
        }
        returnTripSelector.addValueChangeListener(trip -> {
            this.ticket.setReturnTrip(returnTripSelector.getValue());
            System.out.println("Return Trip Selected: " + this.ticket.getReturnTrip());
            selectReturnSeat.setEnabled(returnTripSelector.getValue() != null);
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

        printTicket.addClickListener(l -> {
            printTicket();
        });

        emailTicket.addClickListener(l -> {
            MessageBox
                    .createQuestion()
                    .asModal(true)
                    .withMessage("Send ticket to email address?")
                    .withOkButton(() -> {
                        sendMail();
                    },
                            ButtonOption.closeOnClick(true),
                            ButtonOption.caption("Send Mail"),
                            ButtonOption.icon(VaadinIcons.ENVELOPE)
                    )
                    .withCancelButton(ButtonOption.closeOnClick(true))
                    .open();
        });

    }

    List<Trip> getTrips() {

        if (trips == null) {
            trips = new ArrayList();
        }
        if (selectDate.getValue() != null) {

            int fromID = 0, toID = 0;

            if (from.getValue() != null && to.getValue() != null) {

                if (from.getValue().getId() == to.getValue().getId()) {
                    Notification.show("From Location must be different from To location.", Notification.Type.WARNING_MESSAGE);
                }
                if (from.getValue().getId() < to.getValue().getId()) {
                    fromID = 1;
                    toID = 9;
                } else {
                    fromID = 9;
                    toID = 1;
                }
            }

            System.out.println("Get Trips => From ID: " + fromID + " To ID: " + toID);

            trips = tripDS.getEntityManager().createNamedQuery("Trip.findByFROMandTOandDate")
                    .setParameter("fromLocation", (from.getValue() != null) ? fromID : 0)
                    .setParameter("toLocation", (to.getValue() != null) ? toID : 0)
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

            int fromID = 0, toID = 0;

            if (from.getValue() != null && to.getValue() != null) {

                if (from.getValue().getId() == to.getValue().getId()) {
                    Notification.show("From Location must be different from To location.", Notification.Type.WARNING_MESSAGE);
                }
                if (to.getValue().getId() < from.getValue().getId()) {
                    fromID = 1;
                    toID = 9;
                } else {
                    fromID = 9;
                    toID = 1;
                }

                System.out.println("Get Trips => From ID: " + fromID + " To ID: " + toID);

            }

            trips = tripDS.getEntityManager().createNamedQuery("Trip.findByFROMandTOandDate")
                    .setParameter("fromLocation", (to.getValue() != null) ? fromID : 0)
                    .setParameter("toLocation", (from.getValue() != null) ? toID : 0)
                    .setParameter("dateStart", TenacleTimeController._24HourDateStart((returnDate.getValue() != null) ? DateUtils.asDate(returnDate.getValue()) : new Date()))
                    .setParameter("dateStop", TenacleTimeController._24HourDateEnd((returnDate.getValue() != null) ? DateUtils.asDate(returnDate.getValue()) : new Date()))
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

    SeatSelectionComponent s = null;

    private void selectSeat(boolean isReturnSelection) {

        s = null;
        s = new SeatSelectionComponent(this, tarriff, isReturnSelection, true, below18.getValue());

        MessageBox
                .create()
                .withMessage(s)
                .withOkButton(ButtonOption.caption("Select Seat"), ButtonOption.icon(VaadinIcons.CHECK))
                .withCancelButton(ButtonOption.caption("Cancel"), ButtonOption.icon(VaadinIcons.CLOSE), ButtonOption.closeOnClick(true))
                .open();

    }

    private void makePayment() {

        MessageBox
                .create()
                .withMessage("Pay for this ticket?")
                .withOkButton(() -> {

                    MessageBox
                            .create()
                            .withMessage(this.ticketComponentList.pComponent)
                            .withOkButton(() -> {

                                //initiate mpesa online check out operation that will be used to 
                                Payment p = this.ticketComponentList.pComponent.getPayment();

                                p.setId(0);
                                p.setTime(new Date());
                                p.setPaymentReference("REFERENCE");
                                p = paymentDS.create(p);

                                //create the payment object here.
                                for (Ticket t : this.ticketComponentList.pComponent.getTickets()) {
                                    t.setPayment(p);
                                    t.setTicketStatus(new TicketStatus(1));
                                    t = ticketDS.edit(t, t);
                                }

                                //clear the tickets from the payment component
//                                this.ticketComponentList.pComponent.removeAllTickets();
                                //hide payment and edit option on the ticket component list
                                for (int i = 0; i < this.ticketComponentList.getComponentCount(); i++) {
                                    Component c = ticketComponentList.getComponent(i);

                                    if (c instanceof TicketComponent) {
                                        TicketComponent t = (TicketComponent) c;
                                        if (t.getTicket().getPayment().getId() > 0) {
                                            t.setPrintMode(true);
                                            this.ticketComponentList.pComponent.removeTicket(t.getTicket());
                                        }
                                    }

                                }
                            }, ButtonOption.caption("Pay"), ButtonOption.icon(VaadinIcons.CHECK))
                            .withCancelButton(ButtonOption.caption("Cancel"), ButtonOption.icon(VaadinIcons.CLOSE), ButtonOption.closeOnClick(true))
                            .open();

                }, ButtonOption.caption("Pay for Ticket"), ButtonOption.icon(VaadinIcons.CHECK))
                .withCancelButton(ButtonOption.caption("Cancel"), ButtonOption.icon(VaadinIcons.CLOSE), ButtonOption.closeOnClick(true))
                .open();

    }

    private void displayHTML() {

        System.out.println(String.format("Trip: %s", this.ticket.getTrip()));
        ticketInfo.setCaption(toHtml(true));
        ticketInfo.setCaptionAsHtml(true);
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String toHtml(boolean showHtml) {
        System.out.println("Trip Selected: " + this.ticket.getTrip());
        System.out.println("Return Trip Selected: " + this.ticket.getReturnTrip());

        String html = (showHtml)
                ? "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <title>Ticket</title>\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "    <link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n"
                + "    <header class=\"w3-container w3-light-grey\">   \n"
                + "        <style>\n"
                + "            p {\n"
                + "                font-family: \"Palatino Linotype\",\"Book Antiqua\",Palatino,serif;\n"
                + "                width: 100%;\n"
                + "            }\n"
                + "            table.cinereousTable {\n"
                + "                font-family: \"Palatino Linotype\",\"Book Antiqua\",Palatino,serif;\n"
                + "                width: 100%;\n"
                + "                text-align: center;\n"
                + "                background-color: #FDFFD5;\n"
                + "            }\n"
                + "            table.cinereousTable td, table.cinereousTable th {\n"
                + "                padding: 4px 4px;\n"
                + "            }\n"
                + "            table.cinereousTable tbody td {\n"
                + "                font-size: 13px;\n"
                + "            }\n"
                + "            table.cinereousTable thead {\n"
                + "                background: #948473;\n"
                + "                background: -moz-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: -webkit-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: linear-gradient(to bottom, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "            }\n"
                + "            table.cinereousTable thead th {\n"
                + "                font-size: 19px;\n"
                + "                font-weight: bold;\n"
                + "                color: #F0F0F0;\n"
                + "                text-align: left;\n"
                + "                border-left: 2px solid #948473;\n"
                + "            }\n"
                + "            table.cinereousTable thead th:first-child {\n"
                + "                border-left: none;\n"
                + "            }\n"
                + "\n"
                + "            table.cinereousTable tfoot {\n"
                + "                font-size: 16px;\n"
                + "                font-weight: bold;\n"
                + "                color: #F0F0F0;\n"
                + "                background: #948473;\n"
                + "                background: -moz-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: -webkit-linear-gradient(top, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "                background: linear-gradient(to bottom, #afa396 0%, #9e9081 66%, #948473 100%);\n"
                + "            }\n"
                + "            table.cinereousTable tfoot td {\n"
                + "                font-size: 16px;\n"
                + "            }\n"
                + "            \n"
                + "        </style>\n"
                + "\n"
                + "    </header>\n"
                + "\n"
                + "\n"
                + "    <div class=\"w3-container\">\n"
                + "\n"
                + "        <div class=\"w3-card-4\" style=\"width:100%\"; align=\"center\";>           \n"
                + "\n"
                + "            <h3 style=\"font-family: 'Times New Roman',Times,serif;\n"
                + "                font-size: 25px;\n"
                + "                text-align: center;\n"
                + "                letter-spacing: 3px;\n"
                + "                word-spacing: 3.6px;\n"
                + "                color: #000000;\n"
                + "                font-weight: normal;\n"
                + "                text-decoration: none;\n"
                + "                font-style: normal;\n"
                + "                font-variant: small-caps;\n"
                + "                text-transform: none; \">" + this.ticket.getCustomer().getFirstName() + " " + this.ticket.getCustomer().getSecondName() + "</h3>     \n"
                + "                \n"
                + "                <img \n"
                //                + "                src=\"https://www.appcoda.com/wp-content/uploads/2013/12/qrcode.jpg\" \n"
                + "                src=\"http://localhost/qr/" + this.ticket.getId() + ".png\" \n"
                + "                alt=\"Avatar\" \n"
                + "                class=\"w3-center  \" \n"
                + "                 align=\"center\"\n"
                + "                style=\"\n"
                + "                width:100px; \n"
                + "                height:100px;\">\n"
                + "                \n"
                + "           \n"
                + "\n"
                + "            <div class=\"w3-container\">   	  \n"
                + "                <hr>      \n"
                + "                <p style=\"text-align: center\"> Ticket Details</p>      \n"
                + "\n"
                + "                <table class=\"cinereousTable\">\n"
                + "                    <tbody>\n"
                + "\n"
                + "                        <tr>\n"
                + "                            <td>\n"
                + "                                <table class=\"cinereousTable\">\n"
                + "                                    <tbody>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Journey</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getFromLocation() + " - " + this.ticket.getToLocation() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Date</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getTrip().getDate() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Seat</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getSeat().toString() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Amount</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getToAmount() + "</td>\n"
                + "                                        </tr>\n"
                + "                                    </tbody>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "\n"
                + ((this.ticket.getReturnTrip() != null)
                ? "                            <td>\n"
                + "                               <table class=\"cinereousTable\">\n"
                + "                                    <tbody>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Journey</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getToLocation() + " - " + this.ticket.getFromLocation() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Date</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getReturnTrip().getDate() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Seat</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getReturnSeat().toString() + "</td>\n"
                + "                                        </tr>\n"
                + "                                        <tr>\n"
                + "                                            <td><b>Amount</b></td>\n"
                + "                                            <td style=\"text-align: left;\">" + this.ticket.getFromAmount() + "</td>\n"
                + "                                        </tr>\n"
                + "                                    </tbody>\n"
                + "                                </table>\n"
                + "                            </td>\n" : "")
                + "                        </tr>\n"
                + "                    </tbody>\n"
                + "                </table>\n"
                + "                <hr>\n"
                + "                                <p style=\"text-align: center\">Amount <b>" + (this.ticket.getFromAmount() + this.ticket.getToAmount()) + "</b></p>      \n"
                + "                <hr>\n"
                + "                <img src=\"http://krc.co.ke/wp-content/uploads/2016/01/logo_krc.png\" alt=\"Avatar\" class=\"w3-left w3-circle w3-margin-right\" style=\"width:40px\">    \n"
                + "                <p style=\"text-align: center\">Enjoy your trip</p>  \n"
                + "            </div>        \n"
                + "        </div>\n"
                + "    </div>\n"
                + "\n"
                + "</html>"
                : "<h2>Ticket Information</h2>";
        return html;
    }

    public void setPrintMode(boolean printMode) {
        makePayment.setVisible(!printMode);
        edit.setVisible(!printMode);
        optionsAfterPurchase.setVisible(printMode);
    }

    private void printTicket() {

        PdfViewer pdfViewer = new PdfViewer(PDFUtils.createPDF(this.ticket, toHtml(true)));

        //show the PDF
        MessageBox
                .create()
                .asModal(true)
                .withMessage(pdfViewer)
                .withCloseButton(ButtonOption.closeOnClick(true))
                .open();
    }

    private void sendMail() {
        MailUtils.sendMail(this.ticket, toHtml(true));
    }
}
