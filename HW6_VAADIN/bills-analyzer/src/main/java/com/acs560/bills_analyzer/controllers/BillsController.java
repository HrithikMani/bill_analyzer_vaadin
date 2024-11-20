package com.acs560.bills_analyzer.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acs560.bills_analyzer.models.Bill;
import com.acs560.bills_analyzer.services.BillsService;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;

/**
 * The rest controller for handling bills data.
 * 
 */
@RestController()
@RequestMapping("/api/v1/bills")
@NoArgsConstructor
public class BillsController {

	//The bills service that is injected through autowire
	@Autowired
	private BillsService billsService;
	
	/**
	 * Get all bills
	 * @return - the list of @Bill
	 */
	@GetMapping
	public ResponseEntity<List<Bill>> getBills() {
		return ResponseEntity.ok(billsService.getBills());
	}
	
	/**
	 * Get a bill
	 * @param company id - company id of the bill
	 * @param month - month of the bill
	 * @param year - year of the bill
	 * @return - the @Bill
	 */
	@GetMapping("/company/{id}/month/{month}/year/{year}")
	public ResponseEntity<Bill> getBill(@PathVariable int id,
			@PathVariable int month, @PathVariable int year) {
		
		Optional<Bill> bill = billsService.getBill(id, month, year);
		
		ResponseEntity<Bill> response =
				bill.isPresent() ? ResponseEntity.ok(bill.get()) : ResponseEntity.notFound().build();
		
		return response;
	}
	
	/**
	 * Get bills filtered by company id
	 * @param company id - the company id
	 * @return - list of all bills filtered by company id
	 */
	@GetMapping("/company/{id}")
	public ResponseEntity<List<Bill>> getBillsByCompany(@PathVariable int id){
		return ResponseEntity.ok(billsService.getBillsByCompany(id));
	}
	
	/**
	 * Get bills filtered by month
	 * @param month - the month
	 * @return - list of all bills filtered by month
	 */
	@GetMapping("/month/{month}")
	public ResponseEntity<List<Bill>> getBillsByMonth(@PathVariable int month){
		return ResponseEntity.ok(billsService.getBillsByMonth(month));
	}
	
	/**
	 * Get bills filtered by month
	 * @param city - the city
	 * @return - list of all bills filtered by city
	 */
	@GetMapping("/city/{city}")
	public ResponseEntity<List<Bill>> getBillsByCity(@PathVariable String city){
		return ResponseEntity.ok(billsService.getBillsByCity(city));
	}
	
	/**
	 * Get bills filtered by company id and month
	 * @param company id - the company id
	 * @param month - the month
	 * @return - list of all bills filtered by company id and month
	 */
	@GetMapping("/company/{id}/month/{month}")
	public ResponseEntity<List<Bill>> getBills(@PathVariable int id,
			@PathVariable int month){
		return ResponseEntity.ok(billsService.getBillsByCompanyAndMonth(id, month));
	}
	
	/**
	 * Adds a new bill.
	 * 
	 * @param bill - the Bill object to be added
	 * @return a ResponseEntity indicating the result of the operation. 
	 *         The response status is set to 201 Created if the bill is added successfully.
	 */
	@PostMapping()
	public ResponseEntity<String> add(@Valid @RequestBody Bill bill){
		var addedBill = billsService.add(bill);
		
		if (addedBill == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Bill already exists");
		}
		
		return ResponseEntity.created(null).body("Bill added successfully");
	}
	
	/**
	 * Updates an existing bill.
	 * 
	 * @param bill - the Bill object with updated information
	 * @return a ResponseEntity indicating the result of the operation.
	 *         The response status is set to 200 OK if the bill is updated successfully.
	 */
	@PutMapping()
	public ResponseEntity<String> update(@Valid @RequestBody Bill bill){
		var updatedBill = billsService.update(bill);
		
		if (updatedBill == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill does not exist");
		}
		
		return ResponseEntity.created(null).body("Bill updated successfully");
	}
	
	/**
	 * Deletes an existing bill.
	 * 
	 * @param bill - the Bill object to be deleted
	 * @return a ResponseEntity indicating the result of the operation.
	 *         The response status is set to 200 OK if the bill is deleted successfully.
	 */
	@DeleteMapping()
	public ResponseEntity<String> delete(@Valid @RequestBody Bill bill){
		var deleted = billsService.delete(bill);
		
		if (!deleted) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill not found.");
	    }
		
		return ResponseEntity.ok("Bill deleted successfully");
	}
	
}
