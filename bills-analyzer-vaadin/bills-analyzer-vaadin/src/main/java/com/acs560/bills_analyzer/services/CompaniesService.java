package com.acs560.bills_analyzer.services;

import java.util.List;
import java.util.Optional;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;

import jakarta.validation.Valid;

public interface CompaniesService {

	List<Company> getCompanies();
	List<Company> getCompanies(String filter);
	
	Optional<Company> getCompany(int id);
	
	Company addCompany(@Valid CompanyRequest c);
	Company updateCompany(int id, CompanyRequest c);
	boolean deleteCompany(int id);
	
}
