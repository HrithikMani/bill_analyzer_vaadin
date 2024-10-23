package com.acs560.bills_analyzer.services;

import java.util.List;
import java.util.Optional;

import com.acs560.bills_analyzer.models.Bill;

import jakarta.validation.Valid;

/**
 * Interface for the bills
 */
public interface BillsService {
	
	/**
	 * Get a bill by company, month, and year
	 * @param companyId - the company id
	 * @param month - the month
	 * @param year - the year
	 * @return - the Bill
	 */
	Optional<Bill> getBill(int companyId, int month, int year);
	
	/**
	 * Get the list of all bills
	 * @return - the list of bills
	 */
	List<Bill> getBills();
	
	/**
	 * Get the bills filtered by company
	 * @param companyId - the company Id
	 * @return - the list of bills filtered by companyId
	 */
	List<Bill> getBillsByCompany(int companyId);
	
	/**
	 * Get the bills filtered by company and month
	 * @param companyId - the company Id
	 * @param month - the month
	 * @return
	 */
	List<Bill> getBillsByCompanyAndMonth(int companyId, int month);
	
	Bill add(@Valid Bill bill);
	boolean delete(@Valid Bill bill);
	Bill update(@Valid Bill bill);

	List<Bill> getBillsByMonth(int month);
	
	List<Bill> getBillsByMonthAndRange(int month, int range);

	List<Bill> getBills(int companyId, int month, int range);
	
}
