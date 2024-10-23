package com.acs560.bills_analyzer.views.bills;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.models.Company;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

/**
 * The form to manage a bill.
 */
public class BillForm extends FormLayout {

	private static final long serialVersionUID = 476310807171214015L;

	private final ComboBox<Company> company = new ComboBox<>("Name");
	private final ComboBox<Integer> month = new ComboBox<>("Month");
	private final ComboBox<Integer> year = new ComboBox<>("Year");
	private final TextField amount = new TextField("Amount");

	private final Button save = new Button("Save");
	private final Button delete = new Button("Delete");
	private final Button cancel = new Button("Cancel");

	private final Binder<Bill> binder = new BeanValidationBinder<>(Bill.class);
	private Bill bill;
	private boolean isAdd;

	/**
	 * Constructor
	 */
	public BillForm(List<Company> companies) {
		addClassName("bills-form");
		
		month.setItems(getRange(1, 12, true));
		
		int currentYear = Year.now().getValue();
		year.setItems(getRange(currentYear - 10, currentYear, false));

		binder.bindInstanceFields(this);

		add(company, month, year, amount, createButtonsLayout());
		this.setWidth("25em");
	}

	/**
	 * Create the buttons component
	 * 
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
			binder.writeBean(bill);

			if (isAdd) {
				fireEvent(new AddEvent(this, bill));
			} else {
				fireEvent(new UpdateEvent(this, bill));
			}
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Update the form with the bill and whether it is to add or update a bill
	 * @param bill - the bill 
	 * @param isAdd - true indicates add, otherwise update
	 */
	public void update(Bill bill, List<Company> companies, boolean isAdd) {
		
		this.isAdd = isAdd;
		
		//Set whether the delete button is visible
		delete.setVisible(!isAdd);

		company.setItems(companies);
		
		if (bill != null) {		
			this.bill = bill;
		} else {
			company.setValue(null);
			month.setValue(null);
			year.setValue(null);
			amount.setValue("");
			this.bill = new Bill();
		}

		// disable any fields here

		binder.setBean(bill);
	}

	/**
	 * The abstract BillsFormEvent
	 */
	public static abstract class BillFormEvent extends ComponentEvent<BillForm> {

		private static final long serialVersionUID = 6883567812607355511L;

		@Getter
		private Bill bill;

		/**
		 * Constructor
		 * @param source - the BillForm
		 * @param bill - the bill
		 */
		protected BillFormEvent(BillForm source, Bill bill) {
			super(source, false);
			this.bill = bill;
		}
	}

	/**
	 * The AddEvent for this form
	 */
	public static class AddEvent extends BillFormEvent {
		private static final long serialVersionUID = -5119175844759316300L;

		/**
		 * Constructor
		 * @param source - the BillForm
		 * @param bill - the bill
		 */
		protected AddEvent(BillForm form, Bill bill) {
			super(form, bill);
		}
	}

	/**
	 * The CancelEvent for this form
	 */
	public static class CancelEvent extends BillFormEvent {
		private static final long serialVersionUID = 2702460047094448680L;

		/**
		 * Constructor
		 * @param source - the BillForm
		 */
		protected CancelEvent(BillForm form) {
			super(form, null);
		}
	}

	/**
	 * The DeleteEvent for this form
	 */
	public static class DeleteEvent extends BillFormEvent {
		private static final long serialVersionUID = 6200490178187296757L;

		/**
		 * Constructor
		 * @param source - the BillForm
		 * @param bill - the deleted bill
		 */
		protected DeleteEvent(BillForm form, Bill bill) {
			super(form, bill);
		}
	}
	
	/**
	 * The UpdateEvent for this form
	 */
	public static class UpdateEvent extends BillFormEvent {
		private static final long serialVersionUID = 6200490178187296757L;

		/**
		 * Constructor
		 * @param source - the BillForm
		 * @param bill - the updated bill
		 */
		protected UpdateEvent(BillForm form, Bill bill) {
			super(form, bill);
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
	
	private List<Integer> getRange(int min, int max, boolean ascending){
		List<Integer> range = IntStream.rangeClosed(min, max).boxed().collect(Collectors.toList());
		
		if (!ascending) {
			Collections.reverse(range);
		}
		
		return range;
	}

}
