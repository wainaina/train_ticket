/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr;

import com.tenacle.sgr.components.SeatComponent;
import com.tenacle.sgr.components.TicketComponent;
import com.tenacle.sgr.entities.AbstractRepository;
import com.tenacle.sgr.entities.Customer;
import com.tenacle.sgr.entities.Location;
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
import com.tenacle.sgr.utils.TenacleTimeController;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.vaadin.addons.md_stepper.HorizontalStepper;
import org.vaadin.addons.md_stepper.Step;
import org.vaadin.addons.md_stepper.StepBuilder;
import org.vaadin.alump.scaleimage.ScaleImage;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MGridLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import tm.kod.widgets.numberfield.NumberField;

/**
 *
 * @author samuel
 */
public class PurchaseTicket extends Panel implements View {

    HorizontalSplitPanel split = new HorizontalSplitPanel();

    HashMap<Seat, Ticket> seatsSelected = new HashMap();

    ListSelect<Ticket> seatSelector = new ListSelect("Selected Seats", seatsSelected.values());

    double ticket_amount = 0.0;
    TicketComponent ticketComponent = new TicketComponent(null);

    public PurchaseTicket() {

        //Create the splitting component here.           
        split.setSecondComponent(new MVerticalLayout().add(seatSelector, 1));
        split.setSplitPosition(70f, Unit.PERCENTAGE);
        split.setSizeFull();

        Step enterNumber = new Step(true, "Enter Number", "Description", getEnterNumber());
        Step verifyNumber = new Step(true, "Verify Number", "Description", verifyNumber());
        Step selectSeat = new Step(true, "Seat Selection", "Description", ticketComponent);
        Step makePayment = new Step(true, "Make Payment", "Description", ticketComponent);
        Step printReceipt = new Step(true, "Print Receipt", "Description", ticketComponent);

        HorizontalStepper stepper = new HorizontalStepper(Arrays.asList(enterNumber, verifyNumber, selectSeat, makePayment, printReceipt), false);
        stepper.setSizeFull();
        stepper.start();

        setContent(stepper);

        Responsive.makeResponsive(stepper);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event);

    }

    private Component getEnterNumber() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth(100, Sizeable.Unit.PERCENTAGE);
        content.setSpacing(true);
        content.setMargin(true);

        Label basicInformationTitle = new Label("Enter your Mobile Number");
        basicInformationTitle.addStyleName(ValoTheme.LABEL_H2);
        Label basicInformationLabel = new Label("Your mobile phone number is required "
                + "in order to do this transaction, since we shall use it to validate "
                + "your transaction in the last step.", ContentMode.HTML);

        NumberField field = new NumberField("Phone Number");
        field.setSigned(false);
        field.setUseGrouping(true);
        field.setGroupingSeparator(' ');
        field.setDecimalLength(0);

        field.addStyleName(ValoTheme.TEXTFIELD_HUGE);

        Label demoUsageTitle = new Label("Enter your mobile phone number "
                + "in the format <b>254720125123</b>", ContentMode.HTML);

        demoUsageTitle.addStyleName(ValoTheme.LABEL_H3);

        content.addComponent(basicInformationTitle);
        content.addComponent(basicInformationLabel);
        content.addComponent(demoUsageTitle);
        content.addComponent(field);
        content.iterator().forEachRemaining(c -> c.setWidth(100, Unit.PERCENTAGE));

//        setCaption("Enter Mobile Number");
//        setDescription("Enter your Phone Number");
        return content;
    }

    private Component verifyNumber() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth(100, Sizeable.Unit.PERCENTAGE);
        content.setSpacing(true);
        content.setMargin(true);

        Label basicInformationTitle = new Label("Verify Mobile Number");
        basicInformationTitle.addStyleName(ValoTheme.LABEL_H2);
        Label basicInformationLabel = new Label("We need to verify that it is indeed you who is using this phone number.", ContentMode.HTML);

        NumberField field = new NumberField("Verification code");
        field.setSigned(false);
        field.setUseGrouping(true);
        field.setGroupingSeparator('-');
        field.setDecimalLength(0);

        field.addStyleName(ValoTheme.TEXTFIELD_HUGE);

        Label demoUsageTitle = new Label("Please enter the Verification code sent to your phone", ContentMode.HTML);

        demoUsageTitle.addStyleName(ValoTheme.LABEL_H3);

        content.addComponent(basicInformationTitle);
        content.addComponent(basicInformationLabel);
        content.addComponent(demoUsageTitle);
        content.addComponent(field);
        content.iterator().forEachRemaining(c -> c.setWidth(100, Unit.PERCENTAGE));

        //create customer information here.
//        setCaption("Verification Code");
//        setDescription("Here, we verify your number");
        return content;
    }

}
