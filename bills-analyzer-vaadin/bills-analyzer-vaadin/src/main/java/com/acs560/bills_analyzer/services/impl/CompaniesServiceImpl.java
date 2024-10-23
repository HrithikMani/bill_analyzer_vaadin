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
	
	@Autowired
	public CompaniesServiceImpl(CompaniesRepository cr) {
		this.cr = cr;
	}

	@Override
	public List<Company> getCompanies() {
		var companyEntities = cr.findAll();
		List<Company> companies = new ArrayList<>();
		companyEntities.forEach(ce -> companies.add(new Company(ce)));
		
		return companies;
	}

	@Override
	public Optional<Company> getCompany(int id) {
		var ce = cr.findById(id);
		Optional<Company> company = 
				ce.isPresent() ? Optional.of(new Company(ce.get())) : Optional.empty();
		
		return company;
	}

	@Override
	public Company addCompany(CompanyRequest c) {
		var companyToAdd = new CompanyEntity(c);	
		
		var companyEntity = cr.save(companyToAdd);
		
		return new Company(companyEntity);
	}

	@Override
	public Company updateCompany(int id, CompanyRequest c) {
		Company updatedCompany = null;
		
		if (cr.existsById(id)) {
			var companyEntity = cr.save(new CompanyEntity(id, c.getName()));
			updatedCompany = new Company(companyEntity);
		} 
		
		return updatedCompany;
	}

	@Override
	public boolean deleteCompany(int id) {
		boolean isDeleted = false;
		
		if (cr.existsById(id)) {
			cr.deleteById(id);
			isDeleted = true;
		}
		
		return isDeleted;
	}

	@Override
	public List<Company> getCompanies(String filter) {
		var companyEntities = cr.findByNameContains(filter);
		
		List<Company> companies = new ArrayList<>();
		companyEntities.forEach(ce -> companies.add(new Company(ce)));
		
		return companies;
	}

}
