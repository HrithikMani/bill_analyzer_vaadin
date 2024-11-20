package com.acs560.bills_analyzer.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.acs560.bills_analyzer.entities.BillEntity;
import com.acs560.bills_analyzer.entities.BillEntityId;

public interface BillsRepository extends CrudRepository<BillEntity, BillEntityId> {

	List<BillEntity> findAllByIdCompanyId(int id);
	
	List<BillEntity> findAllByIdBillingMonthAndIdCompanyId(int billingMonth, int id);
	
	Optional<BillEntity> findAllByIdBillingMonthAndIdBillingYearAndIdCompanyId(int billingMonth, int billingYear, int id);

	List<BillEntity> findAllByIdBillingMonth(int billingMonth);
	
	//Non-PK JPA Query
	List<BillEntity> findAllByCity(String city);
	
	List<BillEntity> findAllByIdBillingMonthIn(Set<Integer> months);
	
	List<BillEntity> findAllByIdCompanyIdAndIdBillingMonthIn(int companyId, Set<Integer> months);
	
	@Query(value = """
			SELECT AVG(amount) FROM bills WHERE billing_month = ?1
			""", nativeQuery=true)
	Double calculateAverage(int month);
	
	@Query(value = """
			SELECT AVG(amount) FROM bills WHERE billing_month = ?1 AND company_id = ?2
			""", nativeQuery=true)
	Double calculateAverage(int month, int companyId);
	
	@Query(value = """
			SELECT AVG(amount) FROM bills WHERE billing_month in ?1
			""", nativeQuery=true)
	Double calculateAverage(Set<Integer> months);
	
	@Query(value = """
			SELECT AVG(amount) FROM bills WHERE billing_month in ?1 AND company_id = ?2
			""", nativeQuery=true)
	Double calculateAverage(Set<Integer> months, int companyId);

}
