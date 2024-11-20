package com.acs560.bills_analyzer.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.services.BillsAnalysisService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class BillsAnalysisServiceMedianTest {

	private static final Company NIPSCO = new Company(3,"NIPSCO");

	private static final Company AEP = new Company(1, "AEP");
	
	private static final Company DOESNT_EXIST = new Company(5555, "DOESNT EXIST");
	
	private static final String Lucknow = "Lucknow";
	
	private List<Bill> decemberBills = List.of(
			new Bill(12, 2023, AEP, Lucknow, 72.0),
			new Bill(12, 2022, AEP, Lucknow, 81.0));
	
	private List<Bill> januaryBills = List.of(

			new Bill(1, 2024, AEP, Lucknow, 90.0),
			new Bill(1, 2024, AEP, Lucknow, 105.0),
			new Bill(1, 2024, NIPSCO, Lucknow, 88.0));
	
	private List<Bill> februaryBills = List.of(
			new Bill(2, 2024, AEP, Lucknow, 109.0),
			new Bill(2, 2024, AEP, Lucknow, 123.0));

	@Autowired
	private BillsAnalysisService billsAnalysisService;
	
	@Test
	public void testCalculateMedianForANonexistingMonthAndRange_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMedianForMonthRange(9, 1));
	}
	
	@Test
	public void testCalculateMedianForAMonth_shouldReturnCorrectValue(){				
		Double expectedMedian = getMedian(januaryBills);		
		
		var result = billsAnalysisService.calculateMedianForMonth(1);
		
		Assertions.assertEquals(expectedMedian, result, 0.01);
	}
	
	@Test
	public void testCalculateMedianForNonexistingMonth_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMedianForMonth(6));
	}
	
	@Test
	public void testCalculateMedianForAMonthAndName_shouldReturnCorrectValue(){
		
		List<Bill> testBills = januaryBills
				.stream()
				.filter(b-> b.getCompany().getName().equals(AEP.getName()))
				.toList();
		
		double expectedMedian = getMedian(testBills); 
				
		var result = billsAnalysisService.calculateMedianForCompanyMonth(AEP.getId(), 1);
		
		Assertions.assertEquals(expectedMedian, result, 0.01);
	}
	
	@Test
	public void testCalculateMedianForAMonthAndNonexistingName_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateMedianForCompanyMonth(DOESNT_EXIST.getId(), 1));
	}
	
	@Test
	public void testCalculateMedianForANonexistingMonthAndName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateMedianForCompanyMonth(AEP.getId(), 5));
	}
	
	@Test
	public void testCalculateMedianForAMonthAndRange_shouldReturnCorrectValue(){
		List<Bill> expectedBills = new ArrayList<>(decemberBills);
		expectedBills.addAll(januaryBills);
		expectedBills.addAll(februaryBills);	
		
		double expectedMedian = getMedian(expectedBills);		
		
		var result = billsAnalysisService.calculateMedianForMonthRange(1, 1);
		
		Assertions.assertEquals(expectedMedian, result, 0.01);
	}
	
	@Test
	public void testCalculateMedianForAMonthRangeAndName_shouldReturnCorrectValue() {
		List<Bill> expectedBills = new ArrayList<>(decemberBills);
		expectedBills.addAll(januaryBills);
		expectedBills.addAll(februaryBills);
			
		Set<Integer> months = Set.of(1, 2, 12);
		
		var testBills = expectedBills
				.stream()
				.filter(b-> months.contains(
						b.getMonth()) && b.getCompany().getName().equals(AEP.getName()))
				.toList();	
		
		double expectedMedian = getMedian(testBills);
		
		var result = billsAnalysisService.calculateMedian(AEP.getId(), 1, 1);
		
		Assertions.assertEquals(expectedMedian, result, 0.01);
	}
	
	@Test
	public void testCalculateMedianForANonexistingMonthRangeAndName_shouldThrowException() {		
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateMedian(AEP.getId(), 5, 1));
	}
	
	@Test
	public void testCalculateMedianForAMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMedian(DOESNT_EXIST.getId(), 1, 1));
	}
	
	@Test
	public void testCalculateMedianForANonexistingMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMedian(DOESNT_EXIST.getId(), 5, 1));
	}
	
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
	        return Double.parseDouble(String.format("%.2f", medianAmount));
	    } else {
	    	medianAmount = (amounts.get(size / 2 - 1) + amounts.get(size / 2)) / 2.0;
	    	return Double.parseDouble(String.format("%.2f", medianAmount));
	    }
	}
	
	
}
