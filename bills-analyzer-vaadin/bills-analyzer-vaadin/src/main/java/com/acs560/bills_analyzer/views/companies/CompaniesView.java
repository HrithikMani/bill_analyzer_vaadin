package com.acs560.bills_analyzer.views.companies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;
import com.acs560.bills_analyzer.services.CompaniesService;
import com.acs560.bills_analyzer.views.MainLayout;
import com.acs560.bills_analyzer.views.companies.CompanyForm.AddEvent;
import com.acs560.bills_analyzer.views.companies.CompanyForm.CancelEvent;
import com.acs560.bills_analyzer.views.companies.CompanyForm.DeleteEvent;
import com.acs560.bills_analyzer.views.companies.CompanyForm.UpdateEvent;
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
 * The view to display and manage companies.
 */
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "companies", layout = MainLayout.class)
@PageTitle("Companies | Bills Analyzer")
public class CompaniesView extends VerticalLayout  {

	private static final long serialVersionUID = 6436483924131073477L;
	
	@Autowired
	private CompaniesService companiesService;
	
	private final Grid<Company> grid;
	private final TextField filterText;
	private final CompanyForm companyForm;

	private boolean filtering = false;
	
	/**
	 * Constructor.
	 * Builds this component.
	 * @param companiesService - the autowired CompaniesService
	 */
	public CompaniesView(CompaniesService companiesService) {
		this.companiesService = companiesService;
		
        addClassName("list-view");
        setSizeFull();
        
        grid = createGrid();
    	companyForm = createForm();
    	filterText = createFilter();

        add(createToolbar(filterText), getContent());
        updateGrid();
        closeForm();
	}
	
	/**
	 * Create the Company Form
	 * @return - the company form
	 */
	private CompanyForm createForm() {
		CompanyForm companyForm = new CompanyForm();
		
    	companyForm.addListener(AddEvent.class, this::addCompany);
    	companyForm.addListener(DeleteEvent.class, this::deleteCompany);
    	companyForm.addListener(CancelEvent.class, e -> closeForm());
    	companyForm.addListener(UpdateEvent.class, this::updateCompany);
    	
    	return companyForm;
	}

	/**
	 * Create the company grid
	 * @return - the company grid
	 */
    private Grid<Company> createGrid() {
    	Grid<Company>  grid = new Grid<>(Company.class);
    	
        grid.addClassNames("company-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                handleSelected(event.getValue()));
        
        return grid;
    }
    
    /**
     * Create the filter text field
     * @return - the filter text field
     */
    private TextField createFilter() {
    	TextField filterText = new TextField();
    	filterText.setValueChangeTimeout(1000);
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
    private Component createToolbar(TextField filterText) {
        Button addCompanyButton = new Button("Add company");
        addCompanyButton.addClickListener(click -> handleAdd());

        var toolbar = new HorizontalLayout(filterText, addCompanyButton);
        toolbar.addClassName("toolbar");
        
        return toolbar;
    }
    
    /**
     * Get the main content for the view
     * @return - the main content for the view
     */
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, companyForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, companyForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    
    /**
     * Handler for the filter text change listener
     */
    private void handleFilter() {
    	
    	if (!filtering) {
    		return;
    	}
    	
    	String filter = filterText.getValue();
    	List<Company> companies;
    	
    	if (filter.length() > 2) {
    		companies = companiesService.getCompanies(filter);
    	} else {
    		companies = companiesService.getCompanies();
    	}
    	
    	grid.setItems(companies);
    }
    
    /**
     * Update the view's grid of data
     */
    private void updateGrid() {
    	List<Company> companies = companiesService.getCompanies();
    	grid.setItems(companies);
    	filtering = false;
    	filterText.clear();
    	filtering = true;
    }
    
    /**
     * Handler for selected company from the grid
     * @param company - the selected company
     */
    private void handleSelected(Company company) {
    	
    	//Set the selected company on the form
    	companyForm.update(company, false);
    	
		companyForm.setVisible(true);
		addClassName("editing");
		
		//TODO MCP disable grid selection...and reenable when cancel is clicked
    }
        
    /**
     * Handler for add company button action
     */
    private void handleAdd() {
    	grid.asSingleSelect().clear();
    	companyForm.update(null, true);
		companyForm.setVisible(true);
		addClassName("editing");
    }
    
    /**
     * Handler for company form close event
     */
    private void closeForm() {
        companyForm.setVisible(false);
        removeClassName("editing");
    }
    
    /**
     * Handler for company form add event
     * @param event - the AddEvent
     */
    private void addCompany(AddEvent event) {
    	CompanyRequest cr = new CompanyRequest(event.getCompany().getName());
    	companiesService.addCompany(cr);
    	updateGrid();
    	closeForm();
    }
    
    /**
     * Handler for company form update event
     * @param event - the UpdateEvent
     */
    private void updateCompany(UpdateEvent event) {
    	final int id = event.getCompany().getId();
    	final CompanyRequest cr = new CompanyRequest(event.getCompany().getName());
    	companiesService.updateCompany(id, cr);
    	updateGrid();
    	closeForm();
    }
    
    /**
     * Handler for company form delete event
     * @param event- the DeleteEvent
     */
    private void deleteCompany(DeleteEvent event) {
    	companiesService.deleteCompany(event.getCompany().getId());
    	updateGrid();
    	closeForm();
    }

}
