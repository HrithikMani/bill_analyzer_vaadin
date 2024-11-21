package com.acs560.bills_analyzer.services;

public interface BillsAnalysisService {

	double calculateAverageForMonth(int companyId, int month);
	
	double calculateAverage(int companyId, int month, int monthRange);
	
	double calculateAverage(int month);
	
	double calculateAverageForMonthRange(int month, int monthRange);
	
}
