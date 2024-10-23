package com.acs560.bills_analyzer.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.requests.CompanyRequest;
import com.acs560.bills_analyzer.services.CompaniesService;

@SpringBootTest
public class CompaniesServiceTest {

	@Autowired
	private CompaniesService cs;
	
	@Test
	public void testGetAllCompanies_shouldReturnCompanies() {
		var result = cs.getCompanies();
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetCompany_shouldReturnCompany() {
		var result = cs.getCompany(1);
		Assertions.assertTrue(result.get().getName().equals("AEP"));
	}
	
	@Test
	public void testGetNonexistingCompany_shouldReturnEmpty() {
		var result = cs.getCompany(10000000); 
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void testAddCompany_shouldAdd() {
		String name = "My Company";
		CompanyRequest c = new CompanyRequest(name);
		
		var added = cs.addCompany(c);
		var company = cs.getCompany(added.getId());
		
		Assertions.assertEquals(added, company.get());
	}
	
	@Test
	public void testAddDuplicateCompany_shouldThrowException() {
		String name = "DUPLICATE";
		CompanyRequest cr = new CompanyRequest(name);
		
		cs.addCompany(cr);
		
		Assertions.assertThrows(DataIntegrityViolationException.class,
				() -> cs.addCompany(cr));
	}
	
	@Test
	public void testAddCompanyWithExistingName_shouldThrowException() {
		CompanyRequest cr = new CompanyRequest("AEP");
		
		Assertions.assertThrows(DataIntegrityViolationException.class, 
				() -> cs.addCompany(cr));
	}
	
	@Test
	public void testUpdateCompany_shouldUpdate() {
		final int ID = 6;
		final String UPDATED_NAME = "UPDATABLE2";
		CompanyRequest cr = new CompanyRequest(UPDATED_NAME);
		
		cs.updateCompany(ID,  cr);
		
		var company = cs.getCompany(ID);
		
		Assertions.assertEquals(company.get().getName(), UPDATED_NAME);
	}
	
	@Test
	public void testUpdateCompanyToExistingName_shouldThrowException() {
		final int ID = 1;
		final String EXISTING_NAME = "CITY UTILITIES";
		
		CompanyRequest cr = new CompanyRequest(EXISTING_NAME);
		
		Assertions.assertThrows(DataIntegrityViolationException.class,
				() -> cs.updateCompany(ID,  cr));
	}
	
	@Test
	public void testUpdateNonexistingCompany_shouldReturnNull() {
		final int ID = 600000;
		final String NAME = "NONEXISTING";
		CompanyRequest cr = new CompanyRequest(NAME);
		
		var result = cs.updateCompany(ID,  cr);
		
		Assertions.assertNull(result);
	}
	
	@Test
	public void testDeleteCompany_shouldDelete() {
		final Company c = new Company(5, "DELETEABLE");
		cs.deleteCompany(c.getId());
		
		Assertions.assertTrue(cs.getCompany(c.getId()).isEmpty());
	}
	
	@Test
	public void testDeleteChildCompany_shouldThrowException() {
		Assertions.assertThrows(DataIntegrityViolationException.class, 
				()-> cs.deleteCompany(1));
	}
	
	@Test
	public void testDeleteNonexistingCompany_shouldReturnFalse() {
		final Company c = new Company(5555555, "DOESNT EXIST");

		Assertions.assertFalse(cs.deleteCompany(c.getId()));
	}
}
