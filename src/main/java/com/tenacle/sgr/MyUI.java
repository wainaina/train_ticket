package com.tenacle.sgr;

import com.github.appreciated.builder.DrawerVariant;
import com.github.appreciated.builder.NavigationDrawerBuilder;
import com.github.appreciated.layout.drawer.AbstractNavigationDrawer;
import com.tenacle.sgr.components.TicketHistory;
import com.tenacle.sgr.components.TrainComponent;
import com.tenacle.sgr.persistence.tools.DB;
import com.tenacle.sgr.trips.TripSchedule;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import kaesdingeling.hybridmenu.HybridMenu;
import kaesdingeling.hybridmenu.builder.HybridMenuBuilder;
import kaesdingeling.hybridmenu.builder.left.LeftMenuButtonBuilder;
import kaesdingeling.hybridmenu.data.enums.EMenuComponents;
import kaesdingeling.hybridmenu.data.enums.EMenuDesign;
import kaesdingeling.hybridmenu.data.leftmenu.MenuButton;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        //init the DB
        DB.getInstance();

        Layout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        // add content to make the scrollbar appear
//        VerticalLayout rightLayout = new VerticalLayout();
//        for (int i = 0; i < 100; i++) {
//            rightLayout.addComponent(new Button("Button " + i));
//        }
//
//        Panel rightPanel = new Panel(rightLayout);
//        rightPanel.setSizeFull(); // <= this is important

//        MenuBar menuBar = new MenuBar();
//        menuBar.addItem("Some item", null);
//        content.addComponent(new HorizontalSplitPanel(new VerticalLayout(menuBar), rightPanel));

        HybridMenu hybridMenu = HybridMenuBuilder.get()
                .setContent(new VerticalLayout())
                .setMenuComponent(EMenuComponents.LEFT_WITH_TOP)
                .build();

        hybridMenu.switchTheme(EMenuDesign.WHITE);

        UI.getCurrent().getNavigator().setErrorView(PurchaseTicket.class);

        MenuButton purchaseTicket = LeftMenuButtonBuilder.get()
                .setCaption("Purchase Ticket")
                .setIcon(VaadinIcons.TICKET)
                .navigateTo(PurchaseTicket.class)
                .build();

        hybridMenu.addLeftMenuButton(purchaseTicket);

        MenuButton schedule = LeftMenuButtonBuilder.get()
                .setCaption("Schedule")
                .setIcon(VaadinIcons.TIMER)
                .navigateTo(Schedule.class)
                .build();

        hybridMenu.addLeftMenuButton(schedule);

        MenuButton adminConsole = LeftMenuButtonBuilder.get()
                .setCaption("Admin Console")
                .setIcon(VaadinIcons.HOME)
                .navigateTo(AdminConsole.class)
                .build();

        hybridMenu.addLeftMenuButton(adminConsole);

        Responsive.makeResponsive(hybridMenu);
        //purchase ticket
        //view schedule
        //admin console

        AbstractNavigationDrawer drawer = NavigationDrawerBuilder.get()
                .withVariant(DrawerVariant.LEFT)
                .withTitle("SGR Online Ticketing")
                .withAppBarElement(getBorderlessButtonWithIcon(VaadinIcons.ELLIPSIS_DOTS_V))
                .withDefaultNavigationView(PurchaseTicket.class)
                .withSection("Customer Options")
                .withNavigationElement("Buy Ticket", VaadinIcons.HOME, PurchaseTicket.class)
                .withNavigationElement("My Ticket History", VaadinIcons.TICKET, TicketHistory.class)
                .withNavigationElement("Train Schedule", VaadinIcons.TRAIN, TripSchedule.class)
                .withSection("Admin Options")
                .withNavigationElement("DashBoard", VaadinIcons.CHART, PurchaseTicket.class)
                .withNavigationElement("Train Management", VaadinIcons.SPLINE_CHART, TrainComponent.class)
                .withNavigationElement("Schedule Management", VaadinIcons.CONNECT, TripSchedule.class)
                .withNavigationElement("Preferences", VaadinIcons.COG, PurchaseTicket.class)
                .build();

        drawer.setHeightUndefined();

        setContent(
                drawer
        );
        //new TicketComponent(null)

//        setContent(new TicketComponentList());
//        setContent(new TicketComponent(null));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private Button getBorderlessButtonWithIcon(VaadinIcons icon) {
        Button button = new Button(icon);
        button.setWidth("64px");
        button.setHeight("64px");
        button.addStyleNames(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ONLY);
        return button;
    }

}
