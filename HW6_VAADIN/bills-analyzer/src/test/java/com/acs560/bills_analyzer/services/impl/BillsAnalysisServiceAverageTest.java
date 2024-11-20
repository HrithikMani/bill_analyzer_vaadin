package com.acs560.bills_analyzer.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
public class BillsAnalysisServiceAverageTest {

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
	public void testCalculateAverageForANonexistingMonthAndRange_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateAverageForMonthRange(9, 1));
	}
	
	@Test
	public void testCalculateAverageForAMonth_shouldReturnCorrectValue(){
		double expectedAverage = januaryBills
				.stream()
				.mapToDouble(Bill::getAmount).average().orElseThrow();	
		
		var result = billsAnalysisService.calculateAverageForMonth(1);
		
		Assertions.assertEquals(expectedAverage, result, 0.01);
	}
	
	@Test
	public void testCalculateAverageForNonexistingMonth_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateAverageForMonth(6));
	}
	
	@Test
	public void testCalculateAverageForAMonthAndName_shouldReturnCorrectValue(){
		
		double expectedAverage = januaryBills
				.stream()
				.filter(b-> b.getCompany().getName().equals(AEP.getName()))
				.mapToDouble(Bill::getAmount).average().orElseThrow();	
		
		var result = billsAnalysisService.calculateAverageForCompanyMonth(AEP.getId(), 1);
		
		Assertions.assertEquals(expectedAverage, result, 0.01);
	}
	
	@Test
	public void testCalculateAverageForAMonthAndNonexistingName_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateAverageForCompanyMonth(DOESNT_EXIST.getId(), 1));
	}
	
	@Test
	public void testCalculateAverageForANonexistingMonthAndName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateAverageForCompanyMonth(AEP.getId(), 5));
	}
	
	@Test
	public void testCalculateAverageForAMonthAndRange_shouldReturnCorrectValue(){
		List<Bill> expectedBills = new ArrayList<>(decemberBills);
		expectedBills.addAll(januaryBills);
		expectedBills.addAll(februaryBills);
		
		Set<Integer> months = Set.of(1, 2, 12);
		
		double expectedAverage = expectedBills
				.stream()
				.filter(b-> months.contains(b.getMonth()))
				.mapToDouble(Bill::getAmount).average().orElseThrow();	
		
		var result = billsAnalysisService.calculateAverageForMonthRange(1, 1);
		
		Assertions.assertEquals(expectedAverage, result, 0.01);
	}
	
	
	
	
	@Test
	public void testCalculateAverageForAMonthRangeAndName_shouldReturnCorrectValue() {
		List<Bill> expectedBills = new ArrayList<>(decemberBills);
		expectedBills.addAll(januaryBills);
		expectedBills.addAll(februaryBills);
			
		Set<Integer> months = Set.of(1, 2, 12);
		
		double expectedAverage = expectedBills
				.stream()
				.filter(b-> months.contains(
						b.getMonth()) && b.getCompany().getName().equals(AEP.getName()))
				.mapToDouble(Bill::getAmount).average().orElseThrow();	
		
		var result = billsAnalysisService.calculateAverage(AEP.getId(), 1, 1);
		
		Assertions.assertEquals(expectedAverage, result, 0.01);
	}
	
	@Test
	public void testCalculateAverageForANonexistingMonthRangeAndName_shouldThrowException() {		
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateAverage(AEP.getId(), 5, 1));
	}
	
	@Test
	public void testCalculateAverageForAMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateAverage(DOESNT_EXIST.getId(), 1, 1));
	}
	
	@Test
	public void testCalculateAverageForANonexistingMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateAverage(DOESNT_EXIST.getId(), 5, 1));
	}
	
}
