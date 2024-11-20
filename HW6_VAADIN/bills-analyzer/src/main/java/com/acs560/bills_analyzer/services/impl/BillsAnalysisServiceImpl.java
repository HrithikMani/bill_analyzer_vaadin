package com.acs560.bills_analyzer.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.bills_analyzer.entities.BillEntity;
import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.repositories.BillsRepository;
import com.acs560.bills_analyzer.services.BillsAnalysisService;


@Service
public class BillsAnalysisServiceImpl implements BillsAnalysisService {
		
	private BillsRepository br;	
	
	@Autowired
	public BillsAnalysisServiceImpl(BillsRepository br) {
		this.br = br;
	}
	
	/**
	 * Calculate average of a bill filtered by month
	 * 
	 * @param month - the month
	 * @return - the average amount
	 */
	@Override
	public double calculateAverageForMonth(int month) {
		var average = br.calculateAverage(month);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month");
		}
		
		return average;
	}
	
	/**
	 * Calculate average of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId of the bill
	 * @param month - the month
	 * @return - the average amount 
	 */
	@Override
	public double calculateAverageForCompanyMonth(int companyId, int month) throws NoSuchElementException {
		var average = br.calculateAverage(month, companyId);
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month and range");
		}
		
		return average;
	}
	
	/**
	 * Calculate average of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the average amount 
	 */
	@Override
	public double calculateAverageForMonthRange(int month, int range) {	
		var months = MonthRangeUtil.getRange(month, range);
		var average = br.calculateAverage(months);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month and range");
		}
		
		return average;
	}
	
	/**
	 * Calculate average of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the average amount 
	 */
	@Override
	public double calculateAverage(int companyId, int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		var average = br.calculateAverage(months, companyId);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for name, month, and range");
		}
		
		return average;
	}
	
	/**
	 * Calculate median of a bill filtered by month
	 * 
	 * @param month - the month	 
	 * @return - the median amount 
	 */
	@Override
	public double calculateMedianForMonth(int month) {
	    
		List<Bill> bills = from(br.findAllByIdBillingMonth(month));
		return getMedian(bills);
	    
	}
	

	/**
	 * Calculate average of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @return - the median amount 
	 */
	@Override
	public double calculateMedianForCompanyMonth(int companyId, int month) {
		List<Bill> bills = from(br.findAllByIdBillingMonthAndIdCompanyId(month, companyId));
		return getMedian(bills);
	}
	
	/**
	 * Calculate average of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the median amount 
	 */
	@Override
	public double calculateMedianForMonthRange(int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		List<Bill> bills = from(br.findAllByIdBillingMonthIn(months));
		return getMedian(bills);
	}

	/**
	 * Calculate median of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the median amount 
	 */
	@Override
	public double calculateMedian(int companyId, int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		List<Bill> bills = from(br.findAllByIdCompanyIdAndIdBillingMonthIn(companyId, months));		
		return getMedian(bills);
	}

	/**
	 * Calculate mode of a bill filtered by month
	 * 
	 * @param month - the month
	 * @return - the mode amount 
	 */
	@Override
	public List<Double> calculateModeForMonth(int month) {
		List<Bill> bills = from(br.findAllByIdBillingMonth(month));
		return getMode(bills); 		
	}
	

	/**
	 * Calculate mode of a bill filtered by companyId and month
	 * 
	 * @param companyId - the category companyId of the bill
	 * @param month - the month
	 * @return - the mode amount 
	 */
	@Override
	public List<Double> calculateModeForCompanyMonth(int companyId, int month) {		
		List<Bill> bills = from(br.findAllByIdBillingMonthAndIdCompanyId(month, companyId));
		return getMode(bills); 	
	}

	/**
	 * Calculate mode of a bill filtered by month for a given range
	 * 
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the mode amount 
	 */
	@Override
	public List<Double> calculateModeForMonthRange(int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		List<Bill> bills = from(br.findAllByIdBillingMonthIn(months));

		return getMode(bills); 	
	}

	/**
	 * Calculate mode of a bill filtered by companyId, month for a given range
	 * 
	 * @param companyId - the category companyId in the bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return - the mode amount 
	 */
	@Override
	public List<Double> calculateMode(int companyId, int month, int range) {
		var months = MonthRangeUtil.getRange(month, range);
		List<Bill> bills = from(br.findAllByIdCompanyIdAndIdBillingMonthIn(companyId, months));		
		return getMode(bills); 	
	}
		
	/**
	 * Compute median of bills based on filters 
	 * 
	 * @param filter - the filter
	 * @return median of bills
	 */
	private double getMedian(List<Bill> bills) {
		List<Double> amounts = bills
	    		.stream()
	            .map(Bill::getAmount)
	            .sorted()
	            .collect(Collectors.toList());
	    
	    int size = bills.size();
	    if (size == 0) {
	        throw new NoSuchElementException("No bills found for the given month and range.");
	    }
	    
	    double medianAmount = 0;
	    if (size % 2 == 1) {
	    	medianAmount = amounts.get(size / 2);
	        return Double.parseDouble(String.format("%.3f", medianAmount));
	    } else {
	    	medianAmount = (amounts.get(size / 2 - 1) + amounts.get(size / 2)) / 2.0;
	    	return Double.parseDouble(String.format("%.3f", medianAmount));
	    }
	}
	
	/**
	 * Compute mode of bills based on filters 
	 * 
	 * @param filter - the filter
	 * @return mode of bills
	 */
	private List<Double> getMode(List<Bill> bills) {
		List<Double> amounts = bills
				.stream()
                .map(Bill::getAmount)
                .collect(Collectors.toList());

        if (amounts.isEmpty()) {
            throw new NoSuchElementException("No bills found for the given month and range.");
        }

        // Count occurrences of each amount
        Map<Double, Long> frequencyMap = amounts.stream()
                .collect(Collectors.groupingBy(amount -> amount, Collectors.counting()));

        // Find the highest frequency
        long maxFrequency = frequencyMap.values().stream()
                .max(Long::compare)
                .orElseThrow(() -> new NoSuchElementException("No frequency found."));

        // Find all modes (in case there are multiple with the same frequency)
        List<Double> modes = frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        return modes.stream()
                .map(mode -> Double.parseDouble(String.format("%.3f", mode)))
                .collect(Collectors.toList());
	}
	
	/**
	 * Get a total spending analysis of bills for 
	 * every category 
	 * 	
	 * @return the list of spending in each category
	 */
	@Override
	public List<String> calculateCategoriesSummary(){
		List<Bill> bills = getBills();
        Map<String, Double> categoryExpenditures = new HashMap<>();

        // Group expenditures by category
        bills.forEach(bill -> {
        	Company category = bill.getCompany();
            double amount = bill.getAmount();

            // Sum the amounts for each category
            categoryExpenditures.merge(category.getName(), amount, Double::sum);
        });
        
        if (categoryExpenditures.isEmpty()) {
        	throw new NoSuchElementException("No bills found.");
        }
        
        List<String> result = new ArrayList<>();
        result.add("Total Expenditure Summary");
        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
            String category = entry.getKey();
            String amounts = String.format("%.2f", entry.getValue());
            result.add(category + " = " + amounts);
        }

        return result;
	}
	
	/**
	 * Get a total spending analysis of bills for a given month
	 * every category 
	 * 	
	 * @param month - the month
	 * @return the list of spending in each category filtered by month
	 */
	@Override
	public List<String> calculateCategoriesSummary(String month){
		List<Bill> bills = getBills();
		Map<String, Double> categoryExpenditures = new HashMap<>();
		List<String> result = new ArrayList<>();
		int monthNumber = MonthRangeUtil.getMonthNumber(month);
        bills.forEach(bill -> {
            if (bill.getMonth() == monthNumber) { 
                Company category = bill.getCompany(); 
                double amount = bill.getAmount(); 

                // Sum the amounts for each category
                categoryExpenditures.merge(category.getName(), amount, Double::sum);
            }
            
        });
        
        if (categoryExpenditures.isEmpty()) {
        	throw new NoSuchElementException("No bills found for the given month.");
        }
        
        result.add("Total Expenditure Summary for " + month);
        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
            String category = entry.getKey();
            String amounts = String.format("%.2f", entry.getValue());
            result.add(category + " = " + amounts);
        }

        return result;
	}
	
	/**
	 * Get a total spending analysis of bills for a given year
	 * every category 
	 * 
	 * @param year - the year
	 * @return the list of spending in each category filtered by year
	 */
	@Override
	public List<String> calculateCategoriesSummary(int year){
		List<Bill> bills = getBills();
		Map<String, Double> categoryExpenditures = new HashMap<>();
		List<String> result = new ArrayList<>();
        
        bills.forEach(bill -> {
            if (bill.getYear() == year) { 
                Company category = bill.getCompany(); 
                double amount = bill.getAmount(); 

                // Sum the amounts for each category
                categoryExpenditures.merge(category.getName(), amount, Double::sum);
            }
            
        });
        
        if (categoryExpenditures.isEmpty()) {
        	throw new NoSuchElementException("No bills found for the given year.");
        }
        
        result.add("Total Expenditure Summary for " + year);
        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
            String category = entry.getKey();
            String amounts = String.format("%.2f", entry.getValue());
            result.add(category + " = " + amounts);
        }

        return result;
	}
	
	/**
	 * Get a total spending analysis of bills for a given month and year
	 * every category 
	 * 	
	 * @param month - the month
	 * @param year - the year
	 * @return the list of spending in each category filtered by month and year
	 */
	@Override
	public List<String> calculateCategoriesSummary(String month, int year){
		List<Bill> bills = getBills();
		Map<String, Double> categoryExpenditures = new HashMap<>();
		List<String> result = new ArrayList<>();
        
		int monthNumber = MonthRangeUtil.getMonthNumber(month);
        bills.forEach(bill -> {
            if (bill.getMonth() == monthNumber && bill.getYear() == year) { 
                Company category = bill.getCompany(); 
                double amount = bill.getAmount(); 

                // Sum the amounts for each category
                categoryExpenditures.merge(category.getName(), amount, Double::sum);
            }
            
        });
        
        if (categoryExpenditures.isEmpty()) {
        	throw new NoSuchElementException("No bills found for the given month and year.");
        }
        
        result.add("Total Expenditure Summary for " + month + ", "+ year);
        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
            String category = entry.getKey();
            String amounts = String.format("%.2f", entry.getValue());
            result.add(category + " = " + amounts);
        }

        return result;
	}
	
	
	/**
	 * Retrieves a list of bills from the data source.
	 * 
	 * @return a list of Bill objects
	 */
	private List<Bill> getBills() {
		var billEntities = ((List<BillEntity>) br.findAll());
		return from(billEntities);
	}

	/**
	 * Converts a list of BillEntity objects to a list of Bill objects.
	 * 
	 * @param billEntities the list of BillEntity objects to convert
	 * @return a sorted list of Bill objects
	 */
	private List<Bill> from(List<BillEntity> billEntities){
		var bills = billEntities.stream()
				.map(be -> new Bill(be))
				.collect(Collectors.toList());

		if (bills.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		Collections.sort(bills);

		return bills;
	}
	
}
