package com.acs560.bills_analyzer.services;

/**
 * Interface for the bills visualization service
 */
public interface BillsVisualizationService {
	
	/**
	 * Get an overall visualization of bills for expenditure companyId
	 * 	
	 * @param companyId - the companyId of the expenditure
	 * @return A bar plot of top 10 cities expenditure as a png image
	 */
	byte[] plotSpending(int companyId);
}
