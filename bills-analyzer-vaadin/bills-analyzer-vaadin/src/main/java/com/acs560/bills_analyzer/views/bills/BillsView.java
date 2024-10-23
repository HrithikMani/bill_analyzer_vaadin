package com.acs560.bills_analyzer.views.bills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.services.BillsService;
import com.acs560.bills_analyzer.services.CompaniesService;
import com.acs560.bills_analyzer.views.MainLayout;
import com.acs560.bills_analyzer.views.bills.BillForm.AddEvent;
import com.acs560.bills_analyzer.views.bills.BillForm.CancelEvent;
import com.acs560.bills_analyzer.views.bills.BillForm.DeleteEvent;
import com.acs560.bills_analyzer.views.bills.BillForm.UpdateEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

import jakarta.annotation.security.PermitAll;

/**
 * The view to display and manage bills
 */
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Bills | Bills Analyzer")
public class BillsView extends VerticalLayout {

	private static final long serialVersionUID = 3031343634720386314L;

	@Autowired
	private BillsService billsService;
	@Autowired
	private CompaniesService companiesService;

	private final Grid<Bill> grid;
	private final TextField filterText;
	private final BillForm billsForm;

	/**
	 * Constructor Builds this component
	 * 
	 * @param billsService - the autowired bills service
	 */
	public BillsView(BillsService billsService,
			CompaniesService companiesService) {
		this.billsService = billsService;
		this.companiesService = companiesService;

		addClassName("list-view");
		setSizeFull();

		grid = createGrid();
		billsForm = createForm();
		filterText = createFilter();

		add(getToolbar(filterText), getContent());
		updateGrid();
		closeForm();
	}

	/**
	 * Create the bills grid
	 * @return - the bills grid
	 */
	private Grid<Bill> createGrid() {
		Grid<Bill> grid = new Grid<>(Bill.class);
		grid.addClassNames("company-grid");
		grid.setSizeFull();

		grid.setColumns();
		grid.addColumn(bill -> bill.getMonth()).setHeader("Month");
		grid.addColumn(bill -> bill.getYear()).setHeader("Year");
		grid.addColumn(bill -> bill.getCompany().getName()).setHeader("Company");
		grid.addColumn(bill -> bill.getAmount()).setHeader("Amount");

		grid.getColumns().forEach(col -> col.setAutoWidth(true));

		grid.asSingleSelect().addValueChangeListener(event -> handleSelected(event.getValue()));

		return grid;
	}

	/**
	 * Create the bills form
	 * 
	 * @return - the bills form
	 */
	private BillForm createForm() {
		BillForm billsForm = new BillForm(companiesService.getCompanies());
		billsForm.addListener(AddEvent.class, this::addBill);
		billsForm.addListener(DeleteEvent.class, this::deleteBill);
		billsForm.addListener(CancelEvent.class, e -> closeForm());
		billsForm.addListener(UpdateEvent.class, this::updateBill);
		
		return billsForm;
	}
	
    /**
     * Create the filter text field
     * @return - the filter text field
     */
    private TextField createFilter() {
    	TextField filterText = new TextField();
    	filterText.setValueChangeTimeout(2000);
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> handleFilter());
        
        return filterText;
    }

    /**
     * Create the toolbar
     * @param filterText - the filter text field
     * @return - the toolbar
     */
	private Component getToolbar(TextField filterText) {
		Button addContactButton = new Button("Add bill");
		addContactButton.addClickListener(click -> handleAdd());

		var toolbar = new HorizontalLayout(filterText, addContactButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

    /**
     * Get the main content for the view
     * @return - the main content for the view
     */
	private HorizontalLayout getContent() {
		HorizontalLayout content = new HorizontalLayout(grid, billsForm);
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, billsForm);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

    /**
     * Update the view's grid of data
     */
	private void updateGrid() {
		grid.setItems(billsService.getBills());
	}

    /**
     * Handler for selected bill from the grid
     * @param bill - the selected bill
     */
	private void handleSelected(Bill bill) {
		this.billsForm.update(bill, companiesService.getCompanies(), false);
		this.billsForm.setVisible(true);
		addClassName("editing");
	}

	/**
	 * Handler for add bill button action
	 */
	private void handleAdd() {
		grid.asSingleSelect().clear();
		billsForm.update(null, companiesService.getCompanies(), true);
		billsForm.setVisible(true);
		addClassName("editing");
	}

	/**
	 * Handler for bills form close event
	 */
	private void closeForm() {
		billsForm.setVisible(false);
		removeClassName("editing");
	}

	/**
	 * Handler for bill form add event
	 * @param event - the AddEvent
	 */
	private void addBill(AddEvent event) {
		billsService.add(event.getBill());
		updateGrid();
		closeForm();
	}
	
	/**
	 * Handler for bill form update event
	 * @param event - the UpdateEvent
	 */
	private void updateBill(UpdateEvent event) {
		billsService.update(event.getBill());
		updateGrid();
		closeForm();
	}

	/**
	 * Handler for bill form delete event
	 * @param event
	 */
	private void deleteBill(DeleteEvent event) {
		billsService.delete(event.getBill());

		updateGrid();
		closeForm();
	}

	/**
	 * Handler for the filter text change listener
	 */
	private void handleFilter() {
		// TODO MCP
	}

}
