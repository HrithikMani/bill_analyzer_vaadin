package com.acs560.bills_analyzer.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.acs560.bills_analyzer.security.SecurityService;
import com.acs560.bills_analyzer.views.bills.BillsView;
import com.acs560.bills_analyzer.views.companies.CompaniesView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main layout for the application
 */
public class MainLayout extends AppLayout {

	private static final long serialVersionUID = -5291741451913578403L;
	
	@Autowired
    private final SecurityService securityService;

	/**
	 * Constructor
	 */
	public MainLayout(SecurityService securityService) {
		this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Bills Analyzer");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);
        
        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header); 
    }

    /**
     * Create the drawer
     */
    private void createDrawer() {
    	//Add links to each view as needed
    	RouterLink billsLink = new RouterLink("Bills", BillsView.class);
    	billsLink.setHighlightCondition(HighlightConditions.sameLocation());
    	
        RouterLink companiesLink = new RouterLink("Companies", CompaniesView.class);
        
        addToDrawer(new VerticalLayout(billsLink, companiesLink));
    }
}