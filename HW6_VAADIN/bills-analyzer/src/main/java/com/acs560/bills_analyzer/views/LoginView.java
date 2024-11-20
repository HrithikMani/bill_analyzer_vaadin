package com.acs560.bills_analyzer.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * The login form
 */
@Route("login") 
@PageTitle("Login | Bills Analyzer")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 574154935938093394L;
	private final LoginForm login = new LoginForm(); 

	/**
	 * Constructor
	 */
	public LoginView(){
		addClassName("login-view");
		setSizeFull(); 
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");

		add(new H1("Bills Analyzer"));
		
		//Displaying these as helper...don't do this in prod
		add(new Span("Username: user, Password: user"));
		add(new Span("Username: admin, Password: admin"));
		add(login);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}
