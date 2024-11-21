package com.acs560.bills_analyzer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.acs560.bills_analyzer.entities.CompanyEntity;

public interface CompaniesRepository extends CrudRepository<CompanyEntity, Integer> {

	CompanyEntity findByName(String name);
	
	List<CompanyEntity> findByNameContains(String name);
	
}
