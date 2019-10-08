/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr;

import com.tenacle.sgr.components.TicketComponentList;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Arrays;
import org.vaadin.addons.md_stepper.HorizontalStepper;
import org.vaadin.addons.md_stepper.Step;
import tm.kod.widgets.numberfield.NumberField;

/**
 *
 * @author samuel
 */
public class PurchaseTicket extends Panel implements View {    

    public PurchaseTicket() {

        Step enterNumber = new Step(true, "Enter Number", "Description", getEnterNumber());
        Step verifyNumber = new Step(true, "Verify Number", "Description", verifyNumber());
        Step selectSeat = new Step(true, "Buy Ticket", "Description", new TicketComponentList());

        HorizontalStepper stepper = new HorizontalStepper(Arrays.asList(enterNumber, verifyNumber, selectSeat), false);
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
                + "in the format <b>0720125123</b>", ContentMode.HTML);

        demoUsageTitle.addStyleName(ValoTheme.LABEL_H3);

        content.addComponent(basicInformationTitle);
        content.addComponent(basicInformationLabel);
        content.addComponent(demoUsageTitle);
        content.addComponent(field);
        content.iterator().forEachRemaining(c -> c.setWidth(100, Unit.PERCENTAGE));

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
        
        return content;
    }

}
