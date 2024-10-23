package com.acs560.bills_analyzer.views.companies;

import com.acs560.bills_analyzer.models.Company;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

/**
 * The form to manage a company.
 */
public class CompanyForm extends FormLayout {

	private static final long serialVersionUID = 476310807171214015L;

	//This needs to match a field in my object!
	//The field in the object needs a setter!
	private final TextField name = new TextField("Name");
	
	private final Button save = new Button("Save");
	private final Button delete = new Button("Delete");
	private final Button cancel = new Button("Cancel");

	private final Binder<Company> binder = new BeanValidationBinder<>(Company.class);
	private Company company;
	private boolean isAdd;

	/**
	 * Constructor
	 */
	public CompanyForm() {
		addClassName("company-form");

		binder.bindInstanceFields(this);

		add(name, createButtonsLayout());
		setWidth("25em");
	}

	/**
	 * Create the buttons component
	 * @return - the buttons component
	 */
	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER);
		cancel.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> handleSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
		cancel.addClickListener(event -> fireEvent(new CancelEvent(this)));

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		return new HorizontalLayout(save, delete, cancel);
	}

	/**
	 * Handler for save action
	 */
	private void handleSave() {
		try {
			binder.writeBean(company);
			
			if (isAdd) {
				fireEvent(new AddEvent(this, company));
			} else {
				fireEvent(new UpdateEvent(this, company));
			}
			
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Update the form with the company and whether it is to add or update a company
	 * @param company - the company 
	 * @param isAdd - true indicates add, otherwise update
	 */
	public void update(Company company, boolean isAdd) {
		this.isAdd = isAdd;
		
		//Set whether the delete button is visible
		delete.setVisible(!isAdd);

		if (company != null) {
			this.company = company;
		} else {
			//reset fields to defaults
			name.setValue("");
			this.company = new Company();
		}

		//Disable fields as needed (e.g. id numbers)
//		name.setEnabled(isAdd);

		binder.setBean(company);
	}

	/**
	 * The abstract CompanyFormEvent
	 */
	public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {

		private static final long serialVersionUID = 8892029064323709532L;

		@Getter
		private Company company;

		/**
		 * Constructor
		 * @param source - the CompanyForm
		 * @param company - the company
		 */
		protected CompanyFormEvent(CompanyForm source, Company company) {
			super(source, false);
			this.company = company;
		}
	}

	/**
	 * The AddEvent for this form
	 */
	public static class AddEvent extends CompanyFormEvent {

		private static final long serialVersionUID = -8168200990060394704L;

		/**
		 * Constructor
		 * @param source - the CompanyForm
		 * @param company - the company
		 */
		protected AddEvent(CompanyForm source, Company company) {
			super(source, company);
		}
	}

	/**
	 * The CancelEvent for this form
	 */
	public static class CancelEvent extends CompanyFormEvent {

		private static final long serialVersionUID = -6473184605060760145L;

		/**
		 * Constructor
		 * @param source - the CompanyForm
		 */
		protected CancelEvent(CompanyForm source) {
			super(source, null);
		}
	}

	/**
	 * The DeleteEvent for this form
	 */
	public static class DeleteEvent extends CompanyFormEvent {

		private static final long serialVersionUID = -5085028297106734234L;

		/**
		 * Constructor
		 * @param source - the CompanyForm 
		 * @param company - the deleted company
		 */
		protected DeleteEvent(CompanyForm source, Company company) {
			super(source, company);
		}
	}
	
	/**
	 * The UpdateEvent for this form
	 */
	public static class UpdateEvent extends CompanyFormEvent {

		private static final long serialVersionUID = -5085028297106734234L;

		/**
		 * Constructor
		 * @param source - the CompanyForm 
		 * @param company - the deleted company
		 */
		protected UpdateEvent(CompanyForm source, Company company) {
			super(source, company);
		}
	}

	/**
	 * Add a listener to this form
	 * 
	 * @param eventType - the event type for which to call the listener
	 * @param listener  - the listener
	 * 
	 * @return - an object that can be used to remove the listener
	 */
	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
