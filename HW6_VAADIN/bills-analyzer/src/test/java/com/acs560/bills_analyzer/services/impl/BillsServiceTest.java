package com.acs560.bills_analyzer.services.impl;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.models.Company;
import com.acs560.bills_analyzer.services.BillsService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class BillsServiceTest {
	
	private static final Company DUMMY = new Company(3,"doesn't exist");
	private static final Company AEP = new Company(1, "AEP");
	private static final Company AEP2 = new Company(4, "AEP2");
	private static final String Lucknow = "Lucknow";
	
	private static final Bill ANOTHER_BILL_TO_ADD = new Bill(11, 2099, AEP, Lucknow, 50.0);
	private static final Bill BILL_TO_ADD = new Bill(11, 2199, AEP, Lucknow, 50.0);
	private static final Bill DOESNT_EXIST = new Bill(5, 2124, DUMMY, Lucknow, 50.0);
	private static final Bill EXISTING_BILL = new Bill(11, 2023, AEP, Lucknow, 50.0);
	private static final Bill BILL_TO_DELETE = new Bill(5, 2024, AEP2, Lucknow, 50.0);
	
	@Autowired
	private BillsService billsService;
	
	@Test
	public void testGetAllBills_shouldReturnBills() {
		var result = billsService.getBills();
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetAllBillsWithValidRange_shouldReturnBills() {
		var result = billsService.getBillsByMonthAndRange(1, 1);
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetAllBillsWithInvalidRange_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, () -> billsService.getBillsByMonthAndRange(9, 1));
	}

	@Test
	public void testGetBillThatExists_shouldReturnBill() {
		int companyId = EXISTING_BILL.getCompany().getId();
		int month = EXISTING_BILL.getMonth();
		int year = EXISTING_BILL.getYear();
		
		var result = billsService.getBill(companyId, month, year);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void testGetBillThatDoesntExist_shouldReturnEmpty() {
		int companyId = DOESNT_EXIST.getCompany().getId();
		int month = DOESNT_EXIST.getMonth();
		int year = DOESNT_EXIST.getYear();
		
		var result = billsService.getBill(companyId, month, year);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void testAddBill_shouldAdd() {
		billsService.add(BILL_TO_ADD);
		
		int id = BILL_TO_ADD.getCompany().getId();
		int month = BILL_TO_ADD.getMonth();
		int year = BILL_TO_ADD.getYear();
		
		Assertions.assertTrue(billsService.getBill(id, month, year).isPresent());
	}
	
	@Test
	public void testAddDuplicateBill_shouldThrowException() {
		billsService.add(ANOTHER_BILL_TO_ADD);
		
		int id = ANOTHER_BILL_TO_ADD.getCompany().getId();
		int month = ANOTHER_BILL_TO_ADD.getMonth();
		int year = ANOTHER_BILL_TO_ADD.getYear();
		
		Assertions.assertTrue(billsService.getBill(id, month, year).isPresent());
		Assertions.assertNull(billsService.add(ANOTHER_BILL_TO_ADD));
	}
	
	@Test
	public void testDeleteBill_shouldDelete() {	
		
		billsService.delete(BILL_TO_DELETE);
		
		int id = BILL_TO_DELETE.getCompany().getId();
		int month = BILL_TO_DELETE.getMonth();
		int year = BILL_TO_DELETE.getYear();

		Assertions.assertFalse(billsService.getBill(id, month, year).isPresent());
	}
	
	@Test
	public void testDeleteNonexistingBill_shouldReturnFalse() {
		Assertions.assertFalse(billsService.delete(DOESNT_EXIST));
	}
	
	@Test
	public void testUpdateBill_shouldUpdate() {
		final int companyId = EXISTING_BILL.getCompany().getId();
		final String name = EXISTING_BILL.getCompany().getName();
		final int month = EXISTING_BILL.getMonth();
		final int year = EXISTING_BILL.getYear();
		final String city = EXISTING_BILL.getCity();
		final double newAmount = 1_000_000;
		
		Assertions.assertNotEquals(EXISTING_BILL.getAmount(), newAmount, 0.01);
		
		final Bill updatedBill = new Bill(month, year, new Company(companyId, name), city, newAmount);
		billsService.update(updatedBill);
		
		final var bill = billsService.getBill(companyId, month, year);
		
		Assertions.assertEquals(bill.get(), updatedBill);
		Assertions.assertEquals(bill.get().getAmount(), newAmount, 0.01);
	}
	
	@Test
	public void testUpdateNonexistingBill_shouldReturnNull() {
		Assertions.assertNull(billsService.update(DOESNT_EXIST));
	}
	
	@Test
	public void testGetBillsForName_shouldReturnBills() {
		var result = billsService.getBillsByCompany(EXISTING_BILL.getCompany().getId());
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetBillsForNonexistingName_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, ()->billsService.getBillsByCompany(222222) );
	}
	
	@Test
	public void testGetBillsForNameMonthAndRange_shouldReturnBills() {
		var result = billsService.getBills(1, 1, 1);
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetBillsForNameMonthAndInvalidRange_shouldReturnBills() {
		Assertions.assertThrows(NoSuchElementException.class, ()->billsService.getBills(1, 8, 1) );
	}
	
	@Test
	public void testGetAllBillsForMonth_shouldReturnBills() {
		var result = billsService.getBillsByMonth(1);
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetAllBillsForInvalidMonth_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, () -> billsService.getBillsByMonth(9));
	}
	
	@Test
	public void testGetAllBillsForMonthAndName_shouldReturnBills() {
		var result = billsService.getBillsByCompanyAndMonth(1, 1);
		Assertions.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testGetAllBillsForMonthAndInvalidName_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsService.getBillsByCompanyAndMonth(2222, 1));
	}
	
	@Test
	public void testGetAllBillsForNameAndInvalidMonth_shouldThrowException() {
		Assertions.assertThrows(NoSuchElementException.class, 
				() -> billsService.getBillsByCompanyAndMonth(1, 9));
	}
	
}
