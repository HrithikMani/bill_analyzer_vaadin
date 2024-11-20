package com.acs560.bills_analyzer.services.impl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.bills_analyzer.entities.BillEntity;
import com.acs560.bills_analyzer.entities.BillEntityId;
import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.repositories.BillsRepository;
import com.acs560.bills_analyzer.services.BillsService;

import jakarta.validation.Valid;

/**
 * The bills service implementation
 */
@Service
public class BillsServiceImpl implements BillsService {
	
	@Autowired
	BillsRepository br;
	
	/**
	 * Get a bill by companyId, month, and year
	 * 
	 * @param companyId - the companyId
	 * @param month - the month
	 * @param year - the year
	 * @return - the Bill
	 */
	@Override
	public Optional<Bill> getBill(int companyId, int month, int year) {
		BillEntityId id = new BillEntityId(month, year, companyId);
		var be = br.findById(id);
		Optional<Bill> result = be.isPresent() ? Optional.of(new Bill(be.get())) : Optional.empty(); 
		
		return result;
	}
	
	/**
	 * Get all bills
	 * 
	 * @return - the all bills
	 */
	@Override
	public List<Bill> getBills(){
		var billEntities = ((List<BillEntity>) br.findAll());
		return from(billEntities);
	}

	/**
	 * Get all bills filtered by the expenditure companyId
	 * 
	 * @param companyId - the companyId
	 * @return - the bills filtered by the expenditure companyId
	 */
	@Override
	public List<Bill> getBillsByCompany(int companyId) {
		var billEntities = br.findAllByIdCompanyId(companyId);
		return from(billEntities);
	}
	
	/**
	 * Get all bills filtered by the expenditure companyId and month
	 * 
	 * @param companyId - the companyId
	 * @param month - the month
	 * @return - the bills filtered by the expenditure companyId and month
	 */
	@Override
	public List<Bill> getBillsByCompanyAndMonth(int companyId, int month) {
		var billEntities = br.findAllByIdBillingMonthAndIdCompanyId(month, companyId);
		return from(billEntities);
	}
	
	/**
	 * Get all bills filtered by the month
	 * 
	 * @param month - the month
	 * @return - the bills filtered by the month
	 */
	@Override
	public List<Bill> getBillsByMonth(int month) {
		var billEntities = br.findAllByIdBillingMonth(month);
		return from(billEntities);
	}
	
	/**
	 * Get all bills filtered by the month
	 * 
	 * @param city - the city
	 * @return - the bills filtered by the month
	 */
	@Override
	public List<Bill> getBillsByCity(String city){
		var billEntities = br.findAllByCity(city);
		return from(billEntities);	
	};
	
	/**
	 * Get all bills filtered by the month
	 * for a duration specified by the range
	 * 
	 * @param month - the month
	 * @param range - the range
	 * @return - the bills filtered by the month over a range
	 */
	@Override
	public List<Bill> getBillsByMonthAndRange(int month, int range) {
		var months = getRange(month, range);
		var billEntities = br.findAllByIdBillingMonthIn(months);
		
		return from(billEntities);
	}

	
	/**
	 * Get all bills filtered by the expenditure companyId, month and the range 
	 * 
	 * @param companyId - the companyId
	 * @param month - the month
	 * @param range - the range
	 * @return - the bills filtered by the expenditure companyId, month and the range
	 */
	@Override
	public List<Bill> getBills(int companyId, int month, int range) {
		var months = getRange(month, range);
		var billEntities = br.findAllByIdCompanyIdAndIdBillingMonthIn(companyId, months);
		
		return from(billEntities);
	}
	
	/**
	 * Adds an existing bill in the repository
	 * 
	 * @param bill - the Bill object to be added
	 */
	@Override
	public Bill add(@Valid Bill bill) {
		Bill addedBill = null;
		
		BillEntity billToAdd = new BillEntity(bill);
		
		if (!br.existsById(billToAdd.getId())) {
			var billEntity = br.save(billToAdd);
			addedBill = new Bill(billEntity);
		}

		return addedBill;
	}

	/**
	 * Deletes an existing bill in the repository
	 * 
	 * @param bill - the Bill object to be deleted
	 */
	@Override
	public boolean delete(@Valid Bill bill) {
		boolean isDeleted = false;
		BillEntity billToDelete = new BillEntity(bill);
		
		if (br.existsById(billToDelete.getId())) {
			br.delete(new BillEntity(bill));
			isDeleted = true;
		}
		return isDeleted;
	}

	/**
	 * Updates an existing bill in the repository
	 * 
	 * @param bill - the Bill object with updated information
	 */
	@Override
	public Bill update(@Valid Bill bill) {
		Bill updatedBill = null;
		
		BillEntity billToUpdate = new BillEntity(bill);
		
		if (br.existsById(billToUpdate.getId())) {
			var updatedBillEntity = br.save(new BillEntity(bill));
			updatedBill = new Bill(updatedBillEntity);
		}

		return updatedBill;
	}
	
	/**
	 * Returns a set of months where:
	 * All months from before and after current month specified by range  
	 * 
	 * @param month
	 * @param range
	 * @return A set of months
	 */
	private static Set<Integer> getRange(int month, int range) {
		if (range > 12){
			throw new NoSuchElementException("Invalid range.");
		}
		
		int lowerRange = month - range;
		int upperRange = month + range;
		
		Set<Integer> months = new HashSet<>();
		
		for (int i = lowerRange; i <= upperRange; i++) {
			int monthToAdd = i <= 0 ? i + 12 : i;			
			months.add(monthToAdd);
		}
		
		return months;
	}
	
	/**
	 * A function to collect, sort and return a list of bills
	 * 
	 * @param billEntities
	 * @return a list of bills
	 */
	private List<Bill> from(List<BillEntity> billEntities){
		var bills = billEntities.stream()
				.map(be -> new Bill(be))
				.collect(Collectors.toList());

		if (bills.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		bills.sort(Comparator.comparing(Bill::getYear)
				.thenComparing(Bill::getMonth)
				.thenComparing(Bill::getCompany));
		return bills;
	}

}
