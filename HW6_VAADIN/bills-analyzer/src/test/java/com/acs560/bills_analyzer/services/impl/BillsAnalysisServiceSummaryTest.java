package com.acs560.bills_analyzer.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.services.BillsAnalysisService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class BillsAnalysisServiceSummaryTest {
	private static final Company NIPSCO = new Company(3,"NIPSCO");

	private static final Company AEP = new Company(1, "AEP");
	
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
	
	private List<Bill> allBills = new ArrayList<>();
//	
	@BeforeEach
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {	
		allBills.addAll(decemberBills);
		allBills.addAll(januaryBills);
		allBills.addAll(februaryBills);
	}

	@Autowired
	private BillsAnalysisService billsAnalysisService;
	
	
		@Test
		public void testCalculateSummary_shouldReturnCorrectValue() {		
	        List<String> expectedSummery = new ArrayList<>();
	        expectedSummery.add("Total Expenditure Summary");
	        expectedSummery.add("UPDATABLE = 90.00");
	        expectedSummery.add("AEP2 = 1000090.00");
	        expectedSummery.add("AEP = 975.00");
	        expectedSummery.add("NIPSCO = 88.00");
	        	        
	        var result = billsAnalysisService.calculateCategoriesSummary();
	        
			Assertions.assertEquals(expectedSummery, result);
		}
		
		@Test
		public void testCalculateSummaryForAMonth_shouldReturnCorrectValue() {
			Map<String, Double> categoryExpenditures = new HashMap<>();
	
	        januaryBills.forEach(bill -> {
	            String category = bill.getCompany().getName();
	            double amount = bill.getAmount();
	
	            categoryExpenditures.merge(category, amount, Double::sum);
	        });
	        
	        List<String> expectedSummery = new ArrayList<>();
	        expectedSummery.add("Total Expenditure Summary for January");
	        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
	            String category = entry.getKey();
	            String amounts = String.format("%.2f", entry.getValue());
	            expectedSummery.add(category + " = " + amounts);
	        }
	        
	        var result = billsAnalysisService.calculateCategoriesSummary("January");
	        
			Assertions.assertEquals(expectedSummery, result);
		}
		
		@Test
		public void testCalculateSummaryForAYear_shouldReturnCorrectValue() {
			
	        
	        List<String> expectedSummery = new ArrayList<>();
	        expectedSummery.add("Total Expenditure Summary for 2024");
	        expectedSummery.add("AEP = 334.00");	        
	        
	        var result = billsAnalysisService.calculateCategoriesSummary(2024);
	        
			Assertions.assertEquals(expectedSummery, result);
		}
		
		public void testCalculateSummaryForAMonthAndYear_shouldReturnCorrectValue() {
			Map<String, Double> categoryExpenditures = new HashMap<>();
	
			allBills.forEach(bill -> {
	            if (bill.getMonth() == 1 && bill.getYear() == 2024) { 
	                String category = bill.getCompany().getName(); 
	                double amount = bill.getAmount(); 
	
	                // Sum the amounts for each category
	                categoryExpenditures.merge(category, amount, Double::sum);
	            }
	            
	        });
	        
	        List<String> expectedSummery = new ArrayList<>();
	        expectedSummery.add("Total Expenditure Summary for January, 2024");
	        for (Map.Entry<String, Double> entry : categoryExpenditures.entrySet()) {
	            String category = entry.getKey();
	            String amounts = String.format("%.2f", entry.getValue());
	            expectedSummery.add(category + " = " + amounts);
	        }
	        
	        var result = billsAnalysisService.calculateCategoriesSummary("January", 2024);
	        
			Assertions.assertEquals(expectedSummery, result);
		}
		
		@Test
		public void testCalculateSummaryForNonexistingMonth_shouldThrowException() {
			Assertions.assertThrows(NoSuchElementException.class, () -> billsAnalysisService.calculateCategoriesSummary("June"));
		}
		
		@Test
		public void testCalculateSummaryForNonexistingYear_shouldThrowException() {
			Assertions.assertThrows(NoSuchElementException.class, () -> billsAnalysisService.calculateCategoriesSummary(2016));
		}
		@Test
		public void testCalculateSummaryForAYearAndNonexistingMonth_shouldThrowException() {
			Assertions.assertThrows(NoSuchElementException.class, () -> billsAnalysisService.calculateCategoriesSummary("June", 2024));
		}
		
		@Test
		public void testCalculateSummaryForAMonthAndNonexistingYear_shouldThrowException() {
			Assertions.assertThrows(NoSuchElementException.class, () -> billsAnalysisService.calculateCategoriesSummary("January", 2016));
		}
		
		@Test
		public void testCalculateSummaryForNonexistingMonthAndYear_shouldThrowException() {
			Assertions.assertThrows(NoSuchElementException.class, () -> billsAnalysisService.calculateCategoriesSummary("January", 2016));
		}

}
