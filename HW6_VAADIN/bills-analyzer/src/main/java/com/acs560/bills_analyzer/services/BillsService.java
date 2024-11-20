package com.acs560.bills_analyzer.services;

import java.util.List;
import java.util.Optional;

import com.acs560.bills_analyzer.models.Bill;

/**
 * Interface for the bills
 */
public interface BillsService {
	
	/**
	 * Get a bill by companyId, month, and year
	 * 
	 * @param companyId - the companyId
	 * @param month - the month
	 * @param year - the year
	 * @return - the Bill
	 */
	Optional<Bill> getBill(int companyId, int month, int year);
	
	/**
	 * Get the list of all bills
	 * 
	 * @return - the list of bills
	 */
	List<Bill> getBills();
	
	/**
	 * Get the bills filtered by companyId
	 * 
	 * @param companyId - the companyId
	 * @return - the list of bills filtered by companyId
	 */
	List<Bill> getBillsByCompany(int companyId);
	
	/**
	 * Get the bills filtered by companyId and month
	 * 
	 * @param companyId - the companyId
	 * @param month - the month 
	 * @return - the list of bills filtered by companyId and month
	 */
	List<Bill> getBillsByCompanyAndMonth(int companyId, int month);
	
	/**
	 * Get the bills filtered by month
	 * 
	 * @param month - the month 
	 * @return - the list of bills filtered by month
	 */
	List<Bill> getBillsByMonth(int month);
	
	/**
	 * Get the bills filtered by city
	 * 
	 * @param city - the city 
	 * @return - the list of bills filtered by city
	 */
	List<Bill> getBillsByCity(String city);
	
	/**
	 * Get the bills filtered by month for a duration
	 * specified by an integer range
	 * 
	 * @param month - the month 
	 * @param range - the range
	 * @return - the list of bills filtered by month
	 */
	List<Bill> getBillsByMonthAndRange(int month, int range);

	/**
	 * Get the bills filtered by companyId and month for a duration
	 * specified by an integer range
	 * 
	 * @param companyId - the companyId
	 * @param month - the month 
	 * @param range - the range
	 * @return - the list of bills filtered by month
	 */
	List<Bill> getBills(int companyId, int month, int range);
	
	/**
	 * Adds a new bill to the repository.
	 * 
	 * @param bill - the Bill object to be added
	 */
	Bill add(Bill bill);
	
	/**
	 * Deletes an existing bill in the repository.
	 * 
	 * @param bill - the Bill object to be deleted
	 */
	boolean delete(Bill bill);
	
	/**
	 * Updates an existing bill in the repository.
	 * 
	 * @param bill - the Bill object with updated information
	 */
	Bill update(Bill bill);
	
}
