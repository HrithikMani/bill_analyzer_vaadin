package com.acs560.bills_analyzer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acs560.bills_analyzer.services.BillsAnalysisService;

import lombok.NoArgsConstructor;


/**
 * The rest controller for handling bills analysis data
 * This has end-points for average, median and mode data analysis
 * 
 */
@RestController()
@RequestMapping("/api/v1/billsAnalysis")
@NoArgsConstructor
public class BillsAnalysisController {
	
	//The bills analysis service that is injected through autowire
	@Autowired
	private BillsAnalysisService billsAnalysisService;
	
	
	/**
	 * Constructs a new instance of BillsAnalysisController
	 */
	public BillsAnalysisController(BillsAnalysisService billsAnalysisService) {
		this.billsAnalysisService = billsAnalysisService;
	}
	
	/**
	 * Get an average analysis of bills filtered by month
	 * 	
	 * @param month - the month
	 * @return the average of bill amount
	 */
	@GetMapping("/average/month/{month}")
	public ResponseEntity<Double> getAverage(@PathVariable int month) {
		return ResponseEntity.ok(billsAnalysisService.calculateAverageForMonth(month));
	}
	
	/**
	 * Get an average analysis of bills filtered by id and month
	 * 	
	 * @param id - the category id of bill
	 * @param month - the month
	 * @return the average of bill amount
	 */
	@GetMapping("/average/id/{id}/month/{month}")
	public double getAverageForCompanyMonth(@PathVariable int id, @PathVariable int month) {
		return billsAnalysisService.calculateAverageForCompanyMonth(id, month);
	}	
	
	/**
	 * Get an average analysis of bills filtered by month and range
	 * 	
	 * @param month - the month
	 * @param range - the range of months
	 * @return the average of bill amount
	 */
	@GetMapping("/average/month/{month}/range/{range}")
	public ResponseEntity<Double> getAverage(@PathVariable int month, @PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateAverageForMonthRange(month, range));
	}
	
	/**
	 * Get an average analysis of bills filtered by id, month and range
	 * 
	 * @param id - the category id of bill
	 * @param month - the month
	 * @param range - the range of months
	 * @return the average of bill amount
	 */
	@GetMapping("/average/id/{id}/month/{month}/range/{range}")
	public ResponseEntity<Double> getAverage(@PathVariable int id, @PathVariable int month,
			@PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateAverage(id, month, range));
	}
	
	/**
	 * Get an median analysis of bills filtered by month
	 * 
	 * @param month - the month	 
	 * @return the median of bill amount
	 */
	@GetMapping("/median/month/{month}")
	public ResponseEntity<Double> getMedian(@PathVariable int month) {
		return ResponseEntity.ok(billsAnalysisService.calculateMedianForMonth(month));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and id
	 * 
	 * @param id - the id of the category
	 * @param month - the month
	 * @return the median of bill amount
	 */
	@GetMapping("/median/id/{id}/month/{month}")
	public ResponseEntity<Double> getMedianForCompanyMonth(@PathVariable int id, @PathVariable int month) {
		return ResponseEntity.ok(billsAnalysisService.calculateMedianForCompanyMonth(id, month));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and range 
	 * @param month - the month
	 * @param range - the range of months
	 * @return the median of bill amount
	 */
	@GetMapping("/median/month/{month}/range/{range}")
	public ResponseEntity<Double> getMedian(@PathVariable int month,
			@PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateMedianForMonthRange(month, range));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and id for a specified
	 * range of months
	 * 
	 * @param id - the id of the category
	 * @param month - the month
	 * @param range - the range of months
	 * @return the median of bill amount
	 */
	@GetMapping("/median/id/{id}/month/{month}/range/{range}")
	public ResponseEntity<Double> getMedian(@PathVariable int id, @PathVariable int month, 
			@PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateMedian(id, month, range));
	}
	
	/**
	 * Get an median analysis of bills filtered by month
	 * 
	 * @param month - the month	 
	 * @return a list containing mode or multiple values of modes of bill amounts
	 */
	@GetMapping("/mode/month/{month}")
	public ResponseEntity<List<Double>> getMode(@PathVariable int month) {
		return ResponseEntity.ok(billsAnalysisService.calculateModeForMonth(month));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and id
	 * 
	 * @param id - the id of the category
	 * @param month - the month
	 * @return a list containing mode or multiple values of modes of bill amounts
	 */
	@GetMapping("/mode/id/{id}/month/{month}")
	public ResponseEntity<List<Double>> getModeForCompanyMonth(@PathVariable int id, @PathVariable int month) {
		return ResponseEntity.ok(billsAnalysisService.calculateModeForCompanyMonth(id, month));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and range 
	 * @param month - the month
	 * @param range - the range of months
	 * @return a list containing mode or multiple values of modes of bill amounts
	 */
	@GetMapping("/mode/month/{month}/range/{range}")
	public ResponseEntity<List<Double>> getMode(@PathVariable int month,
			@PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateModeForMonthRange(month, range));
	}
	
	/**
	 * Get an median analysis of bills filtered by month and id for a specified
	 * range of months
	 * 
	 * @param id - the id of the category
	 * @param month - the month
	 * @param range - the range of months
	 * @return a list containing mode or multiple values of modes of bill amounts
	 */
	@GetMapping("/mode/id/{id}/month/{month}/range/{range}")
	public ResponseEntity<List<Double>> getMode(@PathVariable int id, @PathVariable int month, 
			@PathVariable int range) {
		return ResponseEntity.ok(billsAnalysisService.calculateMode(id, month, range));
	}
	
	/**
	 * Get a total spending analysis of bills for 
	 * every category 
	 * 	
	 * @return the list of spending in each category
	 */
	@GetMapping("/summary")
	public List<String> getsummary() {
		return billsAnalysisService.calculateCategoriesSummary();
	}
	
	/**
	 * Get a total spending analysis of bills for a given month
	 * every category 
	 * 	
	 * @param month - the month
	 * @return the list of spending in each category filtered by month
	 */
	@GetMapping("/summary/month/{month}")
	public List<String> getCategories(@PathVariable String month){
		return billsAnalysisService.calculateCategoriesSummary(month);
	}
	
	/**
	 * Get a total spending analysis of bills for a given year
	 * every category 
	 * 	
	 * @param year - the year
	 * @return the list of spending in each category filtered by year
	 */
	@GetMapping("/summary/year/{year}")
	public List<String> getCategories(@PathVariable int year){
		return billsAnalysisService.calculateCategoriesSummary(year);
	}
	
	/**
	 * Get a total spending analysis of bills for a given month and year
	 * every category 
	 * 	
	 * @param month - the month
	 * @param year - the year
	 * @return the list of spending in each category filtered by month and year
	 */
	@GetMapping("/summary/month/{month}/year/{year}")
	public List<String> getCategories(@PathVariable String month, @PathVariable int year){
		return billsAnalysisService.calculateCategoriesSummary(month, year);
	}
	
}
