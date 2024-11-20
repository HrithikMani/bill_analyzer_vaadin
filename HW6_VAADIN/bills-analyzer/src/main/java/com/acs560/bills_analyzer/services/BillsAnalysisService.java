package com.acs560.bills_analyzer.services;

import java.util.List;

/**
 * Interface for the bills analysis service
 */
public interface BillsAnalysisService {
	
	
	/**
	 * Calculate average of a bill filtered by month
	 * 
	 * @param month - the month
	 * @return - the average amount 
	 */
	double calculateAverageForMonth(int month);
	
	/**
	 * Calculate average of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId of the bill
	 * @param month - the month
	 * @return - the average amount 
	 */
	double calculateAverageForCompanyMonth(int companyId, int month);
	
	/**
	 * Calculate average of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the average amount 
	 */
	double calculateAverageForMonthRange(int month, int range);
	
	/**
	 * Calculate average of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the average amount 
	 */
	double calculateAverage(int companyId, int month, int range);
	
	/**
	 * Calculate average of a bill filtered by month
	 * 
	 * @param month - the month	 
	 * @return - the median amount 
	 */
	double calculateMedianForMonth(int month);
	
	/**
	 * Calculate average of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @return - the median amount 
	 */
	double calculateMedianForCompanyMonth(int companyId, int month);
	
	/**
	 * Calculate average of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the median amount 
	 */
	double calculateMedianForMonthRange(int month, int range);
	
	/**
	 * Calculate median of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the median amount 
	 */
	double calculateMedian(int companyId, int month, int range);
	
	/**
	 * Calculate mode of a bill filtered by month
	 * 
	 * @param month - the month
	 * @return - the mode amount 
	 */
	List<Double> calculateModeForMonth(int month);
	
	/**
	 * Calculate mode of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId of the bill
	 * @param month - the month
	 * @return - the mode amount 
	 */
	List<Double> calculateModeForCompanyMonth(int companyId, int month);
	
	/**
	 * Calculate mode of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the mode amount 
	 */
	List<Double> calculateModeForMonthRange(int month, int range);
	
	/**
	 * Calculate mode of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the mode amount 
	 */
	List<Double> calculateMode(int companyId, int month, int range);
	
	/**
	 * Get a total spending analysis of bills for 
	 * every category 
	 * 	
	 * @return the list of spending in each category
	 */
	List<String> calculateCategoriesSummary();
	
	/**
	 * Get a total spending analysis of bills for a given month for
	 * every category 
	 * 	
	 * @param month - the month
	 * @return the list of spending in each category filtered by month
	 */
	List<String> calculateCategoriesSummary(String month);
	
	/**
	 * Get a total spending analysis of bills for a given year for
	 * every category 
	 * 	
	 * @param year - the year
	 * @return the list of spending in each category filtered by year
	 */
	List<String> calculateCategoriesSummary(int year);
	
	/**
	 * Get a total spending analysis of bills for a given month and year for
	 * every category 
	 * 	
	 * @param month - the month
	 * @param year - the year
	 * @return the list of spending in each category filtered by month and year
	 */
	List<String> calculateCategoriesSummary(String month, int year);
	
}
