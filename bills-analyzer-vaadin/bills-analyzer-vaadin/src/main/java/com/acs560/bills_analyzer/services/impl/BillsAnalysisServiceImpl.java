package com.acs560.bills_analyzer.services.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acs560.bills_analyzer.repositories.BillsRepository;
import com.acs560.bills_analyzer.services.BillsAnalysisService;

@Service
public class BillsAnalysisServiceImpl implements BillsAnalysisService {

	private BillsRepository br;
	
	@Autowired
	public BillsAnalysisServiceImpl(BillsRepository br) {
		this.br = br;
	}
	
	@Override
	public double calculateAverageForMonth(int companyId, int month) throws NoSuchElementException {
		var average = br.calculateAverage(month, companyId);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month");
		}
		
		return average;
	}
	
	/**
	 * 
	 * month 2 with range of 3: -1 - 5 [11 thru 5]
	 * 
	 * month 8 with range of 2: 6 - 10
	 * month 8 with range of 3: 5 - 11
	 * 
	 * month 11 with range of 2: 9 - 13
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
	
	@Override
	public double calculateAverage(int month) {
		var average = br.calculateAverage(month);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month");
		}
		
		return average;
	}

	@Override
	public double calculateAverageForMonthRange(int month, int range) {	
		var months = MonthRangeUtil.getRange(month, range);
		var average = br.calculateAverage(months);
		
		if (average == null) {
			throw new NoSuchElementException("No bills exist for month and range");
		}
		
		return average;
	}
}
