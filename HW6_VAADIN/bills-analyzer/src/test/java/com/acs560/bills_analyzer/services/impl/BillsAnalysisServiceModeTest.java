package com.acs560.bills_analyzer.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class BillsAnalysisServiceModeTest {

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
	public void testCalculateModeForANonexistingMonthAndRange_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateModeForMonthRange(9, 1));
	}
	
	@Test
	public void testCalculateModeForAMonth_shouldReturnCorrectValue(){				
		List<Double> expectedMode = new ArrayList<>(Arrays.asList(90.0, 88.0, 105.0));		
		
		List<Double> result = billsAnalysisService.calculateModeForMonth(1);
		
		Assertions.assertEquals(expectedMode, result);
	}
	
	@Test
	public void testCalculateModeForNonexistingMonth_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateModeForMonth(6));
	}
	
	@Test
	public void testCalculateModeForAMonthAndName_shouldReturnCorrectValue(){
		
		List<Bill> testBills = januaryBills
				.stream()
				.filter(b-> b.getCompany().getName().equals(AEP.getName()))
				.toList();	
				
		List<Double> expectedMode = getMode(testBills);
		
		List<Double> result = billsAnalysisService.calculateModeForCompanyMonth(AEP.getId(), 1);
		
		Assertions.assertEquals(expectedMode, result);
	}
	
	@Test
	public void testCalculateModeForAMonthAndNonexistingName_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateModeForCompanyMonth(DOESNT_EXIST.getId(), 1));
	}
	
	@Test
	public void testCalculateModeForANonexistingMonthAndName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateModeForCompanyMonth(AEP.getId(), 5));
	}
	
	@Test
	public void testCalculateModeForAMonthAndRange_shouldReturnCorrectValue(){					
		List<Double> expectedMode = new ArrayList<>(Arrays.asList(72.0, 81.0, 90.0, 88.0, 105.0, 109.0, 123.0));
		
		List<Double> result = billsAnalysisService.calculateModeForMonthRange(1, 1);
		
		Assertions.assertEquals(expectedMode, result);
	}
	
	@Test
	public void testCalculateModeForAMonthRangeAndName_shouldReturnCorrectValue() {
		List<Bill> expectedBills = new ArrayList<>(decemberBills);
		expectedBills.addAll(januaryBills);
		expectedBills.addAll(februaryBills);
			
		Set<Integer> months = Set.of(1, 2, 12);
		
		var testBills = expectedBills
				.stream()
				.filter(b-> months.contains(
						b.getMonth()) && b.getCompany().getName().equals(AEP.getName()))
				.toList();	
		
		List<Double> expectedMode = getMode(testBills);
		
		List<Double> result = billsAnalysisService.calculateMode(AEP.getId(), 1, 1);
		
		Assertions.assertEquals(expectedMode, result);
	}
	
	@Test
	public void testCalculateModeForANonexistingMonthRangeAndName_shouldThrowException() {		
		Assertions.assertThrows(NoSuchElementException.class,
				() -> billsAnalysisService.calculateMode(AEP.getId(), 5, 1));
	}
	
	@Test
	public void testCalculateModeForAMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMode(DOESNT_EXIST.getId(), 1, 1));
	}
	
	@Test
	public void testCalculateModeForANonexistingMonthRangeAndNonexistingName_shouldThrowException(){
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsAnalysisService.calculateMode(DOESNT_EXIST.getId(), 5, 1));
	}
	
	private List<Double> getMode(List<Bill> bills) {
		List<Double> amounts = bills
				.stream()
                .map(Bill::getAmount)
                .collect(Collectors.toList());

        if (amounts.isEmpty()) {
            throw new NoSuchElementException("No bills found for the given month and range.");
        }

        Map<Double, Long> frequencyMap = amounts.stream()
                .collect(Collectors.groupingBy(amount -> amount, Collectors.counting()));

        long maxFrequency = frequencyMap.values().stream()
                .max(Long::compare)
                .orElseThrow(() -> new NoSuchElementException("No frequency found."));

        List<Double> modes = frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        return modes.stream()
                .map(mode -> Double.parseDouble(String.format("%.2f", mode)))
                .collect(Collectors.toList());
	}
	
}
