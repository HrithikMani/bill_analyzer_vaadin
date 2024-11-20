package com.acs560.bills_analyzer.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.bills_analyzer.entities.CompanyEntity;
import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.repositories.CompaniesRepository;
import com.acs560.bills_analyzer.requests.CompanyRequest;
import com.acs560.bills_analyzer.services.CompaniesService;

@Service
public class CompaniesServiceImpl implements CompaniesService {
	
	private CompaniesRepository cr;
	
	/**
     * Constructs a CompaniesServiceImpl with the specified CompaniesRepository.
     * 
     * @param cr the CompaniesRepository used for data access
     */
	@Autowired
	public CompaniesServiceImpl(CompaniesRepository cr) {
		this.cr = cr;
	}

	 /**
     * Retrieves a list of all companies.
     * 
     * @return a list of Company objects
     */
	@Override
	public List<Company> getCompanies() {
		var companyEntities = cr.findAll();
		List<Company> companies = new ArrayList<>();
		companyEntities.forEach(ce -> companies.add(new Company(ce)));
		
		return companies;
	}
	
	 /**
     * Retrieves a list of all companies by filter.
     * 
     * @return a list of Company objects
     */
	@Override
	public List<Company> getCompanies(String filter) {
		var companyEntities = cr.findByNameContains(filter);
		
		List<Company> companies = new ArrayList<>();
		companyEntities.forEach(ce -> companies.add(new Company(ce)));
		
		return companies;
	}

	/**
     * Retrieves a specific company by its ID.
     * 
     * @param id the ID of the company to retrieve
     * @return an Optional containing the Company if found, or an empty Optional if not found
     */
	@Override
	public Optional<Company> getCompany(int id) {
		var ce = cr.findById(id);
		Optional<Company> company = 
				ce.isPresent() ? Optional.of(new Company(ce.get())) : Optional.empty();
		
		return company;
	}

	/**
     * Adds a new company based on the provided CompanyRequest.
     * 
     * @param c the CompanyRequest containing the details of the company to add
     * @return the newly added Company object
     */
	@Override
	public Company addCompany(CompanyRequest c) {
		var companyToAdd = new CompanyEntity(c);	
		
		var companyEntity = cr.save(companyToAdd);
		
		return new Company(companyEntity);
	}

	/**
     * Updates an existing company identified by its ID.
     * 
     * @param id the ID of the company to update
     * @param c the CompanyRequest containing the updated company details
     * @return the updated Company object, or null if the company was not found
     */
	@Override
	public Company updateCompany(int id, CompanyRequest c) {
		Company updatedCompany = null;
		
		if (cr.existsById(id)) {
			var companyEntity = cr.save(new CompanyEntity(id, c.getName()));
			updatedCompany = new Company(companyEntity);
		} 
		
		return updatedCompany;
	}

	/**
     * Deletes a company identified by its ID.
     * 
     * @param id the ID of the company to delete
     * @return true if the company was successfully deleted, false otherwise
     */
	@Override
	public boolean deleteCompany(int id) {
		boolean isDeleted = false;
		
		if (cr.existsById(id)) {
			cr.deleteById(id);
			isDeleted = true;
		}
		
		return isDeleted;
	}

}
