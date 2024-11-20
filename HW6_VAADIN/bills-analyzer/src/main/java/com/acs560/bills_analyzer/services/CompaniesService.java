package com.acs560.bills_analyzer.services;

import java.util.List;
import java.util.Optional;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;

import jakarta.validation.Valid;

public interface CompaniesService {

	 /**
     * Retrieves a list of all companies.
     * 
     * @return a list of Company objects
     */
	List<Company> getCompanies();
	
	/**
     * Retrieves a list of all companies by filter.
     * 
     * @return a list of Company objects
     */
	List<Company> getCompanies(String filter);
	
	/**
     * Retrieves a specific company by its ID.
     * 
     * @param id the ID of the company to retrieve
     * @return an Optional containing the Company if found, or an empty Optional if not found
     */
	Optional<Company> getCompany(int id);
	
	/**
     * Adds a new company based on the provided CompanyRequest.
     * 
     * @param c the CompanyRequest containing the details of the company to add
     * @return the newly added Company object
     */
	Company addCompany(@Valid CompanyRequest c);
	
	/**
     * Updates an existing company identified by its ID.
     * 
     * @param id the ID of the company to update
     * @param c the CompanyRequest containing the updated company details
     * @return the updated Company object, or null if the company was not found
     */
	Company updateCompany(int id, CompanyRequest c);
	
	/**
     * Deletes a company identified by its ID.
     * 
     * @param id the ID of the company to delete
     * @return true if the company was successfully deleted, false otherwise
     */
	boolean deleteCompany(int id);
	
}
