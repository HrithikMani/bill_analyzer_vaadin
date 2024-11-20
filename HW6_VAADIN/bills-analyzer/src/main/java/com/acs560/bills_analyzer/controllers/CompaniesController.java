package com.acs560.bills_analyzer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;
import com.acs560.bills_analyzer.services.CompaniesService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/v1/companies")
public class CompaniesController {

	private CompaniesService cs;
	
	/**
     * Constructs a new CompaniesController with the specified CompaniesService.
     * 
     * @param cs the CompaniesService to be used for managing companies
     */
	@Autowired
	public CompaniesController(CompaniesService cs) {
		this.cs = cs;
	}
	
	/**
     * Retrieves a list of all companies.
     * 
     * @return a ResponseEntity containing the list of companies
     */
	@GetMapping("listAllCompanies")
	public ResponseEntity<List<Company>> getCompanies(){
		return ResponseEntity.ok(cs.getCompanies());
	}
	
	 /**
     * Retrieves a specific company by its ID.
     * 
     * @param id the ID of the company to retrieve
     * @return a ResponseEntity containing the company, or a 404 Not Found if not found
     */
	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompany(@PathVariable int id){
		var company = cs.getCompany(id);
		
		return company.isPresent() ? 
				ResponseEntity.ok(company.get()) : ResponseEntity.notFound().build();
	}
	
	 /**
     * Creates a new company.
     * 
     * @param c the CompanyRequest object containing company details
     * @return a ResponseEntity containing the created company and a 201 Created status
     */
	@PostMapping
	public ResponseEntity<Company> addCompany(@Valid @RequestBody CompanyRequest c){
		var addedCompany = cs.addCompany(c);
		
		return ResponseEntity.created(null).body(addedCompany);
	}
	
	/**
     * Updates an existing company by its ID.
     * 
     * @param id the ID of the company to update
     * @param c the CompanyRequest object containing updated company details
     * @return a ResponseEntity with a 200 OK status
     */
	@PutMapping("/{id}")
	public ResponseEntity<Company> addCompany(@PathVariable int id,
			@Valid @RequestBody CompanyRequest c){

			cs.updateCompany(id, c);
		
		return ResponseEntity.ok().build();
	}
	
	/**
     * Deletes a specific company by its ID.
     * 
     * @param id the ID of the company to delete
     * @return a ResponseEntity with a 200 OK status
     */
	@DeleteMapping("/{id}")
	public ResponseEntity<Company> deleteCompany(@PathVariable int id){
		cs.deleteCompany(id);
		
		return ResponseEntity.ok().build();
	}
	
}
